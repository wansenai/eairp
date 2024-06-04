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
import com.wansenai.bo.AssembleStockDataExportBO;
import com.wansenai.bo.FileDataBO;
import com.wansenai.dto.warehouse.DisassembleReceiptDTO;
import com.wansenai.dto.warehouse.QueryDisassembleReceiptDTO;
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
import com.wansenai.service.warehouse.DisassembleReceiptService;
import com.wansenai.service.warehouse.WarehouseReceiptSubService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.AssembleReceiptCodeEnum;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.DisassembleReceiptCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.AssembleReceiptDetailVO;
import com.wansenai.vo.warehouse.AssembleReceiptVO;
import com.wansenai.vo.warehouse.DisassembleReceiptDetailVO;
import com.wansenai.vo.warehouse.DisassembleReceiptVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
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
public class DisassembleReceiptServiceImpl extends ServiceImpl<WarehouseReceiptMainMapper, WarehouseReceiptMain> implements DisassembleReceiptService {

    private final WarehouseReceiptSubService warehouseReceiptSubService;
    private final ProductService productService;
    private final CommonService commonService;
    private final ISysUserService userService;
    private final SysFileMapper fileMapper;
    private final ProductStockMapper productStockMapper;

    public DisassembleReceiptServiceImpl(WarehouseReceiptSubService warehouseReceiptSubService, ProductService productService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, ProductStockMapper productStockMapper) {
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
    public Response<Page<DisassembleReceiptVO>> getDisassembleReceiptPageList(QueryDisassembleReceiptDTO queryDisassembleReceiptDTO) {
        var result = new Page<DisassembleReceiptVO>();
        var page = new Page<WarehouseReceiptMain>(queryDisassembleReceiptDTO.getPage(), queryDisassembleReceiptDTO.getPageSize());

        var wrapperMainMapper = lambdaQuery()
                .eq(queryDisassembleReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryDisassembleReceiptDTO.getOperatorId())
                .eq(queryDisassembleReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryDisassembleReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryDisassembleReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryDisassembleReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryDisassembleReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryDisassembleReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryDisassembleReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryDisassembleReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryDisassembleReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryDisassembleReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "拆卸单")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .orderByDesc(WarehouseReceiptMain::getCreateTime)
                .page(page);

        var disAssembleReceiptVOList = new ArrayList<DisassembleReceiptVO>(wrapperMainMapper.getRecords().size() + 1);
        wrapperMainMapper.getRecords().forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if (product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var disAssembleReceiptVO = DisassembleReceiptVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            disAssembleReceiptVOList.add(disAssembleReceiptVO);
        });
        result.setRecords(disAssembleReceiptVOList);
        result.setTotal(wrapperMainMapper.getTotal());
        return Response.responseData(result);
    }

