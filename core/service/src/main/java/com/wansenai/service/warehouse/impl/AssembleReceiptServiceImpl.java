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
import com.wansenai.bo.AssembleStockBO;
import com.wansenai.bo.FileDataBO;
import com.wansenai.dto.warehouse.AssembleReceiptDTO;
import com.wansenai.dto.warehouse.QueryAssembleReceiptDTO;
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
import com.wansenai.service.warehouse.AssembleReceiptService;
import com.wansenai.service.warehouse.WarehouseReceiptSubService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.AssembleReceiptCodeEnum;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AssembleReceiptDetailVO;
import com.wansenai.vo.warehouse.AssembleReceiptVO;
import jakarta.servlet.http.HttpServletResponse;
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
public class AssembleReceiptServiceImpl extends ServiceImpl<WarehouseReceiptMainMapper, WarehouseReceiptMain> implements AssembleReceiptService {

    private final WarehouseReceiptSubService warehouseReceiptSubService;
    private final ProductService productService;
    private final CommonService commonService;
    private final ISysUserService userService;
    private final SysFileMapper fileMapper;
    private final ProductStockMapper productStockMapper;

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

    public AssembleReceiptServiceImpl(WarehouseReceiptSubService warehouseReceiptSubService, ProductService productService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, ProductStockMapper productStockMapper) {
        this.warehouseReceiptSubService = warehouseReceiptSubService;
        this.productService = productService;
        this.commonService = commonService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.productStockMapper = productStockMapper;
    }

