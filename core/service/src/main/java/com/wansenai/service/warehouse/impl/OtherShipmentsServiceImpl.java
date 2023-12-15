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
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.StorageShipmentStockBO;
import com.wansenai.bo.StorageShipmentStockExportBO;
import com.wansenai.dto.warehouse.OtherShipmentDTO;
import com.wansenai.dto.warehouse.QueryOtherShipmentDTO;
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
import com.wansenai.service.warehouse.OtherShipmentsService;
import com.wansenai.service.warehouse.WarehouseReceiptSubService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.OtherShipmentCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.warehouse.OtherShipmentDetailVO;
import com.wansenai.vo.warehouse.OtherShipmentVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
public class OtherShipmentsServiceImpl extends ServiceImpl<WarehouseReceiptMainMapper, WarehouseReceiptMain> implements OtherShipmentsService {

    private final WarehouseReceiptSubService warehouseReceiptSubService;
    private final ProductService productService;
    private final CommonService commonService;
    private final ISysUserService userService;
    private final SysFileMapper fileMapper;
    private final ProductStockMapper productStockMapper;

    public OtherShipmentsServiceImpl(WarehouseReceiptSubService warehouseReceiptSubService, ProductService productService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, ProductStockMapper productStockMapper) {
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

    private void updateProductStock(List<WarehouseReceiptSub> receiptSubList, int stockType) {
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
    public Response<Page<OtherShipmentVO>> getOtherShipmentsPageList(QueryOtherShipmentDTO queryOtherShipmentDTO) {
        var result = new Page<OtherShipmentVO>();
        var page = new Page<WarehouseReceiptMain>(queryOtherShipmentDTO.getPage(), queryOtherShipmentDTO.getPageSize());

        var wrapperMainMapper = lambdaQuery()
                .eq(queryOtherShipmentDTO.getCustomerId() != null, WarehouseReceiptMain::getRelatedPersonId, queryOtherShipmentDTO.getCustomerId())
                .eq(queryOtherShipmentDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryOtherShipmentDTO.getOperatorId())
                .eq(queryOtherShipmentDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryOtherShipmentDTO.getStatus())
                .eq(StringUtils.hasLength(queryOtherShipmentDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryOtherShipmentDTO.getReceiptNumber())
                .eq(StringUtils.hasLength(queryOtherShipmentDTO.getOtherReceipt()), WarehouseReceiptMain::getReceiptNumber, queryOtherShipmentDTO.getOtherReceipt())
                .like(StringUtils.hasLength(queryOtherShipmentDTO.getRemark()), WarehouseReceiptMain::getRemark, queryOtherShipmentDTO.getRemark())
                .ge(StringUtils.hasLength(queryOtherShipmentDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryOtherShipmentDTO.getStartDate())
                .le(StringUtils.hasLength(queryOtherShipmentDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryOtherShipmentDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "其他出库")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .page(page);

        var otherShipmentVOList = new ArrayList<OtherShipmentVO>(wrapperMainMapper.getRecords().size() + 1);
        wrapperMainMapper.getRecords().forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if(product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var otherShipmentVO = OtherShipmentVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .customerName(commonService.getCustomerName(item.getRelatedPersonId()))
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            otherShipmentVOList.add(otherShipmentVO);
        });
        result.setRecords(otherShipmentVOList);
        result.setTotal(wrapperMainMapper.getTotal());
        return Response.responseData(result);
    }

    private List<OtherShipmentVO> getOtherShipmentsList(QueryOtherShipmentDTO queryOtherShipmentDTO) {
        var wrapperMainMapper = lambdaQuery()
                .eq(queryOtherShipmentDTO.getCustomerId() != null, WarehouseReceiptMain::getRelatedPersonId, queryOtherShipmentDTO.getCustomerId())
                .eq(queryOtherShipmentDTO.getOperatorId() != null, WarehouseReceiptMain::getCreateBy, queryOtherShipmentDTO.getOperatorId())
                .eq(queryOtherShipmentDTO.getStatus() != null, WarehouseReceiptMain::getStatus, queryOtherShipmentDTO.getStatus())
                .eq(StringUtils.hasLength(queryOtherShipmentDTO.getReceiptNumber()), WarehouseReceiptMain::getReceiptNumber, queryOtherShipmentDTO.getReceiptNumber())
                .eq(StringUtils.hasLength(queryOtherShipmentDTO.getOtherReceipt()), WarehouseReceiptMain::getReceiptNumber, queryOtherShipmentDTO.getOtherReceipt())
                .like(StringUtils.hasLength(queryOtherShipmentDTO.getRemark()), WarehouseReceiptMain::getRemark, queryOtherShipmentDTO.getRemark())
                .ge(StringUtils.hasLength(queryOtherShipmentDTO.getStartDate()), WarehouseReceiptMain::getCreateTime, queryOtherShipmentDTO.getStartDate())
                .le(StringUtils.hasLength(queryOtherShipmentDTO.getEndDate()), WarehouseReceiptMain::getCreateTime, queryOtherShipmentDTO.getEndDate())
                .eq(WarehouseReceiptMain::getType, "其他出库")
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var otherShipmentVOList = new ArrayList<OtherShipmentVO>(wrapperMainMapper.size() + 1);
        wrapperMainMapper.forEach(item -> {

            var product = productService.getById(item.getProductId());
            var productInfo = "";
            if(product != null) {
                productInfo = product.getProductName() + "|" + product.getProductStandard() + "|" + product.getProductModel() + "|" + product.getProductUnit();
            }

            var operator = userService.getById(item.getCreateBy());
            var otherShipmentVO = OtherShipmentVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(productInfo)
                    .customerName(commonService.getCustomerName(item.getRelatedPersonId()))
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator).map(SysUser::getName).orElse(""))
                    .productNumber(item.getTotalProductNumber())
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();

            otherShipmentVOList.add(otherShipmentVO);
        });
        return otherShipmentVOList;
    }

    @Override
    public Response<OtherShipmentDetailVO> getOtherShipmentsDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var receiptMain = lambdaQuery()
                .eq(WarehouseReceiptMain::getId, id)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();

        if (receiptMain != null) {
            var otherShipmentDetailVO = OtherShipmentDetailVO.builder()
                    .id(receiptMain.getId())
                    .customerId(receiptMain.getRelatedPersonId())
                    .customerName(commonService.getCustomerName(receiptMain.getRelatedPersonId()))
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
                var tableData = new ArrayList<StorageShipmentStockBO>(warehouseSubs.size() + 1);
                warehouseSubs.forEach(warehouseReceiptSub -> {
                    var product = productStockMapper.getProductSkuByBarCode(warehouseReceiptSub.getProductBarcode(), warehouseReceiptSub.getWarehouseId());

                    var storageShipmentStockBO = StorageShipmentStockBO.builder()
                            .id(warehouseReceiptSub.getId())
                            .warehouseId(warehouseReceiptSub.getWarehouseId())
                            .warehouseName(commonService.getWarehouseName(warehouseReceiptSub.getWarehouseId()))
                            .barCode(warehouseReceiptSub.getProductBarcode())
                            .productId(warehouseReceiptSub.getProductId())
                            .productName(product.getProductName())
                            .productUnit(product.getProductUnit())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productExtendInfo(product.getExtendInfo())
                            .stock(product.getStock())
                            .unitPrice(warehouseReceiptSub.getUnitPrice())
                            .amount(warehouseReceiptSub.getTotalAmount())
                            .productNumber(warehouseReceiptSub.getProductNumber())
                            .remark(warehouseReceiptSub.getRemark())
                            .build();
                    tableData.add(storageShipmentStockBO);
                });
                otherShipmentDetailVO.setTableData(tableData);
            }
            var fileList = commonService.getFileList(receiptMain.getFileId());
            otherShipmentDetailVO.setFiles(fileList);
            return Response.responseData(otherShipmentDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    public Response<String> addOrUpdateOtherShipments(OtherShipmentDTO otherShipmentsDTO) {
        var userId = userService.getCurrentUserId();
        var fid = processFiles(otherShipmentsDTO.getFiles(), otherShipmentsDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = otherShipmentsDTO.getId() != null;

        var totalProductNumber = 0;
        var totalAmount = BigDecimal.ZERO;

        if(!otherShipmentsDTO.getTableData().isEmpty()) {
            for (StorageShipmentStockBO stockBO : otherShipmentsDTO.getTableData()) {
                totalProductNumber += stockBO.getProductNumber();
                totalAmount = totalAmount.add(Optional.ofNullable(stockBO.getAmount()).orElse(BigDecimal.ZERO));
            }
        }

        if (isUpdate) {
            var beforeReceipt = warehouseReceiptSubService.lambdaQuery()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, otherShipmentsDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 1);
            }

            var warehouseReceiptMain = lambdaUpdate()
                    .eq(WarehouseReceiptMain::getId, otherShipmentsDTO.getId())
                    .set(otherShipmentsDTO.getCustomerId() != null, WarehouseReceiptMain::getRelatedPersonId, otherShipmentsDTO.getCustomerId())
                    .set(otherShipmentsDTO.getStatus() != null, WarehouseReceiptMain::getStatus, otherShipmentsDTO.getStatus())
                    .set(WarehouseReceiptMain::getTotalProductNumber, totalProductNumber)
                    .set(WarehouseReceiptMain::getTotalAmount, totalAmount)
                    .set(StringUtils.hasLength(otherShipmentsDTO.getReceiptDate()), WarehouseReceiptMain::getReceiptDate, otherShipmentsDTO.getReceiptDate())
                    .set(StringUtils.hasLength(otherShipmentsDTO.getRemark()), WarehouseReceiptMain::getRemark, otherShipmentsDTO.getRemark())
                    .set(StringUtils.hasLength(fileIds), WarehouseReceiptMain::getFileId, fileIds)
                    .set(WarehouseReceiptMain::getUpdateBy, userId)
                    .set(WarehouseReceiptMain::getUpdateTime, LocalDateTime.now())
                    .update();

            warehouseReceiptSubService.lambdaUpdate()
                    .eq(WarehouseReceiptSub::getWarehouseReceiptMainId, otherShipmentsDTO.getId())
                    .remove();

            var shipmentStockList = otherShipmentsDTO.getTableData();
            var shipmentStock = shipmentStockList.stream()
                    .map(item -> WarehouseReceiptSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .warehouseReceiptMainId(otherShipmentsDTO.getId())
                            .productId(item.getProductId())
                            .warehouseId(item.getWarehouseId())
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

            if (updateSubResult && warehouseReceiptMain) {
                return Response.responseMsg(OtherShipmentCodeEnum.UPDATE_OTHER_SHIPMENT_STOCK_SUCCESS);
            }
            return Response.responseMsg(OtherShipmentCodeEnum.UPDATE_OTHER_SHIPMENT_STOCK_ERROR);

        } else {
            var receiptMainId = SnowflakeIdUtil.nextId();
            var shipmentStockList = otherShipmentsDTO.getTableData();
            var shipmentStock = shipmentStockList.stream()
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
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = warehouseReceiptSubService.saveBatch(shipmentStock);

            var warehouseReceiptMain = WarehouseReceiptMain.builder()
                    .id(receiptMainId)
                    .relatedPersonId(otherShipmentsDTO.getCustomerId())
                    .productId(otherShipmentsDTO.getTableData().get(0).getProductId())
                    .receiptNumber(otherShipmentsDTO.getReceiptNumber())
                    .type("其他出库")
                    .initReceiptNumber(otherShipmentsDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(otherShipmentsDTO.getReceiptDate()))
                    .totalAmount(totalAmount)
                    .totalProductNumber(totalProductNumber)
                    .remark(otherShipmentsDTO.getRemark())
                    .fileId(fileIds)
                    .source(0)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(warehouseReceiptMain);
            updateProductStock(shipmentStock, 2);
            if (saveSubResult && saveMainResult) {
                return Response.responseMsg(OtherShipmentCodeEnum.ADD_OTHER_SHIPMENT_STOCK_SUCCESS);
            }
            return Response.responseMsg(OtherShipmentCodeEnum.ADD_OTHER_SHIPMENT_STOCK_ERROR);
        }
    }

    @Override
    public Response<String> deleteBatchOtherShipments(List<Long> ids) {
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
            return Response.responseMsg(OtherShipmentCodeEnum.DELETE_OTHER_SHIPMENT_STOCK_ERROR);
        }
        return Response.responseMsg(OtherShipmentCodeEnum.DELETE_OTHER_SHIPMENT_STOCK_SUCCESS);
    }

    @Override
    public Response<String> updateOtherShipmentsStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(WarehouseReceiptMain::getStatus, status)
                .in(WarehouseReceiptMain::getId, ids)
                .update();
        if(!updateResult) {
            return Response.responseMsg(OtherShipmentCodeEnum.UPDATE_OTHER_SHIPMENT_STOCK_ERROR);
        }
        return Response.responseMsg(OtherShipmentCodeEnum.UPDATE_OTHER_SHIPMENT_STOCK_SUCCESS);
    }

    @Override
    public void exportOtherShipments(QueryOtherShipmentDTO queryOtherShipmentDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var mainData = getOtherShipmentsList(queryOtherShipmentDTO);
        if (!mainData.isEmpty()) {
            exportMap.put("其他出库", ExcelUtils.getSheetData(mainData));
            if (queryOtherShipmentDTO.getIsExportDetail()) {
                var subData = new ArrayList<StorageShipmentStockExportBO>();
                for (OtherShipmentVO otherShipmentVO : mainData) {
                    var detail = getOtherShipmentsDetail(otherShipmentVO.getId()).getData().getTableData();
                    if(!detail.isEmpty()) {
                        detail.forEach(item -> {
                            var storageShipmentStockBO = StorageShipmentStockExportBO.builder()
                                    .receiptNumber(otherShipmentVO.getReceiptNumber())
                                    .relatedPersonType("客户")
                                    .relatedPerson(otherShipmentVO.getCustomerName())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productExtendInfo(item.getProductExtendInfo())
                                    .stock(item.getStock())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(storageShipmentStockBO);
                        });
                    }
                }
                exportMap.put("其他出库明细", ExcelUtils.getSheetData(subData));
            }
            ExcelUtils.exportManySheet(response, "其他出库", exportMap);
        }
    }

    @Override
    public void exportOtherShipmentsDetail(String receiptNumber, HttpServletResponse response) throws Exception {
        var id = lambdaQuery()
                .eq(WarehouseReceiptMain::getReceiptNumber, receiptNumber)
                .eq(WarehouseReceiptMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(WarehouseReceiptMain::getType, "其他出库")
                .one()
                .getId();
        var detail = getOtherShipmentsDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var exportData = new ArrayList<StorageShipmentStockExportBO>();
            tableData.forEach(item -> {
                var storageShipmentStockBO = new StorageShipmentStockExportBO();
                storageShipmentStockBO.setReceiptNumber(data.getReceiptNumber());
                storageShipmentStockBO.setRelatedPersonType("客户");
                storageShipmentStockBO.setRelatedPerson(data.getCustomerName());
                BeanUtils.copyProperties(item, storageShipmentStockBO);
                exportData.add(storageShipmentStockBO);
            });
            var fileName = data.getReceiptNumber() + "-其他出库单明细";
            ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
        }
    }
}
