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
package com.wansenai.service.receipt.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.ShipmentsDataBO;
import com.wansenai.dto.receipt.retail.QueryRetailRefundDTO;
import com.wansenai.dto.receipt.retail.QueryShipmentsDTO;
import com.wansenai.dto.receipt.retail.RetailRefundDTO;
import com.wansenai.dto.receipt.retail.RetailShipmentsDTO;
import com.wansenai.entities.product.ProductStockKeepUnit;
import com.wansenai.entities.receipt.ReceiptRetailMain;
import com.wansenai.entities.receipt.ReceiptRetailSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.product.ProductStockKeepUnitMapper;
import com.wansenai.mappers.receipt.ReceiptRetailMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.MemberService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.receipt.ReceiptRetailService;
import com.wansenai.service.receipt.ReceiptRetailSubService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.RetailCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.retail.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReceiptRetailServiceImpl extends ServiceImpl<ReceiptRetailMainMapper, ReceiptRetailMain> implements ReceiptRetailService {

    private final ReceiptRetailMainMapper receiptRetailMainMapper;

    private final ReceiptRetailSubService receiptRetailSubService;

    private final MemberService memberService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    private final ProductStockKeepUnitMapper productStockKeepUnitMapper;

    private final ProductService productService;

    public ReceiptRetailServiceImpl(ReceiptRetailMainMapper receiptRetailMainMapper, ReceiptRetailSubService receiptRetailSubService, MemberService memberService, ISysUserService userService, SysFileMapper fileMapper, ProductStockKeepUnitMapper productStockKeepUnitMapper, ProductService productService) {
        this.receiptRetailMainMapper = receiptRetailMainMapper;
        this.receiptRetailSubService = receiptRetailSubService;
        this.memberService = memberService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.productStockKeepUnitMapper = productStockKeepUnitMapper;
        this.productService = productService;
    }

    @Override
    public Response<Page<RetailShipmentsVO>> getRetailShipmentsPage(QueryShipmentsDTO shipmentsDTO) {
        var result = new Page<RetailShipmentsVO>();
        var retailShipmentsVOList = new ArrayList<RetailShipmentsVO>();
        var page = new Page<ReceiptRetailMain>(shipmentsDTO.getPage(), shipmentsDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptRetailMain>()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                .eq(StringUtils.hasText(shipmentsDTO.getReceiptNumber()), ReceiptRetailMain::getReceiptNumber, shipmentsDTO.getReceiptNumber())
                .like(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptRetailMain::getRemark, shipmentsDTO.getRemark())
                .eq(shipmentsDTO.getMemberId() != null, ReceiptRetailMain::getMemberId, shipmentsDTO.getMemberId())
                .eq(shipmentsDTO.getAccountId() != null, ReceiptRetailMain::getAccountId, shipmentsDTO.getAccountId())
                .eq(shipmentsDTO.getOperatorId() != null, ReceiptRetailMain::getCreateBy, shipmentsDTO.getOperatorId())
                .eq(shipmentsDTO.getStatus() != null, ReceiptRetailMain::getStatus, shipmentsDTO.getStatus())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(shipmentsDTO.getStartDate()), ReceiptRetailMain::getCreateTime, shipmentsDTO.getStartDate())
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptRetailMain::getCreateTime, shipmentsDTO.getEndDate());

        var queryResult = receiptRetailMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String memberName = null;
            if (item.getMemberId() != null) {
                var member = memberService.getMemberById(item.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
                var user = userService.getById(item.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptRetailSub::getProductNumber)
                    .sum();
            var retailShipmentsVO = RetailShipmentsVO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalAmount())
                    .collectionAmount(item.getTotalAmount())
                    .backAmount(item.getBackAmount())
                    .status(item.getStatus())
                    .build();
            retailShipmentsVOList.add(retailShipmentsVO);
        });
        result.setRecords(retailShipmentsVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<List<RetailShipmentsVO>> getRetailShipmentsList(QueryShipmentsDTO shipmentsDTO) {
        var query = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                .eq(StringUtils.hasText(shipmentsDTO.getReceiptNumber()), ReceiptRetailMain::getReceiptNumber, shipmentsDTO.getReceiptNumber())
                .like(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptRetailMain::getRemark, shipmentsDTO.getRemark())
                .eq(shipmentsDTO.getMemberId() != null, ReceiptRetailMain::getMemberId, shipmentsDTO.getMemberId())
                .eq(shipmentsDTO.getAccountId() != null, ReceiptRetailMain::getAccountId, shipmentsDTO.getAccountId())
                .eq(shipmentsDTO.getOperatorId() != null, ReceiptRetailMain::getCreateBy, shipmentsDTO.getOperatorId())
                .eq(shipmentsDTO.getStatus() != null, ReceiptRetailMain::getStatus, shipmentsDTO.getStatus())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(shipmentsDTO.getStartDate()), ReceiptRetailMain::getReceiptDate, shipmentsDTO.getStartDate())
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptRetailMain::getReceiptDate, shipmentsDTO.getEndDate())
                .list();

        var result = new ArrayList<RetailShipmentsVO>(query.size() + 2);
        for (ReceiptRetailMain receiptRetailMain : query) {

            String memberName = "";
            if (receiptRetailMain.getMemberId() != null) {
                var member = memberService.getMemberById(receiptRetailMain.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if (receiptRetailMain.getCreateBy() != null) {
                var user = userService.getById(receiptRetailMain.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, receiptRetailMain.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptRetailSub::getProductNumber)
                    .sum();
            var retailShipmentsVO = RetailShipmentsVO.builder()
                    .id(receiptRetailMain.getId())
                    .memberName(memberName)
                    .receiptNumber(receiptRetailMain.getReceiptNumber())
                    .receiptDate(receiptRetailMain.getReceiptDate())
                    .productInfo(receiptRetailMain.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(receiptRetailMain.getTotalAmount())
                    .collectionAmount(receiptRetailMain.getTotalAmount())
                    .backAmount(receiptRetailMain.getBackAmount())
                    .status(receiptRetailMain.getStatus())
                    .build();
            result.add(retailShipmentsVO);
        }
        return Response.responseData(result);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateRetailShipments(RetailShipmentsDTO shipmentsDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = shipmentsDTO.getId() != null;

        if (isUpdate) {
            receiptRetailSubService.lambdaUpdate()
                    .eq(ReceiptRetailSub::getReceiptMainId, shipmentsDTO.getId())
                    .remove();

            var receiptSubList = shipmentsDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptRetailSub.builder()
                            .receiptMainId(shipmentsDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .updateBy(userId)
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptRetailSubService.saveBatch(receiptList);

            var fid = new ArrayList<>();
            if (!shipmentsDTO.getFiles().isEmpty()) {
                var receiptMain = getById(shipmentsDTO.getId());
                if (receiptMain != null && StringUtils.hasLength(receiptMain.getFileId())) {
                    var ids = Arrays.stream(receiptMain.getFileId().split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());


                    fileMapper.deleteBatchIds(ids);
                }
                shipmentsDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptRetailMain::getId, shipmentsDTO.getId())
                    .set(shipmentsDTO.getMemberId() != null, ReceiptRetailMain::getMemberId, shipmentsDTO.getMemberId())
                    .set(shipmentsDTO.getAccountId() != null, ReceiptRetailMain::getAccountId, shipmentsDTO.getAccountId())
                    .set(shipmentsDTO.getCollectAmount() != null, ReceiptRetailMain::getChangeAmount, shipmentsDTO.getCollectAmount())
                    .set(shipmentsDTO.getReceiptAmount() != null, ReceiptRetailMain::getTotalAmount, shipmentsDTO.getReceiptAmount())
                    .set(shipmentsDTO.getBackAmount() != null, ReceiptRetailMain::getBackAmount, shipmentsDTO.getBackAmount())
                    .set(shipmentsDTO.getStatus() != null, ReceiptRetailMain::getStatus, shipmentsDTO.getStatus())
                    .set(StringUtils.hasText(shipmentsDTO.getPaymentType()), ReceiptRetailMain::getPaymentType, shipmentsDTO.getPaymentType())
                    .set(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptRetailMain::getRemark, shipmentsDTO.getRemark())
                    .set(StringUtils.hasText(shipmentsDTO.getReceiptDate()), ReceiptRetailMain::getReceiptDate, shipmentsDTO.getReceiptDate())
                    .set(StringUtils.hasLength(fileIds), ReceiptRetailMain::getFileId, fileIds)
                    .set(ReceiptRetailMain::getUpdateBy, userId)
                    .set(ReceiptRetailMain::getUpdateTime, LocalDateTime.now())
                    .update();

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var fid = new ArrayList<>();
            if (!shipmentsDTO.getFiles().isEmpty()) {
                shipmentsDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .build();
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var receiptMain = ReceiptRetailMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                    .initReceiptNumber(shipmentsDTO.getReceiptNumber())
                    .receiptNumber(shipmentsDTO.getReceiptNumber())
                    .receiptDate(LocalDateTime.now())
                    .memberId(shipmentsDTO.getMemberId())
                    .accountId(shipmentsDTO.getAccountId())
                    .paymentType(shipmentsDTO.getPaymentType())
                    .accountId(shipmentsDTO.getAccountId())
                    .changeAmount(shipmentsDTO.getCollectAmount())
                    .totalAmount(shipmentsDTO.getReceiptAmount())
                    .backAmount(shipmentsDTO.getBackAmount())
                    .remark(shipmentsDTO.getRemark())
                    .fileId(fileIds)
                    .status(shipmentsDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();

            var saveMainResult = save(receiptMain);

            var receiptSubList = shipmentsDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptRetailSub.builder()
                            .receiptMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptRetailSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_ERROR);
            }
        }
    }

    @Override
    public Response<String> deleteRetailShipments(List<Long> ids) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptRetailMain::getId, ids)
                .set(ReceiptRetailMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        receiptRetailSubService.lambdaUpdate()
                .in(ReceiptRetailSub::getReceiptMainId, ids)
                .set(ReceiptRetailSub::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_ERROR);
        }
    }

    @Override
    public Response<RetailShipmentsDetailVO> getRetailShipmentsDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = getById(id);

        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(shipment.getFileId())) {
            List<Long> ids = Arrays.stream(shipment.getFileId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            fileList.addAll(fileMapper.selectBatchIds(ids)
                    .stream()
                    .map(item ->
                            FileDataBO.builder(
                                    item.getFileName(),
                                    item.getFileUrl(),
                                    item.getId(),
                                    item.getUid(),
                                    item.getFileType(),
                                    item.getFileSize()
                            ))
                    .toList());
        }

        var receiptSubList = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getReceiptMainId, id)
                .list();

        var tableData = new ArrayList<ShipmentsDataBO>(receiptSubList.size() + 1);
        for (ReceiptRetailSub item : receiptSubList) {
            var shipmentBo = ShipmentsDataBO.builder()
                    .productId(item.getProductId())
                    .barCode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .unitPrice(item.getUnitPrice())
                    .amount(item.getTotalAmount())
                    .warehouseId(item.getWarehouseId())
                    .build();

            var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if(data != null) {
                shipmentBo.setProductName(data.getProductName());
                shipmentBo.setProductStandard(data.getProductStandard());
                shipmentBo.setProductUnit(data.getProductUnit());
                shipmentBo.setStock(data.getStock());

            }

            tableData.add(shipmentBo);
        }

        var retailShipmentsDetailVO = RetailShipmentsDetailVO.builder()
                .receiptNumber(shipment.getReceiptNumber())
                .receiptDate(shipment.getReceiptDate())
                .memberId(shipment.getMemberId())
                .accountId(shipment.getAccountId())
                .paymentType(shipment.getPaymentType())
                .collectAmount(shipment.getChangeAmount())
                .receiptAmount(shipment.getTotalAmount())
                .backAmount(shipment.getBackAmount())
                .remark(shipment.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(retailShipmentsDetailVO);
    }

    @Override
    public Response<String> updateRetailShipmentsStatus(List<Long> ids, Integer status) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptRetailMain::getId, ids)
                .set(ReceiptRetailMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
        }
    }

    @Override
    public Response<RetailStatisticalDataVO> getRetailStatistics() {
        var now = LocalDateTime.now();

        var retailData = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                // .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();
        var retailRefundData = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
               // .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();

        var salesData = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_SHIPMENTS)
            //    .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();
        var salesRefundData = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_REFUND)
           //     .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();

        var purchaseData = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_STORAGE)
             //   .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();
        var purchaseRefundData = lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .eq(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_REFUND)
             //   .eq(ReceiptMain::getStatus, 1)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();

        var todayRetailSales = calculateTotalPrice(retailData, retailRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdayRetailSales = calculateTotalPrice(retailData, retailRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthRetailSales = calculateTotalPrice(retailData, retailRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearRetailSales = calculateTotalPrice(retailData, retailRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var todaySales = calculateTotalPrice(salesData, salesRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdaySales = calculateTotalPrice(salesData, salesRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthSales = calculateTotalPrice(salesData, salesRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearSales = calculateTotalPrice(salesData, salesRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var todayPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdayPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearPurchase = calculateTotalPrice(purchaseData, purchaseRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var retailStatisticalDataVO = RetailStatisticalDataVO.builder()
                .todayRetailSales(todayRetailSales)
                .yesterdayRetailSales(yesterdayRetailSales)
                .monthRetailSales(monthRetailSales)
                .yearRetailSales(yearRetailSales)
                .todaySales(todaySales)
                .yesterdaySales(yesterdaySales)
                .monthSales(monthSales)
                .yearSales(yearSales)
                .todayPurchase(todayPurchase)
                .yesterdayPurchase(yesterdayPurchase)
                .monthPurchase(monthPurchase)
                .yearPurchase(yearPurchase)
                .build();

        return Response.responseData(retailStatisticalDataVO);
    }

    private BigDecimal calculateTotalPrice(List<ReceiptRetailMain> data, List<ReceiptRetailMain> backData, LocalDateTime start, LocalDateTime end) {
        var dataTotalPrice = data.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptRetailMain::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        var backDataTotalPrice = backData.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptRetailMain::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return dataTotalPrice.add(backDataTotalPrice);
    }

    @Override
    public Response<Page<RetailRefundVO>> getRetailRefund(QueryRetailRefundDTO refundDTO) {
        var result = new Page<RetailRefundVO>();
        var retailRefundVOList = new ArrayList<RetailRefundVO>();
        var page = new Page<ReceiptRetailMain>(refundDTO.getPage(), refundDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptRetailMain>()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
                .eq(StringUtils.hasText(refundDTO.getReceiptNumber()), ReceiptRetailMain::getReceiptNumber, refundDTO.getReceiptNumber())
                .like(StringUtils.hasText(refundDTO.getRemark()), ReceiptRetailMain::getRemark, refundDTO.getRemark())
                .eq(refundDTO.getMemberId() != null, ReceiptRetailMain::getMemberId, refundDTO.getMemberId())
                .eq(refundDTO.getAccountId() != null, ReceiptRetailMain::getAccountId, refundDTO.getAccountId())
                .eq(refundDTO.getOperatorId() != null, ReceiptRetailMain::getCreateBy, refundDTO.getOperatorId())
                .eq(refundDTO.getStatus() != null, ReceiptRetailMain::getStatus, refundDTO.getStatus())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(refundDTO.getStartDate()), ReceiptRetailMain::getCreateTime, refundDTO.getStartDate())
                .le(StringUtils.hasText(refundDTO.getEndDate()), ReceiptRetailMain::getCreateTime, refundDTO.getEndDate());

        var queryResult = receiptRetailMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String memberName = null;
            if (item.getMemberId() != null) {
                var member = memberService.getMemberById(item.getMemberId());
                if (member != null) {
                    memberName = member.getMemberName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
                var user = userService.getById(item.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptRetailSub::getProductNumber)
                    .sum();
            var retailRefundVO = RetailRefundVO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalAmount())
                    .paymentAmount(item.getTotalAmount())
                    .backAmount(item.getBackAmount())
                    .status(item.getStatus())
                    .build();
            retailRefundVOList.add(retailRefundVO);
        });
        result.setRecords(retailRefundVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<String> addOrUpdateRetailRefund(RetailRefundDTO refundDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = refundDTO.getId() != null;

        if (isUpdate) {
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptRetailMain::getId, refundDTO.getId())
                    .set(refundDTO.getMemberId() != null, ReceiptRetailMain::getMemberId, refundDTO.getMemberId())
                    .set(refundDTO.getAccountId() != null, ReceiptRetailMain::getAccountId, refundDTO.getAccountId())
                    .set(refundDTO.getPaymentAmount() != null, ReceiptRetailMain::getChangeAmount, refundDTO.getPaymentAmount().negate())
                    .set(refundDTO.getReceiptAmount() != null, ReceiptRetailMain::getTotalAmount, refundDTO.getReceiptAmount().negate())
                    .set(refundDTO.getBackAmount() != null, ReceiptRetailMain::getBackAmount, refundDTO.getBackAmount().negate())
                    .set(refundDTO.getStatus() != null, ReceiptRetailMain::getStatus, refundDTO.getStatus())
                    .set(StringUtils.hasText(refundDTO.getOtherReceipt()), ReceiptRetailMain::getOtherReceipt, refundDTO.getOtherReceipt())
                    .set(StringUtils.hasText(refundDTO.getRemark()), ReceiptRetailMain::getRemark, refundDTO.getRemark())
                    .set(StringUtils.hasText(refundDTO.getReceiptDate()), ReceiptRetailMain::getReceiptDate, refundDTO.getReceiptDate())
                    .set(ReceiptRetailMain::getUpdateBy, userId)
                    .set(ReceiptRetailMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptRetailSubService.lambdaUpdate()
                    .eq(ReceiptRetailSub::getReceiptMainId, refundDTO.getId())
                    .remove();

            var receiptSubList = refundDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptRetailSub.builder()
                            .receiptMainId(refundDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .updateBy(userId)
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptRetailSubService.saveBatch(receiptList);

            if (!refundDTO.getFiles().isEmpty()) {
                var receiptMain = getById(refundDTO.getId());
                if (receiptMain != null && StringUtils.hasLength(receiptMain.getFileId())) {
                    var ids = Arrays.stream(receiptMain.getFileId().split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    fileMapper.deleteBatchIds(ids);
                }
                refundDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .updateBy(userId)
                            .updateTime(LocalDateTime.now())
                            .build();
                    fileMapper.insert(file);
                });
            }

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var fid = new ArrayList<>();
            if (!refundDTO.getFiles().isEmpty()) {
                refundDTO.getFiles().forEach(item -> {
                    var file = SysFile.builder()
                            .id(item.getId())
                            .uid(item.getUid())
                            .fileName(item.getFileName())
                            .fileType(item.getFileType())
                            .fileSize(item.getFileSize())
                            .fileUrl(item.getFileUrl())
                            .createBy(userId)
                            .updateTime(LocalDateTime.now())
                            .build();
                    var result = fileMapper.insert(file);
                    if (result > 0) {
                        fid.add(file.getId());
                    }
                });
            }
            var fileIds = fid.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var receiptMain = ReceiptRetailMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_STORAGE)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
                    .initReceiptNumber(refundDTO.getReceiptNumber())
                    .receiptNumber(refundDTO.getReceiptNumber())
                    .receiptDate(LocalDateTime.now())
                    .memberId(refundDTO.getMemberId())
                    .accountId(refundDTO.getAccountId())
                    .otherReceipt(refundDTO.getOtherReceipt())
                    .accountId(refundDTO.getAccountId())
                    .changeAmount(refundDTO.getPaymentAmount().negate())
                    .totalAmount(refundDTO.getReceiptAmount().negate())
                    .backAmount(refundDTO.getBackAmount().negate())
                    .remark(refundDTO.getRemark())
                    .status(refundDTO.getStatus())
                    .fileId(fileIds)
                    .status(refundDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();

            var saveMainResult = save(receiptMain);

            var receiptSubList = refundDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptRetailSub.builder()
                            .receiptMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptRetailSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_ERROR);
            }
        }
    }

    @Override
    public Response<List<ReceiptRetailDetailVO>> retailDetail(Long id) {
        if(id == null) {
            Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var receiptSubList = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getReceiptMainId, id)
                .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        var result = new ArrayList<ReceiptRetailDetailVO>(receiptSubList.size() + 1);

        for (ReceiptRetailSub receiptRetailSub : receiptSubList) {
            var receiptDetail = ReceiptRetailDetailVO.builder()
                    .id(receiptRetailSub.getId())
                    .warehouseId(receiptRetailSub.getWarehouseId())
                    .productId(receiptRetailSub.getProductId())
                    .productBarcode(receiptRetailSub.getProductBarcode())
                    .productNumber(receiptRetailSub.getProductNumber())
                    .productPrice(receiptRetailSub.getUnitPrice())
                    .productTotalPrice(receiptRetailSub.getTotalAmount())
                    .remark(receiptRetailSub.getRemark())
                    .build();

            var product = productService.getById(receiptRetailSub.getProductId());
            if (product != null) {
                receiptDetail.setProductName(product.getProductName());
                receiptDetail.setProductStandard(product.getProductStandard());
                receiptDetail.setProductModel(product.getProductModel());

                var productSku = productStockKeepUnitMapper.selectOne(new LambdaQueryWrapper<ProductStockKeepUnit>()
                        .eq(ProductStockKeepUnit::getProductBarCode, receiptRetailSub.getProductBarcode())
                        .eq(ProductStockKeepUnit::getProductId, product.getId()));
                receiptDetail.setUnit(productSku.getProductUnit());
            }
            result.add(receiptDetail);
        }

        return Response.responseData(result);
    }

    @Override
    public Response<RetailRefundDetailVO> getRetailRefundDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = getById(id);

        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(shipment.getFileId())) {
            List<Long> ids = Arrays.stream(shipment.getFileId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            fileList.addAll(fileMapper.selectBatchIds(ids)
                    .stream()
                    .map(item ->
                            FileDataBO.builder(
                                    item.getFileName(),
                                    item.getFileUrl(),
                                    item.getId(),
                                    item.getUid(),
                                    item.getFileType(),
                                    item.getFileSize()
                            ))
                    .toList());
        }

        var receiptSubList = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getReceiptMainId, id)
                .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var tableData = new ArrayList<ShipmentsDataBO>(receiptSubList.size() + 1);
        for (ReceiptRetailSub item : receiptSubList) {
            var shipmentBo = ShipmentsDataBO.builder()
                    .productId(item.getProductId())
                    .barCode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .unitPrice(item.getUnitPrice())
                    .amount(item.getTotalAmount())
                    .warehouseId(item.getWarehouseId())
                    .build();

            var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if(data != null) {
                shipmentBo.setProductName(data.getProductName());
                shipmentBo.setProductStandard(data.getProductStandard());
                shipmentBo.setProductUnit(data.getProductUnit());
                shipmentBo.setStock(data.getStock());

            }

            tableData.add(shipmentBo);
        }

        var retailRefundDetailVO = RetailRefundDetailVO.builder()
                .receiptNumber(shipment.getReceiptNumber())
                .receiptDate(shipment.getReceiptDate())
                .memberId(shipment.getMemberId())
                .accountId(shipment.getAccountId())
                .otherReceipt(shipment.getOtherReceipt())
                .paymentAmount(shipment.getChangeAmount())
                .receiptAmount(shipment.getTotalAmount())
                .backAmount(shipment.getBackAmount())
                .remark(shipment.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(retailRefundDetailVO);
    }

    @Override
    public Response<String> deleteRetailRefund(List<Long> ids) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptRetailMain::getId, ids)
                .set(ReceiptRetailMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_REFUND_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.DELETE_RETAIL_REFUND_ERROR);
        }
    }

    @Override
    public Response<String> updateRetailRefundStatus(List<Long> ids, Integer status) {
       if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptRetailMain::getId, ids)
                .set(ReceiptRetailMain::getStatus, status)
                .set(ReceiptRetailMain::getUpdateTime, LocalDateTime.now())
                .set(ReceiptRetailMain::getUpdateBy, userService.getCurrentUserId())
                .update();
        if (updateResult) {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS);
        } else {
            return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR);
        }
    }

}