    public List<DisassembleReceiptVO> getDisassembleReceiptList(QueryDisassembleReceiptDTO queryDisassembleReceiptDTO) {
        var wrapperMainMapper = lambdaQuery()
                .eq(queryDisassembleReceiptDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryDisassembleReceiptDTO.getOperatorId())
                .eq(queryDisassembleReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryDisassembleReceiptDTO.getStatus())
                .eq(StringUtils.hasLength(queryDisassembleReceiptDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryDisassembleReceiptDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryDisassembleReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, queryDisassembleReceiptDTO.getRemark())
                .ge(StringUtils.hasLength(queryDisassembleReceiptDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryDisassembleReceiptDTO.getStartDate())
                .le(StringUtils.hasLength(queryDisassembleReceiptDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryDisassembleReceiptDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "拆卸单")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var disAssembleReceiptVOList = new ArrayList<DisassembleReceiptVO>(wrapperMainMapper.size() + 1);
        wrapperMainMapper.forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if (product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var disAssembleReceiptVO = DisassembleReceiptVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            disAssembleReceiptVOList.add(disAssembleReceiptVO);
        });
        return disAssembleReceiptVOList;
    }

    @Override
    public Response<DisassembleReceiptDetailVO> getDisassembleReceiptDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var receiptMain = lambdaQuery()
                .eq(WarehouseReceiptMain::getId, id)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();

        if (receiptMain != null) {
            var disAssembleReceiptDetailVO = DisassembleReceiptDetailVO.builder()
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
                disAssembleReceiptDetailVO.setTableData(tableData);
            }
            var fileList = commonService.getFileList(receiptMain.getFileId());
            disAssembleReceiptDetailVO.setFiles(fileList);
            return Response.responseData(disAssembleReceiptDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateDisassembleReceipt(DisassembleReceiptDTO disassembleReceiptDTO) {
        var userId = userService.getCurrentUserId();
        var fid = processFiles(disassembleReceiptDTO.getFiles(), disassembleReceiptDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = disassembleReceiptDTO.getId() != null;

        var totalProductNumber = 0;
        var totalAmount = BigDecimal.ZERO;

        if (!disassembleReceiptDTO.getTableData().isEmpty()) {
            for (AssembleStockBO stockBO : disassembleReceiptDTO.getTableData()) {
                totalProductNumber += stockBO.getProductNumber();
                totalAmount = totalAmount.add(stockBO.getAmount());
            }
        }

        if (isUpdate) {
            var beforeReceipt = warehouseReceiptSubService.lambdaQuery()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, disassembleReceiptDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                // 组合件add操作
                updateProductStock(beforeReceipt, 1);

                var otherWarehouseReceipts = new ArrayList<WarehouseReceiptSub>();
                for (WarehouseReceiptSub warehouseReceiptSub : beforeReceipt) {
                    if ("普通子件".equals(warehouseReceiptSub.getType())) {
                        var otherWarehouseStock = WarehouseReceiptSub.builder()
                                .warehouseId(warehouseReceiptSub.getOtherWarehouseId())
                                .productBarcode(warehouseReceiptSub.getProductBarcode())
                                .productNumber(warehouseReceiptSub.getProductNumber())
                                .build();
                        otherWarehouseReceipts.add(otherWarehouseStock);
                    }
                }
                // 普通子件subtract操作
                if (!otherWarehouseReceipts.isEmpty()) {
                    updateProductStock(otherWarehouseReceipts, 2);
                }
            }

            var warehouseReceiptMain = lambdaUpdate()
                    .eq(WarehouseReceiptMain::getId, disassembleReceiptDTO.getId())
                    .set(disassembleReceiptDTO.getStatus() != null, WarehouseReceiptMain::getStatus, disassembleReceiptDTO.getStatus())
                    .set(WarehouseReceiptMain::getTotalProductNumber, totalProductNumber)
                    .set(WarehouseReceiptMain::getTotalAmount, totalAmount)
                    .set(StringUtils.hasLength(disassembleReceiptDTO.getReceiptDate()), WarehouseReceiptMain::getReceiptDate, disassembleReceiptDTO.getReceiptDate())
                    .set(StringUtils.hasLength(disassembleReceiptDTO.getRemark()), WarehouseReceiptMain::getRemark, disassembleReceiptDTO.getRemark())
                    .set(StringUtils.hasLength(fileIds), WarehouseReceiptMain::getFileId, fileIds)
                    .set(WarehouseReceiptMain::getUpdateBy, userId)
                    .set(WarehouseReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            warehouseReceiptSubService.lambdaUpdate()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, disassembleReceiptDTO.getId())
                    .remove();

            var mainStockList = disassembleReceiptDTO.getTableData();
            var mainStock = mainStockList.stream()
                    .map(item -> WarehouseReceiptSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .warehouseReceiptMainId(disassembleReceiptDTO.getId())
                            .productId(item.getProductId())
                            .warehouseId(item.getWarehouseId())
                            .productBarcode(item.getBarCode())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .type(item.getType())
                            .totalAmount(item.getAmount())
                            .type(item.getType())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = warehouseReceiptSubService.saveBatch(mainStock);
            updateProductStock(mainStock, 2);

            // 重新普通子件subtract操作
            var subStockList = new ArrayList<WarehouseReceiptSub>();
            for (AssembleStockBO warehouseStock : mainStockList) {
                if ("普通子件".equals(warehouseStock.getType())) {
                    var subStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getWarehouseId())
                            .productBarcode(warehouseStock.getBarCode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    subStockList.add(subStock);
                }
            }
            if (!subStockList.isEmpty()) {
                updateProductStock(subStockList, 1);
            }

            if (updateSubResult && warehouseReceiptMain) {
                return Response.responseMsg(DisassembleReceiptCodeEnum.UPDATE_DISASSEMBLE_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(DisassembleReceiptCodeEnum.UPDATE_DISASSEMBLE_RECEIPT_ERROR);

        } else {
            var receiptMainId = SnowflakeIdUtil.nextId();
            var mainStockList = disassembleReceiptDTO.getTableData();
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
                            .type(item.getType())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = warehouseReceiptSubService.saveBatch(mainStock);

            var warehouseReceiptMain = WarehouseReceiptMain.builder()
                    .id(receiptMainId)
                    .productId(disassembleReceiptDTO.getTableData().get(0).getProductId())
                    .receiptNumber(disassembleReceiptDTO.getReceiptNumber())
                    .type("拆卸单")
                    .initReceiptNumber(disassembleReceiptDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(disassembleReceiptDTO.getReceiptDate()))
                    .totalAmount(totalAmount)
                    .totalProductNumber(totalProductNumber)
                    .remark(disassembleReceiptDTO.getRemark())
                    .fileId(fileIds)
                    .source(0)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(warehouseReceiptMain);
            updateProductStock(mainStock, 2);
            var subStocks = new ArrayList<WarehouseReceiptSub>();
            for (AssembleStockBO warehouseStock : mainStockList) {
                if ("普通子件".equals(warehouseStock.getType())) {
                    var subStock = WarehouseReceiptSub.builder()
                            .warehouseId(warehouseStock.getWarehouseId())
                            .productBarcode(warehouseStock.getBarCode())
                            .productNumber(warehouseStock.getProductNumber())
                            .build();
                    subStocks.add(subStock);
                }
            }
            if (!subStocks.isEmpty()) {
                updateProductStock(subStocks, 1);
            }
            if (saveSubResult && saveMainResult) {
                return Response.responseMsg(DisassembleReceiptCodeEnum.ADD_DISASSEMBLE_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(DisassembleReceiptCodeEnum.ADD_DISASSEMBLE_RECEIPT_ERROR);
        }
    }

    @Override
    public Response<String> deleteBatchDisassembleReceipt(List<Long> ids) {
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

        if (!deleteResult) {
            return Response.responseMsg(DisassembleReceiptCodeEnum.DELETE_DISASSEMBLE_RECEIPT_ERROR);
        }
        return Response.responseMsg(DisassembleReceiptCodeEnum.DELETE_DISASSEMBLE_RECEIPT_SUCCESS);
    }

    @Override
    public Response<String> updateDisassembleReceiptStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(WarehouseReceiptMain::getStatus, status)
                .in(WarehouseReceiptMain::getId, ids)
                .update();
        if (!updateResult) {
            return Response.responseMsg(DisassembleReceiptCodeEnum.UPDATE_DISASSEMBLE_RECEIPT_ERROR);
        }
        return Response.responseMsg(DisassembleReceiptCodeEnum.UPDATE_DISASSEMBLE_RECEIPT_SUCCESS);
    }

    @Override
    public void exportDisAssembleReceipt(QueryDisassembleReceiptDTO queryDisassembleReceiptDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var mainData = getDisassembleReceiptList(queryDisassembleReceiptDTO);
        if (!mainData.isEmpty()) {
            exportMap.put("拆卸单", ExcelUtils.getSheetData(mainData));
            if (queryDisassembleReceiptDTO.getIsExportDetail()) {
                var subData = new ArrayList<AssembleStockDataExportBO>();
                for (DisassembleReceiptVO disassembleReceiptVO : mainData) {
                    var detail = getDisassembleReceiptDetail(disassembleReceiptVO.getId()).getData().getTableData();
                    if(!detail.isEmpty()) {
                        detail.forEach(item -> {
                            var assembleStockBO = AssembleStockDataExportBO.builder()
                                    .receiptNumber(disassembleReceiptVO.getReceiptNumber())
                                    .type(item.getType())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productModel(item.getProductModel())
                                    .productUnit(item.getProductUnit())
                                    .productStandard(item.getProductStandard())
                                    .stock(item.getStock())
                                    .productNumber(item.getProductNumber())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(assembleStockBO);
                        });
                    }
                }
                exportMap.put("拆卸单明细", ExcelUtils.getSheetData(subData));
            }
            ExcelUtils.exportManySheet(response, "拆卸单", exportMap);
        }
    }

    @Override
    public void exportDisAssembleReceiptDetail(String receiptNumber, HttpServletResponse response) throws Exception {
        var id = lambdaQuery()
                .eq(WarehouseReceiptMain::getReceiptNumber, receiptNumber)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(WarehouseReceiptMain::getType, "拆卸单")
                .one()
                .getId();
        var detail = getDisassembleReceiptDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var exportData = new ArrayList<AssembleStockDataExportBO>();
            tableData.forEach(item -> {
                var assembleStockBO = new AssembleStockDataExportBO();
                assembleStockBO.setReceiptNumber(data.getReceiptNumber());
                BeanUtils.copyProperties(item, assembleStockBO);
                exportData.add(assembleStockBO);
            });
            var fileName = data.getReceiptNumber() + "-拆卸单明细";
            ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
        }
    }
}
