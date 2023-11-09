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
import com.wansenai.bo.SalesDataBO;
import com.wansenai.dto.receipt.sale.*;
import com.wansenai.entities.receipt.ReceiptSaleMain;
import com.wansenai.entities.receipt.ReceiptSaleSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.product.ProductStockKeepUnitMapper;
import com.wansenai.mappers.receipt.ReceiptSaleMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.CustomerService;
import com.wansenai.service.receipt.ReceiptSaleService;
import com.wansenai.service.receipt.ReceiptSaleSubService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.SaleCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.sale.SaleOrderDetailVO;
import com.wansenai.vo.receipt.sale.SaleOrderVO;
import com.wansenai.vo.receipt.sale.SaleRefundVO;
import com.wansenai.vo.receipt.sale.SaleShipmentsVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptSaleServiceImpl extends ServiceImpl<ReceiptSaleMainMapper, ReceiptSaleMain> implements ReceiptSaleService {
    
    private final ReceiptSaleMainMapper receiptSaleMainMapper;
    private final CustomerService customerService;
    private final ISysUserService userService;
    private final ReceiptSaleSubService receiptSubService;
    private final SysFileMapper fileMapper;
    private final ProductStockKeepUnitMapper productStockKeepUnitMapper;

    public ReceiptSaleServiceImpl(ReceiptSaleMainMapper receiptSaleMainMapper, CustomerService customerService, ISysUserService userService, ReceiptSaleSubService receiptSubService, SysFileMapper fileMapper, ProductStockKeepUnitMapper productStockKeepUnitMapper) {
        this.receiptSaleMainMapper = receiptSaleMainMapper;
        this.customerService = customerService;
        this.userService = userService;
        this.receiptSubService = receiptSubService;
        this.fileMapper = fileMapper;
        this.productStockKeepUnitMapper = productStockKeepUnitMapper;
    }

    @Override
    public Response<Page<SaleOrderVO>> getSaleOrderPage(QuerySaleOrderDTO querySaleOrderDTO) {
        var result = new Page<SaleOrderVO>();
        var saleOrderVOList = new ArrayList<SaleOrderVO>();
        var page = new Page<ReceiptSaleMain>(querySaleOrderDTO.getPage(), querySaleOrderDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptSaleMain>()
                .eq(ReceiptSaleMain::getType, ReceiptConstants.RECEIPT_TYPE_ORDER)
                .in(ReceiptSaleMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_ORDER)
                .eq(StringUtils.hasText(querySaleOrderDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, querySaleOrderDTO.getReceiptNumber())
                .like(StringUtils.hasText(querySaleOrderDTO.getRemark()), ReceiptSaleMain::getRemark, querySaleOrderDTO.getRemark())
                .eq(querySaleOrderDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, querySaleOrderDTO.getCustomerId())
                .eq(querySaleOrderDTO.getOperatorId() != null, ReceiptSaleMain::getCreateBy, querySaleOrderDTO.getOperatorId())
                .eq(querySaleOrderDTO.getStatus() != null, ReceiptSaleMain::getStatus, querySaleOrderDTO.getStatus())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(querySaleOrderDTO.getStartDate()), ReceiptSaleMain::getCreateTime, querySaleOrderDTO.getStartDate())
                .le(StringUtils.hasText(querySaleOrderDTO.getEndDate()), ReceiptSaleMain::getCreateTime, querySaleOrderDTO.getEndDate());

        var queryResult = receiptSaleMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String customerName = null;
            if (item.getCustomerId() != null) {
                var customer = customerService.getById(item.getCustomerId());
                if (customer != null) {
                    customerName = customer.getCustomerName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
                var user = userService.getById(item.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptSaleSub::getProductNumber)
                    .sum();

            var totalAmount = receiptSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .map(ReceiptSaleSub::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            var taxRateTotalPrice = receiptSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .map(ReceiptSaleSub::getTaxIncludedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            var saleOrderVO = SaleOrderVO.builder()
                    .id(item.getId())
                    .customerName(customerName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(totalAmount)
                    .taxRateTotalPrice(taxRateTotalPrice)
                    .deposit(item.getDeposit())
                    .status(item.getStatus())
                    .build();
            saleOrderVOList.add(saleOrderVO);
        });
        result.setRecords(saleOrderVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<SaleOrderDetailVO> getSaleOrderDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = getById(id);

        List<FileDataBO> fileList = new ArrayList<>();
        if (StringUtils.hasLength(sale.getFileId())) {
            List<Long> ids = Arrays.stream(sale.getFileId().split(","))
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

        var receiptSubList = receiptSubService.lambdaQuery()
                .eq(ReceiptSaleSub::getReceiptSaleMainId, id)
                .list();

        var tableData = new ArrayList<SalesDataBO>(receiptSubList.size() + 1);
        for (ReceiptSaleSub item : receiptSubList) {
            var saleData = SalesDataBO.builder()
                    .productId(item.getProductId())
                    .barCode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .unitPrice(item.getUnitPrice())
                    .amount(item.getTotalAmount())
                    .taxRate(item.getTaxRate())
                    .taxAmount(item.getTaxAmount())
                    .taxTotalPrice(item.getTaxIncludedAmount())
                    .warehouseId(item.getWarehouseId())
                    .build();

            var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if(data != null) {
                saleData.setProductName(data.getProductName());
                saleData.setProductStandard(data.getProductStandard());
                saleData.setProductUnit(data.getProductUnit());
                saleData.setStock(data.getStock());

            }

            tableData.add(saleData);
        }

        List<Long> operatorIds = new ArrayList<>();
        if (StringUtils.hasLength(sale.getOperatorId())) {
            operatorIds = Arrays.stream(sale.getOperatorId().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        List<Long> multipleAccountIds = new ArrayList<>();
        if (StringUtils.hasLength(sale.getMultipleAccount())) {
            multipleAccountIds = Arrays.stream(sale.getMultipleAccount().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        List<Long> multipleAccountAmounts = new ArrayList<>();
        if (StringUtils.hasLength(sale.getMultipleAccountAmount())) {
            multipleAccountAmounts = Arrays.stream(sale.getMultipleAccountAmount().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        var saleOrderDetailVO = SaleOrderDetailVO.builder()
                .receiptNumber(sale.getReceiptNumber())
                .receiptDate(sale.getReceiptDate())
                .customerId(sale.getCustomerId())
                .accountId(sale.getAccountId())
                .operatorIds(operatorIds)
                .discountRate(sale.getDiscountRate())
                .discountAmount(sale.getDiscountAmount())
                .discountLastAmount(sale.getDiscountLastAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .deposit(sale.getDeposit())
                .remark(sale.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(saleOrderDetailVO);
    }

    @Override
    public Response<String> addOrUpdateSaleOrder(SaleOrderDTO saleOrderDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = saleOrderDTO.getId() != null;

        var operatorIds = new StringBuilder();
        if (!saleOrderDTO.getOperatorIds().isEmpty()) {
            var operatorList = saleOrderDTO.getOperatorIds();
            for (Long aLong : operatorList) {
                operatorIds.append(aLong).append(",");
            }
        }

        var multipleAccountIds = new StringBuilder();
        if (!saleOrderDTO.getMultipleAccountIds().isEmpty()) {
            var multipleAccountList = saleOrderDTO.getMultipleAccountIds();
            for (Long aLong : multipleAccountList) {
                multipleAccountIds.append(aLong).append(",");
            }
        } else {
            multipleAccountIds.append("");
        }

        var multipleAccountAmounts = new StringBuilder();
        if (!saleOrderDTO.getMultipleAccountAmounts().isEmpty()) {
            var multipleAccountList = saleOrderDTO.getMultipleAccountAmounts();
            for (Long amount : multipleAccountList) {
                multipleAccountAmounts.append(amount).append(",");
            }
        } else {
            multipleAccountAmounts.append("");
        }
        String accountId = null;
        if (saleOrderDTO.getAccountId() != null) {
            accountId = String.valueOf(saleOrderDTO.getAccountId());
        }

        var fid = new ArrayList<>();
        if (!saleOrderDTO.getFiles().isEmpty()) {
            var receiptMain = getById(saleOrderDTO.getId());
            if (StringUtils.hasLength(receiptMain.getFileId())) {
                var ids = Arrays.stream(receiptMain.getFileId().split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                fileMapper.deleteBatchIds(ids);
            }
            saleOrderDTO.getFiles().forEach(item -> {
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
        var fileIds = fid.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        if (isUpdate) {
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptSaleMain::getId, saleOrderDTO.getId())
                    .set(saleOrderDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, saleOrderDTO.getCustomerId())
                    .set(saleOrderDTO.getDiscountRate() != null, ReceiptSaleMain::getDiscountRate, saleOrderDTO.getDiscountRate())
                    .set(saleOrderDTO.getDiscountAmount() != null, ReceiptSaleMain::getDiscountAmount, saleOrderDTO.getDiscountAmount())
                    .set(saleOrderDTO.getDiscountLastAmount() != null, ReceiptSaleMain::getDiscountLastAmount, saleOrderDTO.getDiscountLastAmount())
                    .set(saleOrderDTO.getDeposit() != null, ReceiptSaleMain::getDeposit, saleOrderDTO.getDeposit())
                    .set(saleOrderDTO.getStatus() != null, ReceiptSaleMain::getStatus, saleOrderDTO.getStatus())
                    .set(StringUtils.hasText(saleOrderDTO.getReceiptDate()), ReceiptSaleMain::getReceiptDate, saleOrderDTO.getReceiptDate())
                    .set(StringUtils.hasText(saleOrderDTO.getRemark()), ReceiptSaleMain::getRemark, saleOrderDTO.getRemark())
                    .set(ReceiptSaleMain::getAccountId, accountId)
                    .set(ReceiptSaleMain::getFileId, fileIds)
                    .set(ReceiptSaleMain::getMultipleAccount, String.valueOf(multipleAccountIds))
                    .set(ReceiptSaleMain::getMultipleAccountAmount, String.valueOf(multipleAccountAmounts))
                    .set(!operatorIds.isEmpty(), ReceiptSaleMain::getOperatorId, String.valueOf(operatorIds))
                    .set(ReceiptSaleMain::getUpdateBy, userId)
                    .set(ReceiptSaleMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSubService.lambdaUpdate()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, saleOrderDTO.getId())
                    .remove();

            var receiptSubList = saleOrderDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSaleSub.builder()
                            .receiptSaleMainId(saleOrderDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxIncludedAmount(item.getTaxTotalPrice())
                            .updateBy(userId)
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptSubService.saveBatch(receiptList);

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var receiptMain = ReceiptSaleMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_ORDER)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_SALES_ORDER)
                    .initReceiptNumber(saleOrderDTO.getReceiptNumber())
                    .receiptNumber(saleOrderDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(saleOrderDTO.getReceiptDate()))
                    .customerId(saleOrderDTO.getCustomerId())
                    .operatorId(String.valueOf(operatorIds))
                    .discountRate(saleOrderDTO.getDiscountRate())
                    .accountId(saleOrderDTO.getAccountId())
                    .discountAmount(saleOrderDTO.getDiscountAmount())
                    .discountLastAmount(saleOrderDTO.getDiscountLastAmount())
                    .deposit(saleOrderDTO.getDeposit())
                    .remark(saleOrderDTO.getRemark())
                    .fileId(fileIds)
                    .status(saleOrderDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            if(StringUtils.hasLength(multipleAccountIds)) {
                receiptMain.setMultipleAccount(String.valueOf(multipleAccountIds));
            }
            if(StringUtils.hasLength(multipleAccountAmounts)) {
                receiptMain.setMultipleAccountAmount(String.valueOf(multipleAccountAmounts));
            }
            var saveMainResult = save(receiptMain);

            var receiptSubList = saleOrderDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptSaleSub.builder()
                            .receiptSaleMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxIncludedAmount(item.getTaxTotalPrice())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_ORDER_ERROR);
            }
        }
    }

    @Override
    public Response<String> deleteSaleOrder(List<Long> ids) {
        if(ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptSaleMain::getId, ids)
                .set(ReceiptSaleMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        var updateSubResult = receiptSubService.lambdaUpdate()
                .in(ReceiptSaleSub::getReceiptSaleMainId, ids)
                .set(ReceiptSaleSub::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateResult && updateSubResult) {
            return Response.responseMsg(SaleCodeEnum.DELETE_SALE_ORDER_SUCCESS);
        } else {
            return Response.responseMsg(SaleCodeEnum.DELETE_SALE_ORDER_ERROR);
        }

    }

    @Override
    public Response<String> updateSaleOrderStatus(List<Long> ids, Integer status) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptSaleMain::getId, ids)
                .set(ReceiptSaleMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_SUCCESS);
        } else {
            return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_ORDER_ERROR);
        }
    }

    @Override
    public Response<Page<SaleShipmentsVO>> getSaleShipmentsPage(QuerySaleShipmentsDTO shipmentsDTO) {
        var result = new Page<SaleShipmentsVO>();
        var saleShipmentsVOList = new ArrayList<SaleShipmentsVO>();
        var page = new Page<ReceiptSaleMain>(shipmentsDTO.getPage(), shipmentsDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptSaleMain>()
                .eq(ReceiptSaleMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptSaleMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_SHIPMENTS)
                .eq(StringUtils.hasText(shipmentsDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, shipmentsDTO.getReceiptNumber())
                .like(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptSaleMain::getRemark, shipmentsDTO.getRemark())
                .eq(shipmentsDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, shipmentsDTO.getCustomerId())
                .eq(shipmentsDTO.getOperatorId() != null, ReceiptSaleMain::getCreateBy, shipmentsDTO.getOperatorId())
                .eq(shipmentsDTO.getStatus() != null, ReceiptSaleMain::getStatus, shipmentsDTO.getStatus())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(shipmentsDTO.getStartDate()), ReceiptSaleMain::getCreateTime, shipmentsDTO.getStartDate())
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptSaleMain::getCreateTime, shipmentsDTO.getEndDate());

        var queryResult = receiptSaleMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            String customerName = null;
            if (item.getCustomerId() != null) {
                var customer = customerService.getById(item.getCustomerId());
                if (customer != null) {
                    customerName = customer.getCustomerName();
                }
            }
            String crateBy = null;
            if (item.getCreateBy() != null) {
                var user = userService.getById(item.getCreateBy());
                if (user != null) {
                    crateBy = user.getName();
                }
            }
            var productNumber = receiptSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptSaleSub::getProductNumber)
                    .sum();

            var taxAmount = receiptSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .map(ReceiptSaleSub::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            var taxRateTotalPrice = receiptSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .map(ReceiptSaleSub::getTaxIncludedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            // 计算代收金额 = 本次欠款 + 本次收款

            var saleShipmentVO = SaleShipmentsVO.builder()
                    .id(item.getId())
                    .customerName(customerName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(taxAmount)
                    .taxIncludedAmount(taxRateTotalPrice)
                    //  .shipmentsTotalPrice()
                    .thisCollectAmount(item.getChangeAmount())
                    //  .thisArrearsAmount()
                    .status(item.getStatus())
                    .build();
            saleShipmentsVOList.add(saleShipmentVO);
        });
        result.setRecords(saleShipmentsVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }
    
    @Override
    public Response<SaleOrderDetailVO> getSaleShipmentsDetail(Long id) {
        return null;
    }

    @Override
    public Response<String> addOrUpdateSaleShipments(SaleShipmentsDTO shipmentsDTO) {
        return null;
    }

    @Override
    public Response<String> updateSaleShipmentsStatus(List<Long> ids, Integer status) {
        return null;
    }

    @Override
    public Response<String> deleteSaleShipments(List<Long> ids) {
        return null;
    }

    @Override
    public Response<Page<SaleRefundVO>> getSaleRefundPage(QuerySaleRefundDTO refundDTO) {
        return null;
    }

    @Override
    public Response<SaleOrderDetailVO> getSaleRefundDetail(Long id) {
        return null;
    }

    @Override
    public Response<String> addOrUpdateSaleRefund(SaleRefundDTO refundDTO) {
        return null;
    }

    @Override
    public Response<String> deleteSaleRefund(List<Long> ids) {
        return null;
    }

    @Override
    public Response<String> updateSaleRefundStatus(List<Long> ids, Integer status) {
        return null;
    }
}