    @Override
    public Response<Page<AssembleReceiptVO>> getAssembleReceiptPageList(QueryAssembleReceiptDTO queryAssembleReceiptDTO) {
        var result = new Page<AssembleReceiptVO>();
        var page = new Page<WarehouseReceiptMain>(queryAssembleReceiptDTO.getPage(), queryAssembleReceiptDTO.getPageSize());

        var wrapperMainMapper = lambdaQuery()
                .eq(queryAssembleReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryAssembleReceiptDTO.getOperatorId())
                .eq(queryAssembleReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryAssembleReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryAssembleReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryAssembleReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryAssembleReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryAssembleReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryAssembleReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryAssembleReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryAssembleReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryAssembleReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "组装单")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .page(page);

        var assembleReceiptVOList = new ArrayList<AssembleReceiptVO>(wrapperMainMapper.getRecords().size() + 1);
        wrapperMainMapper.getRecords().forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if(product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var assembleReceiptVO = AssembleReceiptVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            assembleReceiptVOList.add(assembleReceiptVO);
        });
        result.setRecords(assembleReceiptVOList);
        result.setTotal(wrapperMainMapper.getTotal());
        return Response.responseData(result);
    }

    private List<AssembleReceiptVO> getAssembleReceiptList(QueryAssembleReceiptDTO queryAssembleReceiptDTO) {
        var wrapperMainMapper = lambdaQuery()
                .eq(queryAssembleReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryAssembleReceiptDTO.getOperatorId())
                .eq(queryAssembleReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryAssembleReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryAssembleReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryAssembleReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryAssembleReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryAssembleReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryAssembleReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryAssembleReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryAssembleReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryAssembleReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "组装单")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var assembleReceiptVOList = new ArrayList<AssembleReceiptVO>(wrapperMainMapper.size() + 1);
        wrapperMainMapper.forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if(product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var assembleReceiptVO = AssembleReceiptVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            assembleReceiptVOList.add(assembleReceiptVO);
        });
        return assembleReceiptVOList;
    }


        @Override
    public Response<AssembleReceiptDetailVO> getAssembleReceiptDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var receiptMain = lambdaQuery()
                .eq(WarehouseReceiptMain::getId, id)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();

        if (receiptMain != null) {
            var assembleReceiptDetailVO = AssembleReceiptDetailVO.builder()
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
                var tableData = new ArrayList<AssembleStockBO>(warehouseSubs.size() + 1);
                warehouseSubs.forEach(warehouseReceiptSub -> {
                    var product = productStockMapper.getProductSkuByBarCode(warehouseReceiptSub.getProductBarcode(), warehouseReceiptSub.getWarehouseId());

                    var assembleStockBO = AssembleStockBO.builder()
                            .id(warehouseReceiptSub.getId())
                            .warehouseId(warehouseReceiptSub.getWarehouseId())
                            .warehouseName(commonService.getWarehouseName(warehouseReceiptSub.getWarehouseId()))
                            .barCode(warehouseReceiptSub.getProductBarcode())
                            .productId(warehouseReceiptSub.getProductId())
                            .productName(product.getProductName())
                            .productUnit(product.getProductUnit())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .stock(product.getStock())
                            .type(warehouseReceiptSub.getType())
                            .unitPrice(warehouseReceiptSub.getUnitPrice())
                            .amount(warehouseReceiptSub.getTotalAmount())
                            .productNumber(warehouseReceiptSub.getProductNumber())
                            .remark(warehouseReceiptSub.getRemark())
                            .build();
                    tableData.add(assembleStockBO);
                });
                assembleReceiptDetailVO.setTableData(tableData);
            }
            var fileList = commonService.getFileList(receiptMain.getFileId());
            assembleReceiptDetailVO.setFiles(fileList);
            return Response.responseData(assembleReceiptDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateAssembleReceipt(AssembleReceiptDTO assembleReceiptDTO) {
        var userId = userService.getCurrentUserId();
        var fid = processFiles(assembleReceiptDTO.getFiles(), assembleReceiptDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = assembleReceiptDTO.getId() != null;

        var totalProductNumber = 0;
        var totalAmount = BigDecimal.ZERO;

        if(!assembleReceiptDTO.getTableData().isEmpty()) {
            for (AssembleStockBO stockBO : assembleReceiptDTO.getTableData()) {
                totalProductNumber += stockBO.getProductNumber();
                totalAmount = totalAmount.add(stockBO.getAmount());
            }
        }

        if (isUpdate) {
            var beforeReceipt = warehouseReceiptSubService.lambdaQuery()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, assembleReceiptDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                // 组合件subtract操作
                updateProductStock(beforeReceipt, 2);

                var otherWarehouseReceipts = new ArrayList<WarehouseReceiptSub>();
                for (WarehouseReceiptSub warehouseStock : beforeReceipt) {
                    if("普通子件".equals(warehouseStock.getType())) {
                        var otherWarehouseStock = WarehouseReceiptSub.builder()
                                .warehouseId(warehouseStock.getWarehouseId())
                                .productBarcode(warehouseStock.getProductBarcode())
                                .productNumber(warehouseStock.getProductNumber())
                                .build();
                        otherWarehouseReceipts.add(otherWarehouseStock);
                    }
                }
                // 普通子件add操作
                if(!otherWarehouseReceipts.isEmpty()) {
                    updateProductStock(otherWarehouseReceipts, 1);
                }
            }

            var warehouseReceiptMain = lambdaUpdate()
                    .eq(WarehouseReceiptMain::getId, assembleReceiptDTO.getId())
                    .set(assembleReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, assembleReceiptDTO.getStatus())
                    .set(WarehouseReceiptMain::getTotalProductNumber, totalProductNumber)
                    .set(WarehouseReceiptMain::getTotalAmount, totalAmount)
                    .set(StringUtils.hasLength(assembleReceiptDTO.getReceiptDate()), WarehouseReceiptMain::getReceiptDate, assembleReceiptDTO.getReceiptDate())
                    .set(StringUtils.hasLength(assembleReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, assembleReceiptDTO.getRemark())
                    .set(StringUtils.hasLength(fileIds), WarehouseReceiptMain::getFileId, fileIds)
                    .set(WarehouseReceiptMain::getUpdateBy, userId)
                    .set(WarehouseReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            warehouseReceiptSubService.lambdaUpdate()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, assembleReceiptDTO.getId())
                    .remove();

            var mainStockList = assembleReceiptDTO.getTableData();
            var mainStock = mainStockList.stream()
                    .map(item -> WarehouseReceiptSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .warehouseReceiptMainId(assembleReceiptDTO.getId())
                            .productId(item.getProductId())
                            .warehouseId(item.getWarehouseId())
                            .productBarcode(item.getBarCode())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .type(item.getType())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = warehouseReceiptSubService.saveBatch(mainStock);
            updateProductStock(mainStock, 1);

            // 重新普通子件subtract操作
            var subStockList = new ArrayList<WarehouseReceiptSub>();
            for (AssembleStockBO warehouseStock : mainStockList) {
                if("普通子件".equals(warehouseStock.getType())) {
                    var otherWarehouseStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getWarehouseId())
                            .productBarcode(warehouseStock.getBarCode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    subStockList.add(otherWarehouseStock);
                }
            }
            if(!subStockList.isEmpty()) {
                updateProductStock(subStockList, 2);
            }

            if (updateSubResult && warehouseReceiptMain) {
                return Response.responseMsg(AssembleReceiptCodeEnum.UPDATE_ASSEMBLE_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(AssembleReceiptCodeEnum.UPDATE_ASSEMBLE_RECEIPT_ERROR);

        } else {
            var receiptMainId = SnowflakeIdUtil.nextId();
            var mainStockList = assembleReceiptDTO.getTableData();
            var mainStock = mainStockList.stream()
                    .map(item -> WarehouseReceiptSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .warehouseReceiptMainId(receiptMainId)
                            .productId(item.getProductId())
                            .warehouseId(item.getWarehouseId())
                            .productBarcode(item.getBarCode())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .remark(item.getRemark())
                            .type(item.getType())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = warehouseReceiptSubService.saveBatch(mainStock);

            var warehouseReceiptMain = WarehouseReceiptMain.builder()
                    .id(receiptMainId)
                    .productId(assembleReceiptDTO.getTableData().get(0).getProductId())
                    .receiptNumber(assembleReceiptDTO.getReceiptNumber())
                    .type("组装单")
                    .initReceiptNumber(assembleReceiptDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(assembleReceiptDTO.getReceiptDate()))
                    .totalAmount(totalAmount)
                    .totalProductNumber(totalProductNumber)
                    .remark(assembleReceiptDTO.getRemark())
                    .fileId(fileIds)
                    .source(0)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(warehouseReceiptMain);
            updateProductStock(mainStock, 1);
            var subStocks = new ArrayList<WarehouseReceiptSub>();
            for (AssembleStockBO warehouseStock : mainStockList) {
                if("普通子件".equals(warehouseStock.getType())) {
                    var otherWarehouseStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getWarehouseId())
                            .productBarcode(warehouseStock.getBarCode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    subStocks.add(otherWarehouseStock);
                }
            }
            if(!subStocks.isEmpty()) {
                updateProductStock(subStocks, 2);
            }
            if (saveSubResult && saveMainResult) {
                return Response.responseMsg(AssembleReceiptCodeEnum.ADD_ASSEMBLE_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(AssembleReceiptCodeEnum.ADD_ASSEMBLE_RECEIPT_ERROR);
        }
    }

    @Override
    public Response<String> deleteBatchAssembleReceipt(List<Long> ids) {
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
            return Response.responseMsg(AssembleReceiptCodeEnum.DELETE_ASSEMBLE_RECEIPT_ERROR);
        }
        return Response.responseMsg(AssembleReceiptCodeEnum.DELETE_ASSEMBLE_RECEIPT_SUCCESS);
    }

    @Override
    public Response<String> updateAssembleReceiptStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(WarehouseReceiptMain::getStatus, status)
                .in(WarehouseReceiptMain::getId, ids)
                .update();
        if(!updateResult) {
            return Response.responseMsg(AssembleReceiptCodeEnum.UPDATE_ASSEMBLE_RECEIPT_ERROR);
        }
        return Response.responseMsg(AssembleReceiptCodeEnum.UPDATE_ASSEMBLE_RECEIPT_SUCCESS);
    }

    @Override
    public void exportAssembleReceipt(QueryAssembleReceiptDTO queryAssembleReceiptDTO, HttpServletResponse response) throws Exception {
        var data = getAssembleReceiptList(queryAssembleReceiptDTO);
        if (!data.isEmpty()) {
            var file = ExcelUtils.exportFile(ExcelUtils.DEFAULT_FILE_PATH, "组装单", data);
            ExcelUtils.downloadExcel(file, "组装单", response);
        }
    }
}
