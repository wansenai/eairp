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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.AllotStockBO;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.StorageShipmentStockBO;
import com.wansenai.dto.warehouse.AllotReceiptDTO;
import com.wansenai.dto.warehouse.QueryAllotReceiptDTO;
import com.wansenai.entities.product.ProductStock;
import com.wansenai.entities.system.SysFile;
import com.wansenai.entities.user.SysUser;
import com.wansenai.entities.warehouse.WarehouseReceiptMain;
import com.wansenai.entities.warehouse.WarehouseReceiptSub;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.mappers.warehouse.WarehouseReceiptMainMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.service.warehouse.AllotShipmentsService;
import com.wansenai.service.warehouse.WarehouseReceiptSubService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.AllotShipmentCodeEnum;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.OtherShipmentCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AllotReceiptDetailVO;
import com.wansenai.vo.warehouse.AllotReceiptVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    public AllotShipmentsServiceImpl(WarehouseReceiptSubService warehouseReceiptSubService, ProductService productService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, ProductStockMapper productStockMapper) {
        this.warehouseReceiptSubService = warehouseReceiptSubService;
        this.productService = productService;
        this.commonService = commonService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.productStockMapper = productStockMapper;
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
                .page(page);

        var allotReceiptVOList = new ArrayList<AllotReceiptVO>(wrapperMainMapper.getRecords().size() + 1);
        wrapperMainMapper.getRecords().forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if(product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var allotReceiptVO = AllotReceiptVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
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
                            .productId(warehouseReceiptSub.getProductId())
                            .productName(product.getProductName())
                            .productUnit(product.getProductUnit())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .stock(product.getStock())
                            .unitPrice(warehouseReceiptSub.getUnitPrice())
                            .amount(warehouseReceiptSub.getTotalAmount())
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
        var fid = processFiles(allotReceiptDTO.getFiles(), allotReceiptDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = allotReceiptDTO.getId() != null;

        var totalProductNumber = 0;
        var totalAmount = BigDecimal.ZERO;

        if(!allotReceiptDTO.getTableData().isEmpty()) {
            for (AllotStockBO stockBO : allotReceiptDTO.getTableData()) {
                totalProductNumber += stockBO.getProductNumber();
                totalAmount = totalAmount.add(stockBO.getAmount());
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
                    if(warehouseReceiptSub.getOtherWarehouseId() != null) {
                        // 调入方的仓库id setting 到 warehouseId barCode并没有变化（同barCode不同仓库）
                        var otherWarehouseStock = WarehouseReceiptSub.builder()
                                .warehouseId(warehouseReceiptSub.getOtherWarehouseId())
                                .productBarcode(warehouseReceiptSub.getProductBarcode())
                                .productNumber(warehouseReceiptSub.getProductNumber())
                                .build();
                        otherWarehouseReceipts.add(otherWarehouseStock);
                    }
                }
                if(!otherWarehouseReceipts.isEmpty()) {
                    updateProductStock(otherWarehouseReceipts, 2);
                }
            }

            var warehouseReceiptMain = lambdaUpdate()
                    .eq(WarehouseReceiptMain::getId, allotReceiptDTO.getId())
                    .set(allotReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, allotReceiptDTO.getStatus())
                    .set(WarehouseReceiptMain::getTotalProductNumber, totalProductNumber)
                    .set(WarehouseReceiptMain::getTotalAmount, totalAmount)
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
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
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
                if(warehouseStock.getOtherWarehouseId() != null) {
                    var otherWarehouseStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getOtherWarehouseId())
                            .productBarcode(warehouseStock.getProductBarcode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    otherSubtractWarehouseReceipts.add(otherWarehouseStock);
                }
            }
            if(!otherSubtractWarehouseReceipts.isEmpty()) {
                updateProductStock(otherSubtractWarehouseReceipts, 1);
            }

            if (updateSubResult && warehouseReceiptMain) {
                return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);

        } else {
            var receiptMainId = SnowflakeIdUtil.nextId();
            var shipmentStockList = allotReceiptDTO.getTableData();
            var shipmentStock = shipmentStockList.stream()
                    .map(item -> WarehouseReceiptSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .warehouseReceiptMainId(receiptMainId)
                            .productId(item.getProductId())
                            .warehouseId(item.getWarehouseId())
                            .otherWarehouseId(item.getOtherWarehouseId())
                            .productBarcode(item.getBarCode())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = warehouseReceiptSubService.saveBatch(shipmentStock);

            var warehouseReceiptMain = WarehouseReceiptMain.builder()
                    .id(receiptMainId)
                    .productId(allotReceiptDTO.getTableData().get(0).getProductId())
                    .receiptNumber(allotReceiptDTO.getReceiptNumber())
                    .type("调拨出库")
                    .initReceiptNumber(allotReceiptDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(allotReceiptDTO.getReceiptDate()))
                    .totalAmount(totalAmount)
                    .totalProductNumber(totalProductNumber)
                    .remark(allotReceiptDTO.getRemark())
                    .fileId(fileIds)
                    .source(0)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(warehouseReceiptMain);
            updateProductStock(shipmentStock, 2);
            // 重新计算调入方的仓库累加库存
            var otherSubtractWarehouseReceipts = new ArrayList<WarehouseReceiptSub>();
            for (WarehouseReceiptSub warehouseStock : shipmentStock) {
                if(warehouseStock.getOtherWarehouseId() != null) {
                    var otherWarehouseStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getOtherWarehouseId())
                            .productBarcode(warehouseStock.getProductBarcode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    otherSubtractWarehouseReceipts.add(otherWarehouseStock);
                }
            }
            if(!otherSubtractWarehouseReceipts.isEmpty()) {
                updateProductStock(otherSubtractWarehouseReceipts, 1);
            }
            if (saveSubResult && saveMainResult) {
                return Response.responseMsg(AllotShipmentCodeEnum.ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(AllotShipmentCodeEnum.ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
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

        if(!deleteResult) {
            return Response.responseMsg(AllotShipmentCodeEnum.DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
        }
        return Response.responseMsg(AllotShipmentCodeEnum.DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
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
        if(!updateResult) {
            return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR);
        }
        return Response.responseMsg(AllotShipmentCodeEnum.UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS);
    }
}
