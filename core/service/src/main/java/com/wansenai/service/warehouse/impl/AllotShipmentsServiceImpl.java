/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansenai.service.warehouse.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.AllotStockBO;
import com.wansenai.bo.warehouse.AllotStockDataExportBO;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.warehouse.AllotReceiptExportBO;
import com.wansenai.bo.warehouse.AllotReceiptExportEnBO;
import com.wansenai.bo.warehouse.AllotStockDataExportEnBO;
import com.wansenai.dto.warehouse.AllotReceiptDTO;
import com.wansenai.dto.warehouse.QueryAllotReceiptDTO;
import com.wansenai.entities.product.ProductStock;
import com.wansenai.entities.product.ProductStockKeepUnit;
import com.wansenai.entities.system.SysFile;
import com.wansenai.entities.user.SysUser;
import com.wansenai.entities.warehouse.WarehouseReceiptMain;
import com.wansenai.entities.warehouse.WarehouseReceiptSub;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.mappers.warehouse.WarehouseReceiptMainMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.product.ProductStockKeepUnitService;
import com.wansenai.service.product.ProductStockService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.service.warehouse.AllotShipmentsService;
import com.wansenai.service.warehouse.WarehouseReceiptSubService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.AllotShipmentCodeEnum;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AllotReceiptDetailVO;
import com.wansenai.vo.warehouse.AllotReceiptVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AllotShipmentsServiceImpl extends ServiceImpl<WarehouseReceiptMainMapper, WarehouseReceiptMain> implements AllotShipmentsService {

    private final WarehouseReceiptSubService warehouseReceiptSubService;
    private final ProductService productService;
    private final CommonService commonService;
    private final ISysUserService userService;
    private final SysFileMapper fileMapper;
    private final ProductStockMapper productStockMapper;
    private final ProductStockKeepUnitService productStockKeepUnitService;
    private final ProductStockService productStockService;

    public AllotShipmentsServiceImpl(WarehouseReceiptSubService warehouseReceiptSubService, ProductService productService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, ProductStockMapper productStockMapper, ProductStockKeepUnitService productStockKeepUnitService, ProductStockService productStockService) {
        this.warehouseReceiptSubService = warehouseReceiptSubService;
        this.productService = productService;
        this.commonService = commonService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.productStockMapper = productStockMapper;
        this.productStockKeepUnitService = productStockKeepUnitService;
        this.productStockService = productStockService;
    }

    private ArrayList<Long> processFiles(List<FileDataBO> files, Long retailId) {
        var userId = userService.getCurrentUserId();
        var fid = new ArrayList<Long>();
        if (!files.isEmpty()) {
            var receiptMain = getById(retailId);
            if (receiptMain != null && StringUtils.hasLength(receiptMain.getFileId())) {
                var ids = Arrays.stream(receiptMain.getFileId().split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                fileMapper.deleteBatchIds(ids);
            }
            files.forEach(item -> {
                var file = SysFile.builder()
                        .id(SnowflakeIdUtil.nextId())
                        .uid(item.getUid())
                        .fileName(item.getFileName())
                        .fileType(item.getFileType())
                        .fileSize(item.getFileSize())
                        .fileUrl(item.getFileUrl())
                        .createBy(userId)
                        .createTime(LocalDateTime.now())
                        .build();
                fileMapper.insert(file);
                fid.add(file.getId());
            });
        }
        return fid;
    }

    @Transactional
    public void updateProductStock(List<WarehouseReceiptSub> receiptSubList, int stockType) {
        var stockMap = new ConcurrentHashMap<Long, Integer>();

        receiptSubList.forEach(item -> {
            var stock = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if (stock != null) {
                var stockNumber = stock.getStock();
                var productNumber = item.getProductNumber();
                if (stockType == 1) {
                    stockNumber += productNumber;
                } else if (stockType == 2) {
                    stockNumber -= productNumber;
                }
                stockMap.put(stock.getId(), stockNumber);
            }
        });
        receiptSubList.forEach(item2 -> {
            stockMap.forEach((key, value) -> {
                var stock = ProductStock.builder()
                        .productSkuId(key)
                        .warehouseId(item2.getWarehouseId())
                        .currentStockQuantity(BigDecimal.valueOf(value))
                        .build();
                var wrapper = new LambdaUpdateWrapper<ProductStock>()
                        .eq(ProductStock::getProductSkuId, stock.getProductSkuId())
                        .eq(ProductStock::getWarehouseId, stock.getWarehouseId())
                        .set(ProductStock::getCurrentStockQuantity, BigDecimal.valueOf(value));
                productStockMapper.update(stock, wrapper);
            });
        });
    }

    @Override
    public Response<Page<AllotReceiptVO>> getAllotReceiptPageList(QueryAllotReceiptDTO queryAllotReceiptDTO) {
        var result = new Page<AllotReceiptVO>();
        var page = new Page<WarehouseReceiptMain>(queryAllotReceiptDTO.getPage(), queryAllotReceiptDTO.getPageSize());

        var wrapperMainMapper = lambdaQuery()
                .eq(queryAllotReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryAllotReceiptDTO.getOperatorId())
                .eq(queryAllotReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryAllotReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryAllotReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryAllotReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryAllotReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryAllotReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryAllotReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryAllotReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryAllotReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryAllotReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "调拨出库")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .orderByDesc(WarehouseReceiptMain::getCreateTime)
                .page(page);

        var allotReceiptVOList = new ArrayList<AllotReceiptVO>(wrapperMainMapper.getRecords().size() + 1);
        wrapperMainMapper.getRecords().forEach(item -> {

            var receiptSub = warehouseReceiptSubService.lambdaQuery()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, item.getId())
                    .eq(WarehouseReceiptSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            StringBuilder productInfo = new StringBuilder();
            for (WarehouseReceiptSub warehouseReceiptSub : receiptSub) {
                var product = productService.getById(warehouseReceiptSub.getProductId());
                if (product != null) {
                    // 如果product的某个值是null就不拼接该值
                    if (product.getProductName() != null) {
                        productInfo.append(product.getProductName()).append("|");
                    }
                    if (product.getProductStandard() != null) {
                        productInfo.append(product.getProductStandard()).append("|");
                    }
                    if (product.getProductModel() != null) {
                        productInfo.append(product.getProductModel()).append("|");
                    }
                    if (product.getProductUnit() != null) {
                        productInfo.append(product.getProductUnit()).append("|");
                    }
                }
            }

            var operator = userService.getById(item.getCreateBy());
            var allotReceiptVO = AllotReceiptVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo.toString())
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            allotReceiptVOList.add(allotReceiptVO);
        });
        result.setRecords(allotReceiptVOList);
        result.setTotal(wrapperMainMapper.getTotal());
        return Response.responseData(result);
    }

    private List<AllotReceiptExportBO> getAllotReceiptList(QueryAllotReceiptDTO queryAllotReceiptDTO) {
        var wrapperMainMapper = lambdaQuery()
                .eq(queryAllotReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryAllotReceiptDTO.getOperatorId())
                .eq(queryAllotReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryAllotReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryAllotReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryAllotReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryAllotReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryAllotReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryAllotReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryAllotReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryAllotReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryAllotReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "调拨出库")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var allotReceiptExportBOList = new ArrayList<AllotReceiptExportBO>(wrapperMainMapper.size() + 1);
        wrapperMainMapper.forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if (product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var allotReceiptExportBO = AllotReceiptExportBO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            allotReceiptExportBOList.add(allotReceiptExportBO);
        });
        return allotReceiptExportBOList;
    }

    private List<AllotReceiptExportEnBO> getAllotReceiptEnList(QueryAllotReceiptDTO queryAllotReceiptDTO) {
        var wrapperMainMapper = lambdaQuery()
                .eq(queryAllotReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryAllotReceiptDTO.getOperatorId())
                .eq(queryAllotReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryAllotReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryAllotReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryAllotReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryAllotReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryAllotReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryAllotReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryAllotReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryAllotReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryAllotReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "调拨出库")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var allotReceiptExportBOEnList = new ArrayList<AllotReceiptExportEnBO>(wrapperMainMapper.size() + 1);
        wrapperMainMapper.forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if (product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var allotReceiptExportEnBO = AllotReceiptExportEnBO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            allotReceiptExportBOEnList.add(allotReceiptExportEnBO);
        });
        return allotReceiptExportBOEnList;
    }

    @Override
    public Response<AllotReceiptDetailVO> getAllotReceiptDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var receiptMain = lambdaQuery()
                .eq(WarehouseReceiptMain::getId, id)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();

        if (receiptMain != null) {
            var allotReceiptDetailVO = AllotReceiptDetailVO.builder()
                    .id(receiptMain.getId())
                    .receiptNumber(receiptMain.getReceiptNumber())
                    .receiptDate(receiptMain.getReceiptDate())
                    .remark(receiptMain.getRemark())
                    .status(receiptMain.getStatus())
                    .build();
            var warehouseSubs = warehouseReceiptSubService.lambdaQuery()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, id)
                    .eq(WarehouseReceiptSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            if (!warehouseSubs.isEmpty()) {
                var tableData = new ArrayList<AllotStockBO>(warehouseSubs.size() + 1);
                warehouseSubs.forEach(warehouseReceiptSub -> {
                    var product = productStockMapper.getProductSkuByBarCode(warehouseReceiptSub.getProductBarcode(), warehouseReceiptSub.getWarehouseId());

                    var allotStockBO = AllotStockBO.builder()
                            .id(warehouseReceiptSub.getId())
                            .warehouseId(warehouseReceiptSub.getWarehouseId())
                            .warehouseName(commonService.getWarehouseName(warehouseReceiptSub.getWarehouseId()))
                            .otherWarehouseId(warehouseReceiptSub.getOtherWarehouseId())
                            .otherWarehouseName(commonService.getWarehouseName(warehouseReceiptSub.getOtherWarehouseId()))
                            .barCode(warehouseReceiptSub.getProductBarcode())
                            .salePrice(warehouseReceiptSub.getUnitPrice())
                            .productId(warehouseReceiptSub.getProductId())
                            .productName(product.getProductName())
                            .productUnit(product.getProductUnit())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .stock(product.getStock())
                            .productNumber(warehouseReceiptSub.getProductNumber())
                            .remark(warehouseReceiptSub.getRemark())
                            .build();
                    tableData.add(allotStockBO);
                });
                allotReceiptDetailVO.setTableData(tableData);
            }
            var fileList = commonService.getFileList(receiptMain.getFileId());
            allotReceiptDetailVO.setFiles(fileList);
            return Response.responseData(allotReceiptDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateAllotReceipt(AllotReceiptDTO allotReceiptDTO) {
        var userId = userService.getCurrentUserId();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        var fid = processFiles(allotReceiptDTO.getFiles(), allotReceiptDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = allotReceiptDTO.getId() != null;

        var totalProductNumber = 0;
        if (!allotReceiptDTO.getTableData().isEmpty()) {
            for (AllotStockBO stockBO : allotReceiptDTO.getTableData()) {
                totalProductNumber += stockBO.getProductNumber();
            }
        }

        if (isUpdate) {
            var beforeReceipt = warehouseReceiptSubService.lambdaQuery()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, allotReceiptDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                // 调出方add操作
                updateProductStock(beforeReceipt, 1);

                // 调入方仓库进行add操作 这一步是还原之前的库存 因为后面需要重新计算对方库存 这里不涉及到调出方的仓库库存
                var otherWarehouseReceipts = new ArrayList<WarehouseReceiptSub>();
                for (WarehouseReceiptSub warehouseReceiptSub : beforeReceipt) {
                    if (warehouseReceiptSub.getOtherWarehouseId() != null) {
                        // 调入方的仓库id setting 到 warehouseId barCode并没有变化（同barCode不同仓库）
                        var otherWarehouseStock = WarehouseReceiptSub.builder()
                                .warehouseId(warehouseReceiptSub.getOtherWarehouseId())
                                .productBarcode(warehouseReceiptSub.getProductBarcode())
                                .productNumber(warehouseReceiptSub.getProductNumber())
                                .build();
                        otherWarehouseReceipts.add(otherWarehouseStock);
                    }
                }
                if (!otherWarehouseReceipts.isEmpty()) {
                    updateProductStock(otherWarehouseReceipts, 2);
                }
            }

            var warehouseReceiptMain = lambdaUpdate()
                    .eq(WarehouseReceiptMain::getId, allotReceiptDTO.getId())
                    .set(allotReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, allotReceiptDTO.getStatus())
                    .set(WarehouseReceiptMain::getTotalProductNumber, totalProductNumber)
                    .set(StringUtils.hasLength(allotReceiptDTO.getReceiptDate()), WarehouseReceiptMain::getReceiptDate, allotReceiptDTO.getReceiptDate())
                    .set(StringUtils.hasLength(allotReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, allotReceiptDTO.getRemark())
                    .set(StringUtils.hasLength(fileIds), WarehouseReceiptMain::getFileId, fileIds)
                    .set(WarehouseReceiptMain::getUpdateBy, userId)
                    .set(WarehouseReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            warehouseReceiptSubService.lambdaUpdate()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, allotReceiptDTO.getId())
                    .remove();

            var shipmentStockList = allotReceiptDTO.getTableData();
            var shipmentStock = shipmentStockList.stream()
                    .map(item -> WarehouseReceiptSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .warehouseReceiptMainId(allotReceiptDTO.getId())
                            .productId(item.getProductId())
                            .warehouseId(item.getWarehouseId())
                            .otherWarehouseId(item.getOtherWarehouseId())
                            .productBarcode(item.getBarCode())
                            .productNumber(item.getProductNumber())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = warehouseReceiptSubService.saveBatch(shipmentStock);
            updateProductStock(shipmentStock, 2);

            // 重新计算调入方的仓库累加库存
            var otherSubtractWarehouseReceipts = new ArrayList<WarehouseReceiptSub>();
            for (WarehouseReceiptSub warehouseStock : shipmentStock) {
                if (warehouseStock.getOtherWarehouseId() != null) {
                    var otherWarehouseStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getOtherWarehouseId())
                            .productBarcode(warehouseStock.getProductBarcode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    otherSubtractWarehouseReceipts.add(otherWarehouseStock);
                }
            }
            if (!otherSubtractWarehouseReceipts.isEmpty()) {
                updateProductStock(otherSubtractWarehouseReceipts, 1);
            }

            if (updateSubResult && warehouseReceiptMain) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
                }
                return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
                }
                return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR_EN);
            }

        } else {
            var receiptMainId = SnowflakeIdUtil.nextId();
            // 单据主表
            var warehouseReceiptMain = WarehouseReceiptMain.builder()
                    .id(receiptMainId)
                    .receiptNumber(allotReceiptDTO.getReceiptNumber())
                    .type("调拨出库")
                    .initReceiptNumber(allotReceiptDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(allotReceiptDTO.getReceiptDate()))
                    .totalProductNumber(totalProductNumber)
                    .remark(allotReceiptDTO.getRemark())
                    .fileId(fileIds)
                    .source(0)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(warehouseReceiptMain);

            // 单据子表
            var shipmentStockList = allotReceiptDTO.getTableData();
            var shipmentSubList = new ArrayList<WarehouseReceiptSub>();
            var otherWarehouseStockList = new ArrayList<ProductStock>();
            var otherWarehouseSkuList = new ArrayList<ProductStockKeepUnit>();
            for (AllotStockBO allotStockBO : shipmentStockList) {
                var shipmentStock = WarehouseReceiptSub.builder()
                        .id(SnowflakeIdUtil.nextId())
                        .warehouseReceiptMainId(receiptMainId)
                        .productId(allotStockBO.getProductId())
                        .warehouseId(allotStockBO.getWarehouseId())
                        .unitPrice(allotStockBO.getSalePrice())
                        .otherWarehouseId(allotStockBO.getOtherWarehouseId())
                        .productBarcode(allotStockBO.getBarCode())
                        .productNumber(allotStockBO.getProductNumber())
                        .remark(allotStockBO.getRemark())
                        .createBy(userId)
                        .createTime(LocalDateTime.now())
                        .build();

                shipmentSubList.add(shipmentStock);

                // 查询调入方仓库是否存在该Barcode商品，如果不存在需要新增数据到Stock和SKU表以及Report表
                // 如果存在需要修改SkU表的价格和STOCK表的库存以及Report表的库存

                var product = productService.getById(shipmentStock.getProductId());
                if (allotStockBO.getBarCode().equals(shipmentStock.getProductBarcode())) {
                    // 如果出现重复的productSku数据就不添加到List
                    var productStock = productStockMapper.getProductSkuByBarCode(shipmentStock.getProductBarcode(), shipmentStock.getOtherWarehouseId());
                    if (productStock != null) {
                        var stockNumber = productStock.getStock();
                        var productNumber = shipmentStock.getProductNumber();
                        stockNumber += productNumber;
                        var stock = ProductStock.builder()
                                .id(productStock.getId())
                                .currentStockQuantity(BigDecimal.valueOf(stockNumber))
                                .build();
                        productStockMapper.updateById(stock);
                        // 修改价格
                        var productSku = ProductStockKeepUnit.builder()
                                .id(productStock.getId())
                                .retailPrice(allotStockBO.getSalePrice())
                                .salePrice(allotStockBO.getSalePrice())
                                .lowPrice(allotStockBO.getSalePrice())
                                .updateBy(userId)
                                .updateTime(LocalDateTime.now())
                                .build();
                        productStockKeepUnitService.updateById(productSku);
                    } else {
                        var productSku = ProductStockKeepUnit.builder()
                                .id(SnowflakeIdUtil.nextId())
                                .productId(shipmentStock.getProductId())
                                .productBarCode(shipmentStock.getProductBarcode())
                                .productUnit(product.getProductUnit())
                                .multiAttribute(product.getProductManufacturer())
                                .purchasePrice(BigDecimal.ZERO)
                                .retailPrice(allotStockBO.getSalePrice())
                                .salePrice(allotStockBO.getSalePrice())
                                .lowPrice(allotStockBO.getSalePrice())
                                .deleteFlag(CommonConstants.NOT_DELETED)
                                .createTime(LocalDateTime.now())
                                .createBy(userId)
                                .build();
                        otherWarehouseSkuList.add(productSku);

                        var productStock2 = ProductStock.builder()
                                .id(SnowflakeIdUtil.nextId())
                                .tenantId(userService.getCurrentTenantId())
                                .productSkuId(productSku.getId())
                                .warehouseId(shipmentStock.getOtherWarehouseId())
                                .initStockQuantity(BigDecimal.ZERO)
                                .lowStockQuantity(BigDecimal.ZERO)
                                .highStockQuantity(BigDecimal.ZERO)
                                .currentStockQuantity(BigDecimal.valueOf(shipmentStock.getProductNumber()))
                                .createTime(LocalDateTime.now())
                                .createBy(userId)
                                .updateBy(userId)
                                .deleteFlag(CommonConstants.NOT_DELETED)
                                .build();
                        otherWarehouseStockList.add(productStock2);
                    }
                }

            }

            // 重新计算调入方的仓库累加库存
            var otherSubtractWarehouseReceipts = new ArrayList<WarehouseReceiptSub>();
            for (WarehouseReceiptSub warehouseStock : shipmentSubList) {
                if (warehouseStock.getOtherWarehouseId() != null) {
                    var otherWarehouseStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getOtherWarehouseId())
                            .productBarcode(warehouseStock.getProductBarcode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    otherSubtractWarehouseReceipts.add(otherWarehouseStock);
                }
            }

            if (!otherSubtractWarehouseReceipts.isEmpty()) {
                updateProductStock(otherSubtractWarehouseReceipts, 1);
            }

            var saveSubResult = warehouseReceiptSubService.saveBatch(shipmentSubList);
            if (!otherWarehouseSkuList.isEmpty()) {
                productStockKeepUnitService.saveBatch(otherWarehouseSkuList);
            }
            if (!otherWarehouseStockList.isEmpty()) {
                productStockService.saveBatch(otherWarehouseStockList);
            }
            updateProductStock(shipmentSubList, 2);

            if (saveSubResult && saveMainResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(AllotShipmentCodeEnum.ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
                }
                return Response.responseMsg(AllotShipmentCodeEnum.ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(AllotShipmentCodeEnum.ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
                }
                return Response.responseMsg(AllotShipmentCodeEnum.ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR_EN);
            }
        }
    }

    @Override
    public Response<String> deleteBatchAllotReceipt(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var deleteResult = lambdaUpdate()
                .set(WarehouseReceiptMain::getDeleteFlag, CommonConstants.DELETED)
                .in(WarehouseReceiptMain::getId, ids)
                .update();

        warehouseReceiptSubService.lambdaUpdate()
                .set(WarehouseReceiptSub::getDeleteFlag, CommonConstants.DELETED)
                .in(WarehouseReceiptSub::getWarehouseReceiptMainId, ids)
                .update();

        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if (!deleteResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(AllotShipmentCodeEnum.DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
            }
            return Response.responseMsg(AllotShipmentCodeEnum.DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(AllotShipmentCodeEnum.DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(AllotShipmentCodeEnum.DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS_EN);
        }
    }

    @Override
    public Response<String> updateAllotReceiptStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(WarehouseReceiptMain::getStatus, status)
                .in(WarehouseReceiptMain::getId, ids)
                .update();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if (!updateResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
            }
            return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS_EN);
        }
    }

    @Override
    public void exportAllotReceipt(QueryAllotReceiptDTO queryAllotReceiptDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            var mainData = getAllotReceiptList(queryAllotReceiptDTO);
            if (!mainData.isEmpty()) {
                exportMap.put("调拨出库", ExcelUtils.getSheetData(mainData));
                if (queryAllotReceiptDTO.getIsExportDetail()) {
                    var subData = new ArrayList<AllotStockDataExportBO>();
                    for (AllotReceiptExportBO allotReceiptExportBO : mainData) {
                        var detail = getAllotReceiptDetail(allotReceiptExportBO.getId()).getData().getTableData();
                        if (!detail.isEmpty()) {
                            detail.forEach(item -> {
                                var allotStockBo = AllotStockDataExportBO.builder()
                                        .receiptNumber(allotReceiptExportBO.getReceiptNumber())
                                        .warehouseName(item.getWarehouseName())
                                        .otherWarehouseName(item.getOtherWarehouseName())
                                        .barCode(item.getBarCode())
                                        .productName(item.getProductName())
                                        .productStandard(item.getProductStandard())
                                        .productModel(item.getProductModel())
                                        .productUnit(item.getProductUnit())
                                        .stock(item.getStock())
                                        .productNumber(item.getProductNumber())
                                        .remark(item.getRemark())
                                        .build();
                                subData.add(allotStockBo);
                            });
                        }
                    }
                    exportMap.put("调拨出库明细", ExcelUtils.getSheetData(subData));
                }
                ExcelUtils.exportManySheet(response, "调拨出库", exportMap);
            }
        } else {
            var mainEnData = getAllotReceiptEnList(queryAllotReceiptDTO);
            if (!mainEnData.isEmpty()) {
                exportMap.put("Transfer Outbound", ExcelUtils.getSheetData(mainEnData));
                if (queryAllotReceiptDTO.getIsExportDetail()) {
                    var subEnData = new ArrayList<AllotStockDataExportEnBO>();
                    for (AllotReceiptExportEnBO allotReceiptExportEnBO : mainEnData) {
                        var detail = getAllotReceiptDetail(allotReceiptExportEnBO.getId()).getData().getTableData();
                        if (!detail.isEmpty()) {
                            detail.forEach(item -> {
                                var allotStockEnBo = AllotStockDataExportEnBO.builder()
                                        .receiptNumber(allotReceiptExportEnBO.getReceiptNumber())
                                        .warehouseName(item.getWarehouseName())
                                        .otherWarehouseName(item.getOtherWarehouseName())
                                        .barCode(item.getBarCode())
                                        .productName(item.getProductName())
                                        .productStandard(item.getProductStandard())
                                        .productModel(item.getProductModel())
                                        .productUnit(item.getProductUnit())
                                        .stock(item.getStock())
                                        .productNumber(item.getProductNumber())
                                        .remark(item.getRemark())
                                        .build();
                                subEnData.add(allotStockEnBo);
                            });
                        }
                    }
                    exportMap.put("Transfer Outbound Details", ExcelUtils.getSheetData(subEnData));
                }
                ExcelUtils.exportManySheet(response, "Transfer Outbound", exportMap);
            }
        }
    }

    @Override
    public void exportAllotReceiptDetail(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(WarehouseReceiptMain::getReceiptNumber, receiptNumber)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(WarehouseReceiptMain::getType, "调拨出库")
                .one()
                .getId();
        var detail = getAllotReceiptDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
            if ("zh_CN".equals(systemLanguage)) {
                var exportData = new ArrayList<AllotStockDataExportBO>();
                tableData.forEach(item -> {
                    var allotStockBo = new AllotStockDataExportBO();
                    allotStockBo.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, allotStockBo);
                    exportData.add(allotStockBo);
                });
                var fileName = data.getReceiptNumber() + "-调拨出库明细";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
            } else {
                var exportEnData = new ArrayList<AllotStockDataExportEnBO>();
                tableData.forEach(item -> {
                    var allotStockEnBo = new AllotStockDataExportEnBO();
                    allotStockEnBo.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, allotStockEnBo);
                    exportEnData.add(allotStockEnBo);
                });
                var fileName = data.getReceiptNumber() + "- Transfer Outbound Details";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportEnData));
            }
        }
    }
}
