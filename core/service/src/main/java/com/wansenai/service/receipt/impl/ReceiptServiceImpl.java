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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.bo.XyAxisDataBO;
import com.wansenai.dto.receipt.QueryReceiptDTO;
import com.wansenai.dto.report.*;
import com.wansenai.entities.basic.Customer;
import com.wansenai.entities.basic.Member;
import com.wansenai.entities.basic.Supplier;
import com.wansenai.entities.financial.FinancialAccount;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.entities.product.Product;
import com.wansenai.entities.receipt.*;
import com.wansenai.mappers.financial.FinancialMainMapper;
import com.wansenai.mappers.financial.FinancialSubMapper;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.service.basic.CustomerService;
import com.wansenai.service.basic.MemberService;
import com.wansenai.service.basic.SupplierService;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.receipt.*;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.ReceiptDetailVO;
import com.wansenai.vo.receipt.ReceiptVO;
import com.wansenai.vo.receipt.retail.StatisticalDataVO;
import com.wansenai.vo.report.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRetailService receiptRetailService;

    private final ReceiptRetailSubService receiptRetailSubService;

    private final ReceiptSaleService receiptSaleService;

    private final ReceiptSaleSubService receiptSaleSubService;

    private final ReceiptPurchaseService receiptPurchaseService;

    private final ReceiptPurchaseSubService receiptPurchaseSubService;

    private final MemberService memberService;

    private final CustomerService customerService;

    private final SupplierService supplierService;

    private final ISysUserService userService;

    private final ProductService productService;

    private final ProductStockMapper productStockMapper;

    private final IFinancialAccountService accountService;

    private final CommonService commonService;

    private final FinancialMainMapper financialMainMapper;

    private final FinancialSubMapper financialSubMapper;

    public ReceiptServiceImpl(ReceiptRetailService receiptRetailService, ReceiptRetailSubService receiptRetailSubService, ReceiptSaleService receiptSaleService, ReceiptSaleSubService receiptSaleSubService, ReceiptPurchaseService receiptPurchaseService, ReceiptPurchaseSubService receiptPurchaseSubService, MemberService memberService, CustomerService customerService, SupplierService supplierService, ISysUserService userService, ProductService productService, ProductStockMapper productStockMapper, IFinancialAccountService accountService, CommonService commonService, FinancialMainMapper financialMainMapper, FinancialSubMapper financialSubMapper) {
        this.receiptRetailService = receiptRetailService;
        this.receiptRetailSubService = receiptRetailSubService;
        this.receiptSaleService = receiptSaleService;
        this.receiptSaleSubService = receiptSaleSubService;
        this.receiptPurchaseService = receiptPurchaseService;
        this.receiptPurchaseSubService = receiptPurchaseSubService;
        this.memberService = memberService;
        this.customerService = customerService;
        this.supplierService = supplierService;
        this.userService = userService;
        this.productService = productService;
        this.productStockMapper = productStockMapper;
        this.accountService = accountService;
        this.commonService = commonService;
        this.financialMainMapper = financialMainMapper;
        this.financialSubMapper = financialSubMapper;
    }

    @Override
    public Response<StatisticalDataVO> getStatisticalData() {
        var now = LocalDateTime.now();

        var retailData = receiptRetailService.lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_SHIPMENTS)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();
        var retailRefundData = receiptRetailService.lambdaQuery()
                .eq(ReceiptRetailMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptRetailMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_RETAIL_REFUND)
                .eq(ReceiptRetailMain::getDeleteFlag, 0)
                .list();

        var salesData = receiptSaleService.lambdaQuery()
                .eq(ReceiptSaleMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .in(ReceiptSaleMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_SHIPMENTS)
                .eq(ReceiptSaleMain::getDeleteFlag, 0)
                .list();
        var salesRefundData = receiptSaleService.lambdaQuery()
                .eq(ReceiptSaleMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptSaleMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_REFUND)
                .eq(ReceiptSaleMain::getDeleteFlag, 0)
                .list();

        var purchaseData = receiptPurchaseService.lambdaQuery()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .eq(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_STORAGE)
                .eq(ReceiptPurchaseMain::getDeleteFlag, 0)
                .list();
        var purchaseRefundData = receiptPurchaseService.lambdaQuery()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                .eq(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_REFUND)
                .eq(ReceiptPurchaseMain::getDeleteFlag, 0)
                .list();

        var todayRetailSales = calculateRetailTotalPrice(retailData, retailRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdayRetailSales = calculateRetailTotalPrice(retailData, retailRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthRetailSales = calculateRetailTotalPrice(retailData, retailRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearRetailSales = calculateRetailTotalPrice(retailData, retailRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var todaySales = calculateSaleTotalPrice(salesData, salesRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdaySales = calculateSaleTotalPrice(salesData, salesRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthSales = calculateSaleTotalPrice(salesData, salesRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearSales = calculateSaleTotalPrice(salesData, salesRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var todayPurchase = calculatePurchaseTotalPrice(purchaseData, purchaseRefundData, now.with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yesterdayPurchase = calculatePurchaseTotalPrice(purchaseData, purchaseRefundData, now.minusDays(1).with(LocalTime.MIN), now.minusDays(1).with(LocalTime.MAX));
        var monthPurchase = calculatePurchaseTotalPrice(purchaseData, purchaseRefundData, now.withDayOfMonth(1).with(LocalTime.MIN), now.with(LocalTime.MAX));
        var yearPurchase = calculatePurchaseTotalPrice(purchaseData, purchaseRefundData, now.withDayOfYear(1).with(LocalTime.MIN), now.with(LocalTime.MAX));

        var assembleAxisRetailData = calculateRetailAxisData(retailData, retailRefundData);
        var assembleAxisSaleData = calculateSaleAxisData(salesData, salesRefundData);
        var assembleAxisPurchaseData = calculatePurchaseAxisData(purchaseData, purchaseRefundData);

        var retailStatisticalDataVO = StatisticalDataVO.builder()
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
                .retailAxisStatisticalDataVO(assembleAxisRetailData)
                .saleAxisStatisticalDataVO(assembleAxisSaleData)
                .purchaseAxisStatisticalDataVO(assembleAxisPurchaseData)
                .build();

        return Response.responseData(retailStatisticalDataVO);
    }

    private BigDecimal calculateRetailTotalPrice(List<ReceiptRetailMain> data, List<ReceiptRetailMain> backData, LocalDateTime start, LocalDateTime end) {
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

    private BigDecimal calculateSaleTotalPrice(List<ReceiptSaleMain> data, List<ReceiptSaleMain> backData, LocalDateTime start, LocalDateTime end) {
        var dataTotalPrice = data.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptSaleMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        var backDataTotalPrice = backData.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptSaleMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return dataTotalPrice.add(backDataTotalPrice);
    }

    private BigDecimal calculatePurchaseTotalPrice(List<ReceiptPurchaseMain> data, List<ReceiptPurchaseMain> backData, LocalDateTime start, LocalDateTime end) {
        var dataTotalPrice = data.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptPurchaseMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        var backDataTotalPrice = backData.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptPurchaseMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return dataTotalPrice.add(backDataTotalPrice);
    }

    private List<XyAxisDataBO> calculateRetailAxisData(List<ReceiptRetailMain> data, List<ReceiptRetailMain> backData) {
        LocalDateTime now = LocalDateTime.now();
        var xyAxisDataBOList = new ArrayList<XyAxisDataBO>();
        for (int i = 0; i < 6; i++) {
            LocalDateTime previousMonth = now.minusMonths(i);
            LocalDateTime start = now.minusMonths(i).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
            LocalDateTime end = now.minusMonths(i).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            BigDecimal totalPrice = calculateRetailTotalPriceForRecentMonths(data, backData, start, end);
            var xAxisData = previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            var xyAxisDataBO = XyAxisDataBO.builder()
                    .xAxisData(xAxisData)
                    .yAxisData(totalPrice)
                    .build();
            xyAxisDataBOList.add(xyAxisDataBO);
        }
        Collections.reverse(xyAxisDataBOList);
        return xyAxisDataBOList;
    }

    private BigDecimal calculateRetailTotalPriceForRecentMonths(List<ReceiptRetailMain> data, List<ReceiptRetailMain> backData, LocalDateTime start, LocalDateTime end) {
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


    private List<XyAxisDataBO> calculateSaleAxisData(List<ReceiptSaleMain> data, List<ReceiptSaleMain> backData) {
        LocalDateTime now = LocalDateTime.now();
        var xyAxisDataBOList = new ArrayList<XyAxisDataBO>();
        for (int i = 0; i < 6; i++) {
            LocalDateTime previousMonth = now.minusMonths(i);
            LocalDateTime start = now.minusMonths(i).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
            LocalDateTime end = now.minusMonths(i).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            BigDecimal totalPrice = calculateSaleTotalPriceForRecentMonths(data, backData, start, end);
            var xAxisData = previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            var xyAxisDataBO = XyAxisDataBO.builder()
                    .xAxisData(xAxisData)
                    .yAxisData(totalPrice)
                    .build();
            xyAxisDataBOList.add(xyAxisDataBO);
        }
        Collections.reverse(xyAxisDataBOList);
        return xyAxisDataBOList;
    }

    private BigDecimal calculateSaleTotalPriceForRecentMonths(List<ReceiptSaleMain> data, List<ReceiptSaleMain> backData, LocalDateTime start, LocalDateTime end) {
        var dataTotalPrice = data.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptSaleMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        var backDataTotalPrice = backData.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptSaleMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return dataTotalPrice.add(backDataTotalPrice);
    }

    private List<XyAxisDataBO> calculatePurchaseAxisData(List<ReceiptPurchaseMain> data, List<ReceiptPurchaseMain> backData) {
        LocalDateTime now = LocalDateTime.now();
        var xyAxisDataBOList = new ArrayList<XyAxisDataBO>();
        for (int i = 0; i < 6; i++) {
            LocalDateTime previousMonth = now.minusMonths(i);
            LocalDateTime start = now.minusMonths(i).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
            LocalDateTime end = now.minusMonths(i).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            BigDecimal totalPrice = calculatePurchaseTotalPriceForRecentMonths(data, backData, start, end);
            var xAxisData = previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            var xyAxisDataBO = XyAxisDataBO.builder()
                    .xAxisData(xAxisData)
                    .yAxisData(totalPrice)
                    .build();
            xyAxisDataBOList.add(xyAxisDataBO);
        }
        Collections.reverse(xyAxisDataBOList);
        return xyAxisDataBOList;
    }

    private BigDecimal calculatePurchaseTotalPriceForRecentMonths(List<ReceiptPurchaseMain> data, List<ReceiptPurchaseMain> backData, LocalDateTime start, LocalDateTime end) {
        var dataTotalPrice = data.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptPurchaseMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        var backDataTotalPrice = backData.stream()
                .filter(item -> item.getCreateTime().isAfter(start) && item.getCreateTime().isBefore(end))
                .map(ReceiptPurchaseMain::getDiscountLastAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        return dataTotalPrice.add(backDataTotalPrice);
    }

    @Override
    public Response<Page<ReceiptVO>> otherReceipt(QueryReceiptDTO receiptDTO) {
        String type = receiptDTO.getType();
        String subType = receiptDTO.getSubType();
        if (StringUtils.hasLength(type) && StringUtils.hasLength(subType)) {
            switch (type) {
                case "零售":
                    return Response.responseData(getReceiptRetailVOList(receiptDTO));
                case "销售":
                    return Response.responseData(getReceiptSaleVOList(receiptDTO));
                case "采购":
                    return Response.responseData(getReceiptPurchaseVOList(receiptDTO));
                default:
                    break;
            }
        }

        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
    }

    @Override
    public Response<Page<ReceiptDetailVO>> getOtherDetail(QueryReceiptDTO receiptDTO) {
        String type = receiptDTO.getType();
        Long id = receiptDTO.getId();
        if (StringUtils.hasLength(type) && id != null) {
            switch (type) {
                case "零售":
                    return Response.responseData(getReceiptRetailDetailVOList(receiptDTO));
                case "销售":
                    return Response.responseData(getReceiptSaleDetailVOList(receiptDTO));
                case "采购":
                    return Response.responseData(getReceiptPurchaseDetailVOList(receiptDTO));
                default:
                    break;
            }
        }

        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
    }

    private Page<ReceiptVO> getReceiptRetailVOList(QueryReceiptDTO receiptDTO) {
        var result = new Page<ReceiptVO>();

        var pageData = new Page<ReceiptRetailMain>(receiptDTO.getPage(), receiptDTO.getPageSize());
        var receiptRetailVOList = receiptRetailService.lambdaQuery()
                .eq(ReceiptRetailMain::getSubType, receiptDTO.getSubType())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptRetailMain::getStatus, CommonConstants.REVIEWED)
                .page(pageData);

        if (receiptRetailVOList.getRecords().isEmpty()) {
            return null;
        }

        var receiptVos = new ArrayList<ReceiptVO>(receiptRetailVOList.getRecords().size() + 2);
        receiptRetailVOList.getRecords().forEach(item -> {
            var member = memberService.lambdaQuery()
                    .eq(Member::getId, item.getMemberId())
                    .eq(Member::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one();

            var operator = userService.getById(item.getCreateBy());

            var productNumber = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptRetailSub::getProductNumber)
                    .sum();

            ReceiptVO receiptVO = ReceiptVO.builder()
                    .id(item.getId())
                    .name(Optional.ofNullable(member.getMemberName()).orElse(""))
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator.getName()).orElse(""))
                    .productNumber(productNumber)
                    .totalAmount(item.getTotalAmount())
                    .status(item.getStatus())
                    .build();
            receiptVos.add(receiptVO);
        });
        result.setRecords(receiptVos);
        result.setPages(receiptRetailVOList.getPages());
        result.setSize(receiptRetailVOList.getSize());
        result.setTotal(receiptRetailVOList.getTotal());
        result.setCurrent(receiptRetailVOList.getCurrent());
        return result;
    }

    private Page<ReceiptVO> getReceiptSaleVOList(QueryReceiptDTO receiptDTO) {
        var result = new Page<ReceiptVO>();
        var pageData = new Page<ReceiptSaleMain>(receiptDTO.getPage(), receiptDTO.getPageSize());

        var receiptSaleVOList = receiptSaleService.lambdaQuery()
                .eq(ReceiptSaleMain::getSubType, receiptDTO.getSubType())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptSaleMain::getStatus, CommonConstants.REVIEWED)
                .page(pageData);

        if (receiptSaleVOList.getRecords().isEmpty()) {
            return null;
        }

        var receiptVos = new ArrayList<ReceiptVO>(receiptSaleVOList.getRecords().size() + 2);
        receiptSaleVOList.getRecords().forEach(item -> {
            var customer = customerService.lambdaQuery()
                    .eq(Customer::getId, item.getCustomerId())
                    .eq(Customer::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one();

            var operator = userService.getById(item.getCreateBy());

            var productNumber = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptSaleSub::getProductNumber)
                    .sum();

            ReceiptVO receiptVO = ReceiptVO.builder()
                    .id(item.getId())
                    .name(Optional.ofNullable(customer.getCustomerName()).orElse(""))
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator.getName()).orElse(""))
                    .productNumber(productNumber)
                    .totalAmount(item.getTotalAmount())
                    .taxRateTotalAmount(item.getDiscountLastAmount())
                    .status(item.getStatus())
                    .build();
            receiptVos.add(receiptVO);
        });
        result.setRecords(receiptVos);
        result.setPages(receiptSaleVOList.getPages());
        result.setSize(receiptSaleVOList.getSize());
        result.setTotal(receiptSaleVOList.getTotal());
        result.setCurrent(receiptSaleVOList.getCurrent());
        return result;
    }

    private Page<ReceiptVO> getReceiptPurchaseVOList(QueryReceiptDTO receiptDTO) {
        var result = new Page<ReceiptVO>();
        var pageData = new Page<ReceiptPurchaseMain>(receiptDTO.getPage(), receiptDTO.getPageSize());

        var receiptPurchaseVOList = receiptPurchaseService.lambdaQuery()
                .eq(ReceiptPurchaseMain::getSubType, receiptDTO.getSubType())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptPurchaseMain::getStatus, CommonConstants.REVIEWED)
                .page(pageData);

        if (receiptPurchaseVOList.getRecords().isEmpty()) {
            return null;
        }

        var receiptVos = new ArrayList<ReceiptVO>(receiptPurchaseVOList.getRecords().size() + 2);
        receiptPurchaseVOList.getRecords().forEach(item -> {
            var supplier = supplierService.lambdaQuery()
                    .eq(Supplier::getId, item.getSupplierId())
                    .eq(Supplier::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one();

            var operator = userService.getById(item.getCreateBy());

            var productNumber = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list()
                    .stream()
                    .mapToInt(ReceiptPurchaseSub::getProductNumber)
                    .sum();

            ReceiptVO receiptVO = ReceiptVO.builder()
                    .id(item.getId())
                    .name(Optional.ofNullable(supplier.getSupplierName()).orElse(""))
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .operator(Optional.ofNullable(operator.getName()).orElse(""))
                    .productNumber(productNumber)
                    .totalAmount(item.getTotalAmount())
                    .taxRateTotalAmount(item.getDiscountLastAmount())
                    .status(item.getStatus())
                    .build();
            receiptVos.add(receiptVO);
        });

        result.setRecords(receiptVos);
        result.setPages(receiptPurchaseVOList.getPages());
        result.setSize(receiptPurchaseVOList.getSize());
        result.setTotal(receiptPurchaseVOList.getTotal());
        result.setCurrent(receiptPurchaseVOList.getCurrent());
        return result;
    }

    private Page<ReceiptDetailVO> getReceiptRetailDetailVOList(QueryReceiptDTO receiptDTO) {
        var result = new Page<ReceiptDetailVO>();
        var pageData = new Page<ReceiptRetailSub>(receiptDTO.getPage(), receiptDTO.getPageSize());

        var receiptRetailDetails = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getReceiptMainId, receiptDTO.getId())
                .page(pageData);

        if (receiptRetailDetails.getRecords().isEmpty()) {
            return null;
        }

        var receiptDetailVos = new ArrayList<ReceiptDetailVO>(receiptRetailDetails.getRecords().size() + 2);
        receiptRetailDetails.getRecords().forEach(item -> {

            var receiptDetailVO = ReceiptDetailVO.builder()
                    .id(item.getId())
                    .warehouseId(item.getWarehouseId())
                    .productId(item.getProductId())
                    .productBarcode(item.getProductBarcode())
                    .unitPrice(item.getUnitPrice())
                    .productNumber(item.getProductNumber())
                    .amount(item.getTotalAmount())
                    .remark(item.getRemark())
                    .build();

            var productVO = productService.getProductInfoDetail(item.getProductId()).getData();
            if (productVO != null) {
                receiptDetailVO.setProductName(productVO.getProductName());
                receiptDetailVO.setProductStandard(productVO.getProductStandard());
                receiptDetailVO.setProductModel(productVO.getProductModel());
                receiptDetailVO.setUnit(productVO.getProductUnit());
            }
            // 查询库存
            var stock = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if (stock != null) {
                receiptDetailVO.setStock(stock.getStock());
            }

            receiptDetailVos.add(receiptDetailVO);
        });

        result.setRecords(receiptDetailVos);
        result.setPages(receiptRetailDetails.getPages());
        result.setSize(receiptRetailDetails.getSize());
        result.setTotal(receiptRetailDetails.getTotal());
        result.setCurrent(receiptRetailDetails.getCurrent());

        return result;
    }

    private Page<ReceiptDetailVO> getReceiptSaleDetailVOList(QueryReceiptDTO receiptDTO) {
        var result = new Page<ReceiptDetailVO>();
        var pageData = new Page<ReceiptSaleSub>(receiptDTO.getPage(), receiptDTO.getPageSize());

        var receiptSaleDetails = receiptSaleSubService.lambdaQuery()
                .eq(ReceiptSaleSub::getReceiptSaleMainId, receiptDTO.getId())
                .page(pageData);

        if (receiptSaleDetails.getRecords().isEmpty()) {
            return null;
        }

        var receiptDetailVos = new ArrayList<ReceiptDetailVO>(receiptSaleDetails.getRecords().size() + 2);
        receiptSaleDetails.getRecords().forEach(item -> {
            var receiptDetailVO = ReceiptDetailVO.builder()
                    .id(item.getId())
                    .warehouseId(item.getWarehouseId())
                    .productId(item.getProductId())
                    .productBarcode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .amount(item.getTotalAmount())
                    .unitPrice(item.getUnitPrice())
                    .taxRate(item.getTaxRate())
                    .taxAmount(item.getTaxAmount())
                    .taxIncludedAmount(item.getTaxIncludedAmount())
                    .remark(item.getRemark())
                    .build();

            var productVO = productService.getProductInfoDetail(item.getProductId()).getData();
            if (productVO != null) {
                receiptDetailVO.setProductName(productVO.getProductName());
                receiptDetailVO.setProductStandard(productVO.getProductStandard());
                receiptDetailVO.setProductModel(productVO.getProductModel());
                receiptDetailVO.setUnit(productVO.getProductUnit());
            }
            // 查询库存
            var stock = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if (stock != null) {
                receiptDetailVO.setStock(stock.getStock());
            }
            receiptDetailVos.add(receiptDetailVO);
        });

        result.setRecords(receiptDetailVos);
        result.setPages(receiptSaleDetails.getPages());
        result.setSize(receiptSaleDetails.getSize());
        result.setTotal(receiptSaleDetails.getTotal());
        result.setCurrent(receiptSaleDetails.getCurrent());

        return result;
    }

    private Page<ReceiptDetailVO> getReceiptPurchaseDetailVOList(QueryReceiptDTO receiptDTO) {
        var result = new Page<ReceiptDetailVO>();
        var pageData = new Page<ReceiptPurchaseSub>(receiptDTO.getPage(), receiptDTO.getPageSize());

        var receiptPurchaseDetails = receiptPurchaseSubService.lambdaQuery()
                .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, receiptDTO.getId())
                .page(pageData);

        if (receiptPurchaseDetails.getRecords().isEmpty()) {
            return null;
        }

        var receiptDetailVos = new ArrayList<ReceiptDetailVO>(receiptPurchaseDetails.getRecords().size() + 2);
        receiptPurchaseDetails.getRecords().forEach(item -> {
            var receiptDetailVO = ReceiptDetailVO.builder()
                    .id(item.getId())
                    .warehouseId(item.getWarehouseId())
                    .productId(item.getProductId())
                    .productBarcode(item.getProductBarcode())
                    .productNumber(item.getProductNumber())
                    .amount(item.getTotalAmount())
                    .unitPrice(item.getUnitPrice())
                    .taxRate(item.getTaxRate())
                    .taxAmount(item.getTaxAmount())
                    .taxIncludedAmount(item.getTaxIncludedAmount())
                    .remark(item.getRemark())
                    .build();
            var productVO = productService.getProductInfoDetail(item.getProductId()).getData();
            if (productVO != null) {
                receiptDetailVO.setProductName(productVO.getProductName());
                receiptDetailVO.setProductStandard(productVO.getProductStandard());
                receiptDetailVO.setProductModel(productVO.getProductModel());
                receiptDetailVO.setUnit(productVO.getProductUnit());
            }
            // 查询库存
            var stock = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
            if (stock != null) {
                receiptDetailVO.setStock(stock.getStock());
            }
            receiptDetailVos.add(receiptDetailVO);
        });
        result.setRecords(receiptDetailVos);
        result.setPages(receiptPurchaseDetails.getPages());
        result.setSize(receiptPurchaseDetails.getSize());
        result.setTotal(receiptPurchaseDetails.getTotal());
        result.setCurrent(receiptPurchaseDetails.getCurrent());

        return result;
    }

    @Override
    public Response<IPage<ProductStockVO>> getProductStock(QueryProductStockDTO queryProductStockDTO) {
        var page = new Page<QueryProductStockDTO>(queryProductStockDTO.getPage(), queryProductStockDTO.getPageSize());
        // 获取默认仓库
//        var warehouse = warehouseService.getDefaultWarehouse();
//        if(warehouse.getData() != null) {
//            queryProductStock.setWarehouseId(warehouse.getData().getId());
//        }
        var result = productStockMapper.getProductStock(page, queryProductStockDTO);
        return Response.responseData(result);
    }

    @Override
    public Response<Page<StockFlowVO>> getStockFlow(QueryStockFlowDTO queryStockFlowDTO) {
        var retailData = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getWarehouseId, queryStockFlowDTO.getWarehouseId())
                .eq(ReceiptRetailSub::getProductBarcode, queryStockFlowDTO.getProductBarcode())
                .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        var salesData = receiptSaleSubService.lambdaQuery()
                .eq(ReceiptSaleSub::getWarehouseId, queryStockFlowDTO.getWarehouseId())
                .eq(ReceiptSaleSub::getProductBarcode, queryStockFlowDTO.getProductBarcode())
                .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        var purchaseData = receiptPurchaseSubService.lambdaQuery()
                .eq(ReceiptPurchaseSub::getWarehouseId, queryStockFlowDTO.getWarehouseId())
                .eq(ReceiptPurchaseSub::getProductBarcode, queryStockFlowDTO.getProductBarcode())
                .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        List<StockFlowVO> stockFlowVos = new ArrayList<StockFlowVO>(retailData.size() + salesData.size() + purchaseData.size());
        retailData.forEach(item -> {
            var receiptRetailMain = receiptRetailService.lambdaQuery()
                    .eq(ReceiptRetailMain::getId, item.getReceiptMainId())
                    .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one();
            if (receiptRetailMain != null) {
                var stockFlowVO = StockFlowVO.builder()
                        .receiptNumber(receiptRetailMain.getReceiptNumber())
                        .receiptDate(receiptRetailMain.getReceiptDate())
                        .type(receiptRetailMain.getSubType())
                        .productNumber(item.getProductNumber())
                        .productBarcode(item.getProductBarcode())
                        .productName(commonService.getProductName(item.getProductId()))
                        .warehouseName(commonService.getWarehouseName(item.getWarehouseId()))
                        .build();
                stockFlowVos.add(stockFlowVO);
            }
        });
        salesData.forEach(item -> {
            var receiptSaleMain = receiptSaleService.lambdaQuery()
                    .eq(ReceiptSaleMain::getId, item.getReceiptSaleMainId())
                    .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one();
            if (receiptSaleMain != null) {
                var stockFlowVO = StockFlowVO.builder()
                        .receiptNumber(receiptSaleMain.getReceiptNumber())
                        .receiptDate(receiptSaleMain.getReceiptDate())
                        .type(receiptSaleMain.getSubType())
                        .productNumber(item.getProductNumber())
                        .productBarcode(item.getProductBarcode())
                        .productName(commonService.getProductName(item.getProductId()))
                        .warehouseName(commonService.getWarehouseName(item.getWarehouseId()))
                        .build();
                stockFlowVos.add(stockFlowVO);
            }
        });
        purchaseData.forEach(item -> {
            var receiptPurchaseMain = receiptPurchaseService.lambdaQuery()
                    .eq(ReceiptPurchaseMain::getId, item.getReceiptPurchaseMainId())
                    .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .one();
            if (receiptPurchaseMain != null) {
                var stockFlowVO = StockFlowVO.builder()
                        .receiptNumber(receiptPurchaseMain.getReceiptNumber())
                        .receiptDate(receiptPurchaseMain.getReceiptDate())
                        .type(receiptPurchaseMain.getSubType())
                        .productNumber(item.getProductNumber())
                        .productBarcode(item.getProductBarcode())
                        .productName(commonService.getProductName(item.getProductId()))
                        .warehouseName(commonService.getWarehouseName(item.getWarehouseId()))
                        .build();
                stockFlowVos.add(stockFlowVO);
            }
        });
        stockFlowVos.sort(Comparator.comparing(StockFlowVO::getReceiptDate).reversed());
        var page = new Page<StockFlowVO>(queryStockFlowDTO.getPage(), queryStockFlowDTO.getPageSize());

        int startIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        int endIndex = (int) Math.min(startIndex + page.getSize(), stockFlowVos.size());
        startIndex = Math.min(startIndex, endIndex);
        List<StockFlowVO> pagedStockFlowVos = new ArrayList<>(stockFlowVos.subList(startIndex, endIndex));
        page.setRecords(pagedStockFlowVos);
        page.setTotal(stockFlowVos.size());
        return Response.responseData(page);

    }

    @Override
    public Response<Page<AccountStatisticsVO>> getAccountStatistics(QueryAccountStatisticsDTO accountStatisticsDTO) {
        var result = new Page<AccountStatisticsVO>();

        var page = new Page<FinancialAccount>(accountStatisticsDTO.getPage(), accountStatisticsDTO.getPageSize());
        var accountPage = accountService.lambdaQuery()
                .eq(FinancialAccount::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(StringUtils.hasLength(accountStatisticsDTO.getAccountName()), FinancialAccount::getAccountName, accountStatisticsDTO.getAccountName())
                .eq(StringUtils.hasLength(accountStatisticsDTO.getAccountNumber()), FinancialAccount::getAccountNumber, accountStatisticsDTO.getAccountNumber())
                .page(page);

        if (accountPage.getRecords().isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }

        var accountVos = new ArrayList<AccountStatisticsVO>();
        accountPage.getRecords().forEach(item -> {
            var retailData = receiptRetailService.lambdaQuery()
                    .eq(ReceiptRetailMain::getAccountId, item.getId())
                    .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            var retailChangeAmount = BigDecimal.ZERO;
            var saleChangeAmount = BigDecimal.ZERO;
            var purchaseChangeAmount = BigDecimal.ZERO;

            if (!retailData.isEmpty()) {
                retailChangeAmount = retailData.stream()
                        .filter(receiptRetailMain -> receiptRetailMain.getReceiptDate().isAfter(LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN)))
                        .filter(receiptRetailMain -> receiptRetailMain.getReceiptDate().isBefore(LocalDateTime.now().with(LocalTime.MAX)))
                        .toList()
                        .stream()
                        .map(receipt -> Optional.ofNullable(receipt.getChangeAmount()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            var salesData = receiptSaleService.lambdaQuery()
                    .eq(ReceiptSaleMain::getAccountId, item.getId())
                    .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            if (!salesData.isEmpty()) {
                saleChangeAmount = salesData.stream()
                        .filter(receiptSaleMain -> receiptSaleMain.getReceiptDate().isAfter(LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN)))
                        .filter(receiptSaleMain -> receiptSaleMain.getReceiptDate().isBefore(LocalDateTime.now().with(LocalTime.MAX)))
                        .toList()
                        .stream()
                        .map(receipt -> Optional.ofNullable(receipt.getChangeAmount()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            var purchaseData = receiptPurchaseService.lambdaQuery()
                    .eq(ReceiptPurchaseMain::getAccountId, item.getId())
                    .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            if (!purchaseData.isEmpty()) {
                purchaseChangeAmount = purchaseData.stream()
                        .filter(receiptPurchaseMain -> receiptPurchaseMain.getReceiptDate().isAfter(LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN)))
                        .filter(receiptPurchaseMain -> receiptPurchaseMain.getReceiptDate().isBefore(LocalDateTime.now().with(LocalTime.MAX)))
                        .toList()
                        .stream()
                        .map(receipt -> Optional.ofNullable(receipt.getChangeAmount()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // Calculate the amount of multiple accounts
            var saleAccountMultipleData = receiptSaleService.lambdaQuery()
                    .in(ReceiptSaleMain::getMultipleAccount, item.getId())
                    .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list()
                    .stream()
                    .filter(receiptSaleMain -> receiptSaleMain.getReceiptDate().isAfter(LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN)))
                    .filter(receiptSaleMain -> receiptSaleMain.getReceiptDate().isBefore(LocalDateTime.now().with(LocalTime.MAX)))
                    .toList();

            if (!saleAccountMultipleData.isEmpty()) {
                for (ReceiptSaleMain saleAccountMultiple : saleAccountMultipleData) {
                    var saleAccountMultipleChangeAmount = saleAccountMultiple.getChangeAmount();
                    purchaseChangeAmount = purchaseChangeAmount.add(Optional.ofNullable(saleAccountMultipleChangeAmount).orElse(BigDecimal.ZERO));
                }
            }

            var purchaseAccountMultipleData = receiptPurchaseService.lambdaQuery()
                    .in(ReceiptPurchaseMain::getMultipleAccount, item.getId())
                    .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list()
                    .stream()
                    .filter(ReceiptPurchaseMain -> ReceiptPurchaseMain.getReceiptDate().isAfter(LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN)))
                    .filter(ReceiptPurchaseMain -> ReceiptPurchaseMain.getReceiptDate().isBefore(LocalDateTime.now().with(LocalTime.MAX)))
                    .toList();

            if (!purchaseAccountMultipleData.isEmpty()) {
                for (ReceiptPurchaseMain purchaseAccountMultiple : purchaseAccountMultipleData) {
                    var purchaseAccountMultipleChangeAmount = purchaseAccountMultiple.getChangeAmount();
                    purchaseChangeAmount = purchaseChangeAmount.add(Optional.ofNullable(purchaseAccountMultipleChangeAmount).orElse(BigDecimal.ZERO));
                }
            }

            var thisMonthChangeAmount = retailChangeAmount.add(saleChangeAmount).add(purchaseChangeAmount);

            var accountVo = AccountStatisticsVO.builder()
                    .accountId(item.getId())
                    .accountName(item.getAccountName())
                    .accountNumber(item.getAccountNumber())
                    .initialAmount(item.getInitialAmount())
                    .thisMonthChangeAmount(thisMonthChangeAmount)
                    .currentAmount(item.getCurrentAmount())
                    .build();

            accountVos.add(accountVo);
        });
        result.setRecords(accountVos);
        result.setPages(accountPage.getPages());
        result.setSize(accountPage.getSize());
        result.setTotal(accountPage.getTotal());

        return Response.responseData(result);
    }

    @Override
    public Response<Page<AccountFlowVO>> getAccountFlow(Long accountId, Long page, Long pageSize) {
        if (accountId == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var account = accountService.getById(accountId);
        var retailData = receiptRetailService.lambdaQuery()
                .eq(ReceiptRetailMain::getAccountId, accountId)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var balance = new AtomicReference<>(account.getInitialAmount());

        var accountFlowVos = new ArrayList<AccountFlowVO>();
        if (!retailData.isEmpty()) {
            retailData.forEach(retail -> {
                var accountFlowVO = AccountFlowVO.builder()
                        .receiptNumber(retail.getReceiptNumber())
                        .receiptDate(retail.getReceiptDate())
                        .subType(retail.getSubType())
                        .useType("会员")
                        .name(commonService.getMemberName(retail.getMemberId()))
                        .amount(retail.getChangeAmount() == null ? BigDecimal.ZERO : retail.getChangeAmount())
                        .build();
                accountFlowVos.add(accountFlowVO);
            });
        }

        var salesData = receiptSaleService.lambdaQuery()
                .eq(ReceiptSaleMain::getAccountId, accountId)
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        if (!salesData.isEmpty()) {
            salesData.forEach(sale -> {
                var accountFlowVO = AccountFlowVO.builder()
                        .receiptNumber(sale.getReceiptNumber())
                        .receiptDate(sale.getReceiptDate())
                        .subType(sale.getSubType())
                        .useType("客户")
                        .name(commonService.getCustomerName(sale.getCustomerId()))
                        .amount(sale.getChangeAmount() == null ? BigDecimal.ZERO : sale.getChangeAmount())
                        .build();
                accountFlowVos.add(accountFlowVO);
            });
        }

        var purchaseData = receiptPurchaseService.lambdaQuery()
                .eq(ReceiptPurchaseMain::getAccountId, accountId)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        if (!purchaseData.isEmpty()) {
            purchaseData.forEach(purchase -> {
                var accountFlowVO = AccountFlowVO.builder()
                        .receiptNumber(purchase.getReceiptNumber())
                        .receiptDate(purchase.getReceiptDate())
                        .subType(purchase.getSubType())
                        .useType("供应商")
                        .name(commonService.getSupplierName(purchase.getSupplierId()))
                        .amount(purchase.getChangeAmount() == null ? BigDecimal.ZERO : purchase.getChangeAmount())
                        .build();
                accountFlowVos.add(accountFlowVO);
            });
        }
        accountFlowVos.sort(Comparator.comparing(AccountFlowVO::getReceiptDate).reversed());
        // 进行余额计算
        accountFlowVos.forEach(item -> {
            balance.updateAndGet(v -> v.add(item.getAmount()));
            item.setBalance(balance.get());
        });

        var result = new Page<AccountFlowVO>(page, pageSize);
        int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
        int endIndex = (int) Math.min(startIndex + result.getSize(), accountFlowVos.size());
        startIndex = Math.min(startIndex, endIndex);
        List<AccountFlowVO> pageAccountFlowVos = new ArrayList<>(accountFlowVos.subList(startIndex, endIndex));
        result.setRecords(pageAccountFlowVos);
        result.setTotal(accountFlowVos.size());

        return Response.responseData(result);
    }

    @Override
    public Response<Page<RetailReportVO>> getRetailStatistics(QueryRetailReportDTO queryRetailReportDTO) {
        var result = new Page<RetailReportVO>();
        var page = new Page<ReceiptRetailMain>(queryRetailReportDTO.getPage(), queryRetailReportDTO.getPageSize());
        var retailPage = receiptRetailService.lambdaQuery()
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(queryRetailReportDTO.getMemberId() != null, ReceiptRetailMain::getMemberId, queryRetailReportDTO.getMemberId())
                .ge(queryRetailReportDTO.getStartDate() != null, ReceiptRetailMain::getReceiptDate, queryRetailReportDTO.getStartDate())
                .le(queryRetailReportDTO.getEndDate() != null, ReceiptRetailMain::getReceiptDate, queryRetailReportDTO.getEndDate())
                .page(page);

        var retailVos = new ArrayList<RetailReportVO>();
        retailPage.getRecords().forEach(item -> {
            var retailSubs = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            for (ReceiptRetailSub retailSub : retailSubs) {
                var retailVo = RetailReportVO.builder()
                        .productBarcode(retailSub.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(retailSub.getWarehouseId()))
                        .build();

                var product = productService.getById(retailSub.getProductId());
                if (product != null) {
                    String productExtendInfo = product.getProductManufacturer() +
                            "|" +
                            product.getOtherFieldOne() +
                            "|" +
                            product.getOtherFieldTwo() +
                            "|" +
                            product.getOtherFieldThree();
                    retailVo.setProductName(product.getProductName());
                    retailVo.setProductStandard(product.getProductStandard());
                    retailVo.setProductModel(product.getProductModel());
                    retailVo.setProductUnit(product.getProductUnit());
                    retailVo.setProductExtendInfo(productExtendInfo);
                }
                // 如果是相同的商品条码和仓库名称 则不进行重复添加
                if (retailVos.stream().noneMatch(matchRetailVo -> retailVo.getProductBarcode().equals(matchRetailVo.getProductBarcode())
                        && retailVo.getWarehouseName().equals(matchRetailVo.getWarehouseName()))) {
                    retailVos.add(retailVo);
                }
                // 将数据进行组装 计算重复商品的出库和退货数量以及金额 匹配商品条码和仓库名称
                if (retailVo.getProductBarcode() != null && retailVo.getWarehouseName() != null) {
                    retailVos.forEach(matchRetailVo -> {
                        if (retailVo.getProductBarcode().equals(matchRetailVo.getProductBarcode())
                                && retailVo.getWarehouseName().equals(matchRetailVo.getWarehouseName())) {
                            if (item.getSubType().equals("零售出库")) {
                                matchRetailVo.setRetailAmount(Optional.ofNullable(matchRetailVo.getRetailAmount()).orElse(BigDecimal.ZERO)
                                        .add(Optional.ofNullable(retailSub.getTotalAmount()).orElse(BigDecimal.ZERO)));
                                matchRetailVo.setRetailNumber(Optional.ofNullable(matchRetailVo.getRetailNumber()).orElse(0)
                                        + Optional.ofNullable(retailSub.getProductNumber()).orElse(0));
                            } else if (item.getSubType().equals("零售退货")) {
                                matchRetailVo.setRetailRefundAmount(Optional.ofNullable(matchRetailVo.getRetailRefundAmount()).orElse(BigDecimal.ZERO)
                                        .add(Optional.ofNullable(retailSub.getTotalAmount()).orElse(BigDecimal.ZERO)));
                                matchRetailVo.setRetailRefundNumber(Optional.ofNullable(matchRetailVo.getRetailRefundNumber()).orElse(0)
                                        + Optional.ofNullable(retailSub.getProductNumber()).orElse(0));
                            }
                            // 如果没有值 则归0处理
                            if (matchRetailVo.getRetailAmount() == null) {
                                matchRetailVo.setRetailAmount(BigDecimal.ZERO);
                            }
                            if (matchRetailVo.getRetailRefundAmount() == null) {
                                matchRetailVo.setRetailRefundAmount(BigDecimal.ZERO);
                            }
                            if (matchRetailVo.getRetailNumber() == null) {
                                matchRetailVo.setRetailNumber(0);
                            }
                            if (matchRetailVo.getRetailRefundNumber() == null) {
                                matchRetailVo.setRetailRefundNumber(0);
                            }

                            matchRetailVo.setRetailLastAmount(Optional.ofNullable(matchRetailVo.getRetailAmount()).orElse(BigDecimal.ZERO)
                                    .subtract(Optional.ofNullable(matchRetailVo.getRetailRefundAmount()).orElse(BigDecimal.ZERO)));
                        }
                    });
                }
            }
        });
        result.setRecords(retailVos);
        result.setPages(retailPage.getPages());
        result.setSize(retailPage.getSize());
        result.setTotal(retailVos.size());

        return Response.responseData(result);
    }

    @Override
    public Response<Page<PurchaseReportVO>> getPurchaseStatistics(QueryPurchaseReportDTO queryPurchaseReportDTO) {
        var result = new Page<PurchaseReportVO>();
        var page = new Page<ReceiptPurchaseMain>(queryPurchaseReportDTO.getPage(), queryPurchaseReportDTO.getPageSize());
        var purchasePage = receiptPurchaseService.lambdaQuery()
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(queryPurchaseReportDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseReportDTO.getSupplierId())
                .in(ReceiptPurchaseMain::getSubType, "采购入库", "采购退货")
                .ge(queryPurchaseReportDTO.getStartDate() != null, ReceiptPurchaseMain::getReceiptDate, queryPurchaseReportDTO.getStartDate())
                .le(queryPurchaseReportDTO.getEndDate() != null, ReceiptPurchaseMain::getReceiptDate, queryPurchaseReportDTO.getEndDate())
                .page(page);

        var purchaseVos = new ArrayList<PurchaseReportVO>();
        purchasePage.getRecords().forEach(item -> {
            var purchaseSub = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            for (ReceiptPurchaseSub receiptPurchaseSub : purchaseSub) {
                var purchaseVo = PurchaseReportVO.builder()
                        .productBarcode(receiptPurchaseSub.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(receiptPurchaseSub.getWarehouseId()))
                        .build();

                var product = productService.getById(receiptPurchaseSub.getProductId());
                if (product != null) {
                    String productExtendInfo = product.getProductManufacturer() +
                            "|" +
                            product.getOtherFieldOne() +
                            "|" +
                            product.getOtherFieldTwo() +
                            "|" +
                            product.getOtherFieldThree();
                    purchaseVo.setProductName(product.getProductName());
                    purchaseVo.setProductStandard(product.getProductStandard());
                    purchaseVo.setProductModel(product.getProductModel());
                    purchaseVo.setProductUnit(product.getProductUnit());
                    purchaseVo.setProductExtendInfo(productExtendInfo);
                }
                if (purchaseVos.stream().noneMatch(matchPurchaseVo -> purchaseVo.getProductBarcode().equals(matchPurchaseVo.getProductBarcode())
                        && purchaseVo.getWarehouseName().equals(matchPurchaseVo.getWarehouseName()))) {
                    purchaseVos.add(purchaseVo);
                }

                if (purchaseVo.getProductBarcode() != null && purchaseVo.getWarehouseName() != null) {
                    purchaseVos.forEach(matchPurchaseVo -> {
                        if (purchaseVo.getProductBarcode().equals(matchPurchaseVo.getProductBarcode())
                                && purchaseVo.getWarehouseName().equals(matchPurchaseVo.getWarehouseName())) {
                            if (item.getSubType().equals("采购入库")) {
                                matchPurchaseVo.setPurchaseAmount(Optional.ofNullable(matchPurchaseVo.getPurchaseAmount()).orElse(BigDecimal.ZERO)
                                        .add(Optional.ofNullable(receiptPurchaseSub.getTaxIncludedAmount()).orElse(BigDecimal.ZERO)));
                                matchPurchaseVo.setPurchaseNumber(Optional.ofNullable(matchPurchaseVo.getPurchaseNumber()).orElse(0)
                                        + Optional.ofNullable(receiptPurchaseSub.getProductNumber()).orElse(0));
                            } else if (item.getSubType().equals("采购退货")) {
                                matchPurchaseVo.setPurchaseRefundAmount(Optional.ofNullable(matchPurchaseVo.getPurchaseRefundAmount()).orElse(BigDecimal.ZERO)
                                        .add(Optional.ofNullable(receiptPurchaseSub.getTaxIncludedAmount()).orElse(BigDecimal.ZERO)));
                                matchPurchaseVo.setPurchaseRefundNumber(Optional.ofNullable(matchPurchaseVo.getPurchaseRefundNumber()).orElse(0)
                                        + Optional.ofNullable(receiptPurchaseSub.getProductNumber()).orElse(0));
                            }
                            if (matchPurchaseVo.getPurchaseAmount() == null) {
                                matchPurchaseVo.setPurchaseAmount(BigDecimal.ZERO);
                            }
                            if (matchPurchaseVo.getPurchaseRefundAmount() == null) {
                                matchPurchaseVo.setPurchaseRefundAmount(BigDecimal.ZERO);
                            }
                            if (matchPurchaseVo.getPurchaseNumber() == null) {
                                matchPurchaseVo.setPurchaseNumber(0);
                            }
                            if (matchPurchaseVo.getPurchaseRefundNumber() == null) {
                                matchPurchaseVo.setPurchaseRefundNumber(0);
                            }

                            matchPurchaseVo.setPurchaseLastAmount(Optional.ofNullable(matchPurchaseVo.getPurchaseAmount()).orElse(BigDecimal.ZERO)
                                    .subtract(Optional.ofNullable(matchPurchaseVo.getPurchaseRefundAmount()).orElse(BigDecimal.ZERO)));
                        }
                    });
                }
            }
        });
        result.setRecords(purchaseVos);
        result.setPages(purchasePage.getPages());
        result.setSize(purchasePage.getSize());
        result.setTotal(purchaseVos.size());

        return Response.responseData(result);
    }

    @Override
    public Response<Page<SalesReportVO>> getSalesStatistics(QuerySalesReportDTO querySalesReportDTO) {
        var result = new Page<SalesReportVO>();
        var page = new Page<ReceiptSaleMain>(querySalesReportDTO.getPage(), querySalesReportDTO.getPageSize());
        var salePage = receiptSaleService.lambdaQuery()
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(querySalesReportDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, querySalesReportDTO.getCustomerId())
                .in(ReceiptSaleMain::getSubType, "销售出库", "销售退货")
                .ge(querySalesReportDTO.getStartDate() != null, ReceiptSaleMain::getReceiptDate, querySalesReportDTO.getStartDate())
                .le(querySalesReportDTO.getEndDate() != null, ReceiptSaleMain::getReceiptDate, querySalesReportDTO.getEndDate())
                .page(page);

        var saleVos = new ArrayList<SalesReportVO>();
        salePage.getRecords().forEach(item -> {
            var saleSubs = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            for (ReceiptSaleSub saleSub : saleSubs) {
                var saleVo = SalesReportVO.builder()
                        .productBarcode(saleSub.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(saleSub.getWarehouseId()))
                        .build();

                var product = productService.getById(saleSub.getProductId());
                if (product != null) {
                    String productExtendInfo = product.getProductManufacturer() +
                            "|" +
                            product.getOtherFieldOne() +
                            "|" +
                            product.getOtherFieldTwo() +
                            "|" +
                            product.getOtherFieldThree();
                    saleVo.setProductName(product.getProductName());
                    saleVo.setProductStandard(product.getProductStandard());
                    saleVo.setProductModel(product.getProductModel());
                    saleVo.setProductUnit(product.getProductUnit());
                    saleVo.setProductExtendInfo(productExtendInfo);
                }


                if (saleVos.stream().noneMatch(matchSaleVo -> saleVo.getProductBarcode().equals(matchSaleVo.getProductBarcode())
                        && saleVo.getWarehouseName().equals(matchSaleVo.getWarehouseName()))) {
                    saleVos.add(saleVo);
                }

                if (saleVo.getProductBarcode() != null && saleVo.getWarehouseName() != null) {
                    saleVos.forEach(matchSaleVo -> {
                        if (saleVo.getProductBarcode().equals(matchSaleVo.getProductBarcode())
                                && saleVo.getWarehouseName().equals(matchSaleVo.getWarehouseName())) {
                            if (item.getSubType().equals("销售出库")) {
                                matchSaleVo.setSalesAmount(Optional.ofNullable(matchSaleVo.getSalesAmount()).orElse(BigDecimal.ZERO)
                                        .add(Optional.ofNullable(saleSub.getTaxIncludedAmount()).orElse(BigDecimal.ZERO)));
                                matchSaleVo.setSalesNumber(Optional.ofNullable(matchSaleVo.getSalesNumber()).orElse(0)
                                        + Optional.ofNullable(saleSub.getProductNumber()).orElse(0));
                            } else if (item.getSubType().equals("销售退货")) {
                                matchSaleVo.setSalesRefundAmount(Optional.ofNullable(matchSaleVo.getSalesRefundAmount()).orElse(BigDecimal.ZERO)
                                        .add(Optional.ofNullable(saleSub.getTaxIncludedAmount()).orElse(BigDecimal.ZERO)));
                                matchSaleVo.setSalesRefundNumber(Optional.ofNullable(matchSaleVo.getSalesRefundNumber()).orElse(0)
                                        + Optional.ofNullable(saleSub.getProductNumber()).orElse(0));
                            }
                            if (saleVo.getSalesAmount() == null) {
                                saleVo.setSalesAmount(BigDecimal.ZERO);
                            }
                            if (saleVo.getSalesRefundAmount() == null) {
                                saleVo.setSalesRefundAmount(BigDecimal.ZERO);
                            }
                            if (saleVo.getSalesNumber() == null) {
                                saleVo.setSalesNumber(0);
                            }
                            if (saleVo.getSalesRefundNumber() == null) {
                                saleVo.setSalesRefundNumber(0);
                            }

                            matchSaleVo.setSalesLastAmount(Optional.ofNullable(matchSaleVo.getSalesAmount()).orElse(BigDecimal.ZERO)
                                    .subtract(Optional.ofNullable(matchSaleVo.getSalesRefundAmount()).orElse(BigDecimal.ZERO)));
                        }
                    });
                }
            }
        });
        result.setRecords(saleVos);
        result.setPages(salePage.getPages());
        result.setSize(salePage.getSize());
        result.setTotal(saleVos.size());

        return Response.responseData(result);
    }

    @Override
    public Response<Page<ShipmentsDetailVO>> getShipmentsDetail(QueryShipmentsDetailDTO queryShipmentsDetailDTO) {
        var result = new Page<ShipmentsDetailVO>(queryShipmentsDetailDTO.getPage(), queryShipmentsDetailDTO.getPageSize());
        var retailDetailVos = new ArrayList<ShipmentsDetailVO>();
        var retailData = receiptRetailService.lambdaQuery()
                .eq(StringUtils.hasLength(queryShipmentsDetailDTO.getReceiptNumber()), ReceiptRetailMain::getReceiptNumber, queryShipmentsDetailDTO.getReceiptNumber())
                .eq(queryShipmentsDetailDTO.getOperatorId() != null, ReceiptRetailMain::getCreateBy, queryShipmentsDetailDTO.getOperatorId())
                .eq(queryShipmentsDetailDTO.getRelatedPersonId() != null, ReceiptRetailMain::getMemberId, queryShipmentsDetailDTO.getRelatedPersonId())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptRetailMain::getSubType, "零售出库")
                .like(StringUtils.hasLength(queryShipmentsDetailDTO.getRemark()), ReceiptRetailMain::getRemark, queryShipmentsDetailDTO.getRemark())
                .ge(queryShipmentsDetailDTO.getStartDate() != null, ReceiptRetailMain::getCreateTime, queryShipmentsDetailDTO.getStartDate())
                .le(queryShipmentsDetailDTO.getEndDate() != null, ReceiptRetailMain::getCreateTime, queryShipmentsDetailDTO.getEndDate())
                .list();

        var salesData = receiptSaleService.lambdaQuery()
                .eq(StringUtils.hasLength(queryShipmentsDetailDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, queryShipmentsDetailDTO.getReceiptNumber())
                .eq(queryShipmentsDetailDTO.getOperatorId() != null, ReceiptSaleMain::getCreateBy, queryShipmentsDetailDTO.getOperatorId())
                .eq(queryShipmentsDetailDTO.getRelatedPersonId() != null, ReceiptSaleMain::getCustomerId, queryShipmentsDetailDTO.getRelatedPersonId())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptSaleMain::getSubType, "销售出库")
                .like(StringUtils.hasLength(queryShipmentsDetailDTO.getRemark()), ReceiptSaleMain::getRemark, queryShipmentsDetailDTO.getRemark())
                .ge(queryShipmentsDetailDTO.getStartDate() != null, ReceiptSaleMain::getCreateTime, queryShipmentsDetailDTO.getStartDate())
                .le(queryShipmentsDetailDTO.getEndDate() != null, ReceiptSaleMain::getCreateTime, queryShipmentsDetailDTO.getEndDate())
                .list();

        var purchaseData = receiptPurchaseService.lambdaQuery()
                .eq(StringUtils.hasLength(queryShipmentsDetailDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryShipmentsDetailDTO.getReceiptNumber())
                .eq(queryShipmentsDetailDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryShipmentsDetailDTO.getOperatorId())
                .eq(queryShipmentsDetailDTO.getRelatedPersonId() != null, ReceiptPurchaseMain::getSupplierId, queryShipmentsDetailDTO.getRelatedPersonId())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptPurchaseMain::getSubType, "采购退货")
                .like(StringUtils.hasLength(queryShipmentsDetailDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryShipmentsDetailDTO.getRemark())
                .ge(queryShipmentsDetailDTO.getStartDate() != null, ReceiptPurchaseMain::getCreateTime, queryShipmentsDetailDTO.getStartDate())
                .le(queryShipmentsDetailDTO.getEndDate() != null, ReceiptPurchaseMain::getCreateTime, queryShipmentsDetailDTO.getEndDate())
                .list();

        if (!retailData.isEmpty()) {
            var retailSubs = receiptRetailSubService.lambdaQuery()
                    .in(ReceiptRetailSub::getReceiptMainId, retailData.stream().map(ReceiptRetailMain::getId).toList())
                    .eq(queryShipmentsDetailDTO.getWarehouseId() != null, ReceiptRetailSub::getWarehouseId, queryShipmentsDetailDTO.getWarehouseId())
                    .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptRetailSub retailSubData : retailSubs) {
                var product = productService.getById(retailSubData.getProductId());
                var retailDetailVo = ShipmentsDetailVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productUnit(product.getProductUnit())
                        .productBarcode(retailSubData.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(retailSubData.getWarehouseId()))
                        .productNumber(retailSubData.getProductNumber())
                        .unitPrice(retailSubData.getUnitPrice())
                        .amount(retailSubData.getTotalAmount())
                        .taxRate(BigDecimal.ZERO)
                        .taxAmount(BigDecimal.ZERO)
                        .build();

                for (ReceiptRetailMain retail : retailData) {
                    if (retail.getId().equals(retailSubData.getReceiptMainId())) {
                        retailDetailVo.setReceiptNumber(retail.getReceiptNumber());
                        retailDetailVo.setCreateTime(retail.getCreateTime());
                        retailDetailVo.setType("会员");
                        retailDetailVo.setName(commonService.getMemberName(retail.getMemberId()));
                    }
                }
                retailDetailVos.add(retailDetailVo);
            }
        }

        if (!salesData.isEmpty()) {
            var salesSubs = receiptSaleSubService.lambdaQuery()
                    .in(ReceiptSaleSub::getReceiptSaleMainId, salesData.stream().map(ReceiptSaleMain::getId).toList())
                    .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptSaleSub salesSubData : salesSubs) {
                var product = productService.getById(salesSubData.getProductId());
                var saleDetailVo = ShipmentsDetailVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productUnit(product.getProductUnit())
                        .productBarcode(salesSubData.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(salesSubData.getWarehouseId()))
                        .productNumber(salesSubData.getProductNumber())
                        .unitPrice(salesSubData.getUnitPrice())
                        .amount(salesSubData.getTotalAmount())
                        .taxRate(salesSubData.getTaxRate())
                        .taxAmount(salesSubData.getTaxAmount())
                        .build();

                for (ReceiptSaleMain sale : salesData) {
                    if (sale.getId().equals(salesSubData.getReceiptSaleMainId())) {
                        saleDetailVo.setReceiptNumber(sale.getReceiptNumber());
                        saleDetailVo.setCreateTime(sale.getCreateTime());
                        saleDetailVo.setType("客户");
                        saleDetailVo.setName(commonService.getCustomerName(sale.getCustomerId()));
                    }
                }
                retailDetailVos.add(saleDetailVo);
            }
        }
        if (!purchaseData.isEmpty()) {
            var purchaseSubs = receiptPurchaseSubService.lambdaQuery()
                    .in(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseData.stream().map(ReceiptPurchaseMain::getId).toList())
                    .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptPurchaseSub purchaseSubData : purchaseSubs) {
                var product = productService.getById(purchaseSubData.getProductId());
                var purchaseDetailVo = ShipmentsDetailVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productUnit(product.getProductUnit())
                        .productBarcode(purchaseSubData.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(purchaseSubData.getWarehouseId()))
                        .productNumber(purchaseSubData.getProductNumber())
                        .unitPrice(purchaseSubData.getUnitPrice())
                        .amount(purchaseSubData.getTotalAmount())
                        .taxRate(purchaseSubData.getTaxRate())
                        .taxAmount(purchaseSubData.getTaxAmount())
                        .build();

                for (ReceiptPurchaseMain purchase : purchaseData) {
                    if (purchase.getId().equals(purchaseSubData.getReceiptPurchaseMainId())) {
                        purchaseDetailVo.setReceiptNumber(purchase.getReceiptNumber());
                        purchaseDetailVo.setCreateTime(purchase.getCreateTime());
                        purchaseDetailVo.setType("供应商");
                        purchaseDetailVo.setName(commonService.getSupplierName(purchase.getSupplierId()));
                    }
                }
                retailDetailVos.add(purchaseDetailVo);
            }
        }
        // 进行手动分页
        int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
        int endIndex = (int) Math.min(startIndex + result.getSize(), retailDetailVos.size());
        startIndex = Math.min(startIndex, endIndex);

        List<ShipmentsDetailVO> pageRetailDetailVos = new ArrayList<>(retailDetailVos.subList(startIndex, endIndex));
        result.setRecords(pageRetailDetailVos);
        result.setTotal(retailDetailVos.size());
        return Response.responseData(result);
    }

    @Override
    public Response<Page<StorageDetailVO>> getStorageDetail(QueryStorageDetailDTO queryStorageDetailDTO) {
        var result = new Page<StorageDetailVO>(queryStorageDetailDTO.getPage(), queryStorageDetailDTO.getPageSize());
        var storageDetailVos = new ArrayList<StorageDetailVO>();
        var retailData = receiptRetailService.lambdaQuery()
                .eq(StringUtils.hasLength(queryStorageDetailDTO.getReceiptNumber()), ReceiptRetailMain::getReceiptNumber, queryStorageDetailDTO.getReceiptNumber())
                .eq(queryStorageDetailDTO.getOperatorId() != null, ReceiptRetailMain::getCreateBy, queryStorageDetailDTO.getOperatorId())
                .eq(queryStorageDetailDTO.getRelatedPersonId() != null, ReceiptRetailMain::getMemberId, queryStorageDetailDTO.getRelatedPersonId())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptRetailMain::getSubType, "零售退货")
                .like(StringUtils.hasLength(queryStorageDetailDTO.getRemark()), ReceiptRetailMain::getRemark, queryStorageDetailDTO.getRemark())
                .ge(queryStorageDetailDTO.getStartDate() != null, ReceiptRetailMain::getCreateTime, queryStorageDetailDTO.getStartDate())
                .le(queryStorageDetailDTO.getEndDate() != null, ReceiptRetailMain::getCreateTime, queryStorageDetailDTO.getEndDate())
                .list();

        var salesData = receiptSaleService.lambdaQuery()
                .eq(StringUtils.hasLength(queryStorageDetailDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, queryStorageDetailDTO.getReceiptNumber())
                .eq(queryStorageDetailDTO.getOperatorId() != null, ReceiptSaleMain::getCreateBy, queryStorageDetailDTO.getOperatorId())
                .eq(queryStorageDetailDTO.getRelatedPersonId() != null, ReceiptSaleMain::getCustomerId, queryStorageDetailDTO.getRelatedPersonId())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptSaleMain::getSubType, "销售退货")
                .like(StringUtils.hasLength(queryStorageDetailDTO.getRemark()), ReceiptSaleMain::getRemark, queryStorageDetailDTO.getRemark())
                .ge(queryStorageDetailDTO.getStartDate() != null, ReceiptSaleMain::getCreateTime, queryStorageDetailDTO.getStartDate())
                .le(queryStorageDetailDTO.getEndDate() != null, ReceiptSaleMain::getCreateTime, queryStorageDetailDTO.getEndDate())
                .list();

        var purchaseData = receiptPurchaseService.lambdaQuery()
                .eq(StringUtils.hasLength(queryStorageDetailDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryStorageDetailDTO.getReceiptNumber())
                .eq(queryStorageDetailDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryStorageDetailDTO.getOperatorId())
                .eq(queryStorageDetailDTO.getRelatedPersonId() != null, ReceiptPurchaseMain::getSupplierId, queryStorageDetailDTO.getRelatedPersonId())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptPurchaseMain::getSubType, "采购入库")
                .like(StringUtils.hasLength(queryStorageDetailDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryStorageDetailDTO.getRemark())
                .ge(queryStorageDetailDTO.getStartDate() != null, ReceiptPurchaseMain::getCreateTime, queryStorageDetailDTO.getStartDate())
                .le(queryStorageDetailDTO.getEndDate() != null, ReceiptPurchaseMain::getCreateTime, queryStorageDetailDTO.getEndDate())
                .list();

        if (!retailData.isEmpty()) {
            var retailSubs = receiptRetailSubService.lambdaQuery()
                    .in(ReceiptRetailSub::getReceiptMainId, retailData.stream().map(ReceiptRetailMain::getId).toList())
                    .eq(queryStorageDetailDTO.getWarehouseId() != null, ReceiptRetailSub::getWarehouseId, queryStorageDetailDTO.getWarehouseId())
                    .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptRetailSub retailSubData : retailSubs) {
                var product = productService.getById(retailSubData.getProductId());
                var retailDetailVo = StorageDetailVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productUnit(product.getProductUnit())
                        .productBarcode(retailSubData.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(retailSubData.getWarehouseId()))
                        .productNumber(retailSubData.getProductNumber())
                        .unitPrice(retailSubData.getUnitPrice())
                        .amount(retailSubData.getTotalAmount())
                        .taxRate(BigDecimal.ZERO)
                        .taxAmount(BigDecimal.ZERO)
                        .build();

                for (ReceiptRetailMain retail : retailData) {
                    if (retail.getId().equals(retailSubData.getReceiptMainId())) {
                        retailDetailVo.setReceiptNumber(retail.getReceiptNumber());
                        retailDetailVo.setCreateTime(retail.getCreateTime());
                        retailDetailVo.setType("会员");
                        retailDetailVo.setName(commonService.getMemberName(retail.getMemberId()));
                    }
                }
                storageDetailVos.add(retailDetailVo);
            }
        }

        if (!salesData.isEmpty()) {
            var salesSubs = receiptSaleSubService.lambdaQuery()
                    .in(ReceiptSaleSub::getReceiptSaleMainId, salesData.stream().map(ReceiptSaleMain::getId).toList())
                    .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptSaleSub salesSubData : salesSubs) {
                var product = productService.getById(salesSubData.getProductId());
                var saleDetailVo = StorageDetailVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productUnit(product.getProductUnit())
                        .productBarcode(salesSubData.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(salesSubData.getWarehouseId()))
                        .productNumber(salesSubData.getProductNumber())
                        .unitPrice(salesSubData.getUnitPrice())
                        .amount(salesSubData.getTotalAmount())
                        .taxRate(salesSubData.getTaxRate())
                        .taxAmount(salesSubData.getTaxAmount())
                        .build();

                for (ReceiptSaleMain sale : salesData) {
                    if (sale.getId().equals(salesSubData.getReceiptSaleMainId())) {
                        saleDetailVo.setReceiptNumber(sale.getReceiptNumber());
                        saleDetailVo.setCreateTime(sale.getCreateTime());
                        saleDetailVo.setType("客户");
                        saleDetailVo.setName(commonService.getCustomerName(sale.getCustomerId()));
                    }
                }
                storageDetailVos.add(saleDetailVo);
            }
        }
        if (!purchaseData.isEmpty()) {
            var purchaseSubs = receiptPurchaseSubService.lambdaQuery()
                    .in(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseData.stream().map(ReceiptPurchaseMain::getId).toList())
                    .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptPurchaseSub purchaseSubData : purchaseSubs) {
                var product = productService.getById(purchaseSubData.getProductId());
                var purchaseDetailVo = StorageDetailVO.builder()
                        .productName(product.getProductName())
                        .productStandard(product.getProductStandard())
                        .productModel(product.getProductModel())
                        .productUnit(product.getProductUnit())
                        .productBarcode(purchaseSubData.getProductBarcode())
                        .warehouseName(commonService.getWarehouseName(purchaseSubData.getWarehouseId()))
                        .productNumber(purchaseSubData.getProductNumber())
                        .unitPrice(purchaseSubData.getUnitPrice())
                        .amount(purchaseSubData.getTotalAmount())
                        .taxRate(purchaseSubData.getTaxRate())
                        .taxAmount(purchaseSubData.getTaxAmount())
                        .build();

                for (ReceiptPurchaseMain purchase : purchaseData) {
                    if (purchase.getId().equals(purchaseSubData.getReceiptPurchaseMainId())) {
                        purchaseDetailVo.setReceiptNumber(purchase.getReceiptNumber());
                        purchaseDetailVo.setCreateTime(purchase.getCreateTime());
                        purchaseDetailVo.setType("供应商");
                        purchaseDetailVo.setName(commonService.getSupplierName(purchase.getSupplierId()));
                    }
                }
                storageDetailVos.add(purchaseDetailVo);
            }
        }
        // 进行手动分页
        int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
        int endIndex = (int) Math.min(startIndex + result.getSize(), storageDetailVos.size());
        startIndex = Math.min(startIndex, endIndex);

        List<StorageDetailVO> pageStorageDetailVos = new ArrayList<>(storageDetailVos.subList(startIndex, endIndex));
        result.setRecords(pageStorageDetailVos);
        result.setTotal(storageDetailVos.size());
        return Response.responseData(result);
    }

    @Override
    public Response<Page<ShipmentsSummaryVO>> getShipmentsSummary(QueryShipmentsSummaryDTO queryShipmentsSummaryDTO) {
        var result = new Page<ShipmentsSummaryVO>(queryShipmentsSummaryDTO.getPage(), queryShipmentsSummaryDTO.getPageSize());
        var shipmentsSummaryVos = new ArrayList<ShipmentsSummaryVO>();
        var retailData = receiptRetailService.lambdaQuery()
                .eq(queryShipmentsSummaryDTO.getRelatedPersonId() != null, ReceiptRetailMain::getMemberId, queryShipmentsSummaryDTO.getRelatedPersonId())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptRetailMain::getSubType, "零售出库")
                .ge(queryShipmentsSummaryDTO.getStartDate() != null, ReceiptRetailMain::getCreateTime, queryShipmentsSummaryDTO.getStartDate())
                .le(queryShipmentsSummaryDTO.getEndDate() != null, ReceiptRetailMain::getCreateTime, queryShipmentsSummaryDTO.getEndDate())
                .list();

        var salesData = receiptSaleService.lambdaQuery()
                .eq(queryShipmentsSummaryDTO.getRelatedPersonId() != null, ReceiptSaleMain::getCustomerId, queryShipmentsSummaryDTO.getRelatedPersonId())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptSaleMain::getSubType, "销售出库")
                .ge(queryShipmentsSummaryDTO.getStartDate() != null, ReceiptSaleMain::getCreateTime, queryShipmentsSummaryDTO.getStartDate())
                .le(queryShipmentsSummaryDTO.getEndDate() != null, ReceiptSaleMain::getCreateTime, queryShipmentsSummaryDTO.getEndDate())
                .list();

        var purchaseData = receiptPurchaseService.lambdaQuery()
                .eq(queryShipmentsSummaryDTO.getRelatedPersonId() != null, ReceiptPurchaseMain::getSupplierId, queryShipmentsSummaryDTO.getRelatedPersonId())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptPurchaseMain::getSubType, "采购退货")
                .ge(queryShipmentsSummaryDTO.getStartDate() != null, ReceiptPurchaseMain::getCreateTime, queryShipmentsSummaryDTO.getStartDate())
                .le(queryShipmentsSummaryDTO.getEndDate() != null, ReceiptPurchaseMain::getCreateTime, queryShipmentsSummaryDTO.getEndDate())
                .list();

        if (!retailData.isEmpty()) {
            var retailSubs = receiptRetailSubService.lambdaQuery()
                    .in(ReceiptRetailSub::getReceiptMainId, retailData.stream().map(ReceiptRetailMain::getId).toList())
                    .eq(queryShipmentsSummaryDTO.getWarehouseId() != null, ReceiptRetailSub::getWarehouseId, queryShipmentsSummaryDTO.getWarehouseId())
                    .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptRetailSub retailSubData : retailSubs) {
                if (shipmentsSummaryVos.stream().noneMatch(matchShipmentsSummaryVo -> retailSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                        && commonService.getWarehouseName(retailSubData.getWarehouseId())
                        .equals(matchShipmentsSummaryVo.getWarehouseName()))) {
                    var product = productService.getById(retailSubData.getProductId());

                    var shipmentsSummaryVo = ShipmentsSummaryVO.builder()
                            .productBarcode(retailSubData.getProductBarcode())
                            .warehouseName(commonService.getWarehouseName(retailSubData.getWarehouseId()))
                            .productName(product.getProductName())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productUnit(product.getProductUnit())
                            .productCategoryName(commonService.getProductCategoryName(product.getProductCategoryId()))
                            .shipmentsAmount(retailSubData.getTotalAmount())
                            .shipmentsNumber(retailSubData.getProductNumber())
                            .createTime(retailSubData.getCreateTime())
                            .build();
                    shipmentsSummaryVos.add(shipmentsSummaryVo);
                } else {
                    shipmentsSummaryVos.forEach(matchShipmentsSummaryVo -> {
                        if (retailSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                                && commonService.getWarehouseName(retailSubData.getWarehouseId())
                                .equals(matchShipmentsSummaryVo.getWarehouseName())) {
                            matchShipmentsSummaryVo.setShipmentsAmount(Optional.ofNullable(matchShipmentsSummaryVo.getShipmentsAmount()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(retailSubData.getTotalAmount()).orElse(BigDecimal.ZERO)));
                            matchShipmentsSummaryVo.setShipmentsNumber(Optional.ofNullable(matchShipmentsSummaryVo.getShipmentsNumber()).orElse(0)
                                    + Optional.ofNullable(retailSubData.getProductNumber()).orElse(0));
                        }
                    });
                }
            }
        }

        if (!salesData.isEmpty()) {
            var salesSubs = receiptSaleSubService.lambdaQuery()
                    .in(ReceiptSaleSub::getReceiptSaleMainId, salesData.stream().map(ReceiptSaleMain::getId).toList())
                    .eq(queryShipmentsSummaryDTO.getWarehouseId() != null, ReceiptSaleSub::getWarehouseId, queryShipmentsSummaryDTO.getWarehouseId())
                    .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            for (ReceiptSaleSub salesSubData : salesSubs) {
                if (shipmentsSummaryVos.stream().noneMatch(matchShipmentsSummaryVo -> salesSubData.getProductBarcode()
                        .equals(matchShipmentsSummaryVo.getProductBarcode())
                        && commonService.getWarehouseName(salesSubData.getWarehouseId())
                        .equals(matchShipmentsSummaryVo.getWarehouseName()))) {

                    var product = productService.getById(salesSubData.getProductId());
                    var shipmentsSummaryVo = ShipmentsSummaryVO.builder()
                            .productBarcode(salesSubData.getProductBarcode())
                            .warehouseName(commonService.getWarehouseName(salesSubData.getWarehouseId()))
                            .productName(product.getProductName())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productUnit(product.getProductUnit())
                            .productCategoryName(commonService.getProductCategoryName(product.getProductCategoryId()))
                            .shipmentsAmount(salesSubData.getTotalAmount())
                            .shipmentsNumber(salesSubData.getProductNumber())
                            .createTime(salesSubData.getCreateTime())
                            .build();
                    shipmentsSummaryVos.add(shipmentsSummaryVo);
                } else {
                    shipmentsSummaryVos.forEach(matchShipmentsSummaryVo -> {
                        if (salesSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                                && commonService.getWarehouseName(salesSubData.getWarehouseId())
                                .equals(matchShipmentsSummaryVo.getWarehouseName())) {
                            matchShipmentsSummaryVo.setShipmentsAmount(Optional.ofNullable(matchShipmentsSummaryVo.getShipmentsAmount()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(salesSubData.getTotalAmount()).orElse(BigDecimal.ZERO)));
                            matchShipmentsSummaryVo.setShipmentsNumber(Optional.ofNullable(matchShipmentsSummaryVo.getShipmentsNumber()).orElse(0)
                                    + Optional.ofNullable(salesSubData.getProductNumber()).orElse(0));
                        }
                    });
                }
            }
        }
        if (!purchaseData.isEmpty()) {
            var purchaseSubs = receiptPurchaseSubService.lambdaQuery()
                    .in(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseData.stream().map(ReceiptPurchaseMain::getId).toList())
                    .eq(queryShipmentsSummaryDTO.getWarehouseId() != null, ReceiptPurchaseSub::getWarehouseId, queryShipmentsSummaryDTO.getWarehouseId())
                    .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptPurchaseSub purchaseSubData : purchaseSubs) {
                if (shipmentsSummaryVos.stream().noneMatch(matchShipmentsSummaryVo -> purchaseSubData.getProductBarcode()
                        .equals(matchShipmentsSummaryVo.getProductBarcode())
                        && commonService.getWarehouseName(purchaseSubData.getWarehouseId())
                        .equals(matchShipmentsSummaryVo.getWarehouseName()))) {

                    var product = productService.getById(purchaseSubData.getProductId());
                    var shipmentsSummaryVo = ShipmentsSummaryVO.builder()
                            .productBarcode(purchaseSubData.getProductBarcode())
                            .warehouseName(commonService.getWarehouseName(purchaseSubData.getWarehouseId()))
                            .productName(product.getProductName())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productUnit(product.getProductUnit())
                            .productCategoryName(commonService.getProductCategoryName(product.getProductCategoryId()))
                            .shipmentsAmount(purchaseSubData.getTotalAmount())
                            .shipmentsNumber(purchaseSubData.getProductNumber())
                            .createTime(purchaseSubData.getCreateTime())
                            .build();
                    shipmentsSummaryVos.add(shipmentsSummaryVo);
                } else {
                    shipmentsSummaryVos.forEach(matchShipmentsSummaryVo -> {
                        if (purchaseSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                                && commonService.getWarehouseName(purchaseSubData.getWarehouseId())
                                .equals(matchShipmentsSummaryVo.getWarehouseName())) {
                            matchShipmentsSummaryVo.setShipmentsAmount(Optional.ofNullable(matchShipmentsSummaryVo.getShipmentsAmount()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(purchaseSubData.getTotalAmount()).orElse(BigDecimal.ZERO)));
                            matchShipmentsSummaryVo.setShipmentsNumber(Optional.ofNullable(matchShipmentsSummaryVo.getShipmentsNumber()).orElse(0)
                                    + Optional.ofNullable(purchaseSubData.getProductNumber()).orElse(0));
                        }
                    });
                }
            }
        }
        // 进行手动分页
        int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
        int endIndex = (int) Math.min(startIndex + result.getSize(), shipmentsSummaryVos.size());
        startIndex = Math.min(startIndex, endIndex);

        List<ShipmentsSummaryVO> shipmentsSummaryVOS = new ArrayList<>(shipmentsSummaryVos.subList(startIndex, endIndex));
        result.setRecords(shipmentsSummaryVOS);
        result.setTotal(shipmentsSummaryVos.size());
        return Response.responseData(result);
    }

    @Override
    public Response<Page<StorageSummaryVO>> getStorageSummary(QueryStorageSummaryDTO queryStorageSummaryDTO) {
        var result = new Page<StorageSummaryVO>(queryStorageSummaryDTO.getPage(), queryStorageSummaryDTO.getPageSize());
        var storageSummaryVos = new ArrayList<StorageSummaryVO>();
        var retailData = receiptRetailService.lambdaQuery()
                .eq(queryStorageSummaryDTO.getRelatedPersonId() != null, ReceiptRetailMain::getMemberId, queryStorageSummaryDTO.getRelatedPersonId())
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptRetailMain::getSubType, "零售退货")
                .ge(queryStorageSummaryDTO.getStartDate() != null, ReceiptRetailMain::getCreateTime, queryStorageSummaryDTO.getStartDate())
                .le(queryStorageSummaryDTO.getEndDate() != null, ReceiptRetailMain::getCreateTime, queryStorageSummaryDTO.getEndDate())
                .list();

        var salesData = receiptSaleService.lambdaQuery()
                .eq(queryStorageSummaryDTO.getRelatedPersonId() != null, ReceiptSaleMain::getCustomerId, queryStorageSummaryDTO.getRelatedPersonId())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptSaleMain::getSubType, "销售退货")
                .ge(queryStorageSummaryDTO.getStartDate() != null, ReceiptSaleMain::getCreateTime, queryStorageSummaryDTO.getStartDate())
                .le(queryStorageSummaryDTO.getEndDate() != null, ReceiptSaleMain::getCreateTime, queryStorageSummaryDTO.getEndDate())
                .list();

        var purchaseData = receiptPurchaseService.lambdaQuery()
                .eq(queryStorageSummaryDTO.getRelatedPersonId() != null, ReceiptPurchaseMain::getSupplierId, queryStorageSummaryDTO.getRelatedPersonId())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptPurchaseMain::getSubType, "采购入库")
                .ge(queryStorageSummaryDTO.getStartDate() != null, ReceiptPurchaseMain::getCreateTime, queryStorageSummaryDTO.getStartDate())
                .le(queryStorageSummaryDTO.getEndDate() != null, ReceiptPurchaseMain::getCreateTime, queryStorageSummaryDTO.getEndDate())
                .list();

        if (!retailData.isEmpty()) {
            var retailSubs = receiptRetailSubService.lambdaQuery()
                    .in(ReceiptRetailSub::getReceiptMainId, retailData.stream().map(ReceiptRetailMain::getId).toList())
                    .eq(queryStorageSummaryDTO.getWarehouseId() != null, ReceiptRetailSub::getWarehouseId, queryStorageSummaryDTO.getWarehouseId())
                    .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptRetailSub retailSubData : retailSubs) {
                if (storageSummaryVos.stream().noneMatch(matchShipmentsSummaryVo -> retailSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                        && commonService.getWarehouseName(retailSubData.getWarehouseId())
                        .equals(matchShipmentsSummaryVo.getWarehouseName()))) {
                    var product = productService.getById(retailSubData.getProductId());

                    var storageSummaryVo = StorageSummaryVO.builder()
                            .productBarcode(retailSubData.getProductBarcode())
                            .warehouseName(commonService.getWarehouseName(retailSubData.getWarehouseId()))
                            .productName(product.getProductName())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productUnit(product.getProductUnit())
                            .productCategoryName(commonService.getProductCategoryName(product.getProductCategoryId()))
                            .storageAmount(retailSubData.getTotalAmount())
                            .storageNumber(retailSubData.getProductNumber())
                            .createTime(retailSubData.getCreateTime())
                            .build();
                    storageSummaryVos.add(storageSummaryVo);
                } else {
                    storageSummaryVos.forEach(matchShipmentsSummaryVo -> {
                        if (retailSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                                && commonService.getWarehouseName(retailSubData.getWarehouseId())
                                .equals(matchShipmentsSummaryVo.getWarehouseName())) {
                            matchShipmentsSummaryVo.setStorageAmount(Optional.ofNullable(matchShipmentsSummaryVo.getStorageAmount()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(retailSubData.getTotalAmount()).orElse(BigDecimal.ZERO)));
                            matchShipmentsSummaryVo.setStorageNumber(Optional.ofNullable(matchShipmentsSummaryVo.getStorageNumber()).orElse(0)
                                    + Optional.ofNullable(retailSubData.getProductNumber()).orElse(0));
                        }
                    });
                }
            }
        }

        if (!salesData.isEmpty()) {
            var salesSubs = receiptSaleSubService.lambdaQuery()
                    .in(ReceiptSaleSub::getReceiptSaleMainId, salesData.stream().map(ReceiptSaleMain::getId).toList())
                    .eq(queryStorageSummaryDTO.getWarehouseId() != null, ReceiptSaleSub::getWarehouseId, queryStorageSummaryDTO.getWarehouseId())
                    .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();

            for (ReceiptSaleSub salesSubData : salesSubs) {
                if (storageSummaryVos.stream().noneMatch(matchShipmentsSummaryVo -> salesSubData.getProductBarcode()
                        .equals(matchShipmentsSummaryVo.getProductBarcode())
                        && commonService.getWarehouseName(salesSubData.getWarehouseId())
                        .equals(matchShipmentsSummaryVo.getWarehouseName()))) {

                    var product = productService.getById(salesSubData.getProductId());
                    var storageSummaryVo = StorageSummaryVO.builder()
                            .productBarcode(salesSubData.getProductBarcode())
                            .warehouseName(commonService.getWarehouseName(salesSubData.getWarehouseId()))
                            .productName(product.getProductName())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productUnit(product.getProductUnit())
                            .productCategoryName(commonService.getProductCategoryName(product.getProductCategoryId()))
                            .storageAmount(salesSubData.getTotalAmount())
                            .storageNumber(salesSubData.getProductNumber())
                            .createTime(salesSubData.getCreateTime())
                            .build();
                    storageSummaryVos.add(storageSummaryVo);
                } else {
                    storageSummaryVos.forEach(matchShipmentsSummaryVo -> {
                        if (salesSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                                && commonService.getWarehouseName(salesSubData.getWarehouseId())
                                .equals(matchShipmentsSummaryVo.getWarehouseName())) {
                            matchShipmentsSummaryVo.setStorageAmount(Optional.ofNullable(matchShipmentsSummaryVo.getStorageAmount()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(salesSubData.getTotalAmount()).orElse(BigDecimal.ZERO)));
                            matchShipmentsSummaryVo.setStorageNumber(Optional.ofNullable(matchShipmentsSummaryVo.getStorageNumber()).orElse(0)
                                    + Optional.ofNullable(salesSubData.getProductNumber()).orElse(0));
                        }
                    });
                }
            }
        }
        if (!purchaseData.isEmpty()) {
            var purchaseSubs = receiptPurchaseSubService.lambdaQuery()
                    .in(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseData.stream().map(ReceiptPurchaseMain::getId).toList())
                    .eq(queryStorageSummaryDTO.getWarehouseId() != null, ReceiptPurchaseSub::getWarehouseId, queryStorageSummaryDTO.getWarehouseId())
                    .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            for (ReceiptPurchaseSub purchaseSubData : purchaseSubs) {
                if (storageSummaryVos.stream().noneMatch(matchShipmentsSummaryVo -> purchaseSubData.getProductBarcode()
                        .equals(matchShipmentsSummaryVo.getProductBarcode())
                        && commonService.getWarehouseName(purchaseSubData.getWarehouseId())
                        .equals(matchShipmentsSummaryVo.getWarehouseName()))) {

                    var product = productService.getById(purchaseSubData.getProductId());
                    var storageSummaryVo = StorageSummaryVO.builder()
                            .productBarcode(purchaseSubData.getProductBarcode())
                            .warehouseName(commonService.getWarehouseName(purchaseSubData.getWarehouseId()))
                            .productName(product.getProductName())
                            .productStandard(product.getProductStandard())
                            .productModel(product.getProductModel())
                            .productUnit(product.getProductUnit())
                            .productCategoryName(commonService.getProductCategoryName(product.getProductCategoryId()))
                            .storageAmount(purchaseSubData.getTotalAmount())
                            .storageNumber(purchaseSubData.getProductNumber())
                            .createTime(purchaseSubData.getCreateTime())
                            .build();
                    storageSummaryVos.add(storageSummaryVo);
                } else {
                    storageSummaryVos.forEach(matchShipmentsSummaryVo -> {
                        if (purchaseSubData.getProductBarcode().equals(matchShipmentsSummaryVo.getProductBarcode())
                                && commonService.getWarehouseName(purchaseSubData.getWarehouseId())
                                .equals(matchShipmentsSummaryVo.getWarehouseName())) {
                            matchShipmentsSummaryVo.setStorageAmount(Optional.ofNullable(matchShipmentsSummaryVo.getStorageAmount()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(purchaseSubData.getTotalAmount()).orElse(BigDecimal.ZERO)));
                            matchShipmentsSummaryVo.setStorageNumber(Optional.ofNullable(matchShipmentsSummaryVo.getStorageNumber()).orElse(0)
                                    + Optional.ofNullable(purchaseSubData.getProductNumber()).orElse(0));
                        }
                    });
                }
            }
        }
        // 进行手动分页
        int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
        int endIndex = (int) Math.min(startIndex + result.getSize(), storageSummaryVos.size());
        startIndex = Math.min(startIndex, endIndex);

        List<StorageSummaryVO> storageSummaryVOS = new ArrayList<>(storageSummaryVos.subList(startIndex, endIndex));
        result.setRecords(storageSummaryVOS);
        result.setTotal(storageSummaryVos.size());
        return Response.responseData(result);
    }

    @Override
    public Response<List<RelatedPersonVO>> getRelatedPerson() {
        var members = memberService.list();
        var customers = customerService.list();
        var suppliers = supplierService.list();

        var result = new ArrayList<RelatedPersonVO>();
        if (!members.isEmpty()) {
            for (Member member : members) {
                var personVO = RelatedPersonVO.builder()
                        .id(member.getId())
                        .type("会员")
                        .name(member.getMemberName() + "[会员]")
                        .build();
                result.add(personVO);
            }
        }
        if (!customers.isEmpty()) {
            for (Customer customer : customers) {
                var personVO = RelatedPersonVO.builder()
                        .id(customer.getId())
                        .type("客户")
                        .name(customer.getCustomerName() + "[客户]")
                        .build();
                result.add(personVO);
            }
        }
        if (!suppliers.isEmpty()) {
            for (Supplier supplier : suppliers) {
                var personVO = RelatedPersonVO.builder()
                        .id(supplier.getId())
                        .type("供应商")
                        .name(supplier.getSupplierName() + "[供应商]")
                        .build();
                result.add(personVO);
            }
        }
        return Response.responseData(result);
    }

    @Override
    public Response<Page<CustomerBillVO>> getCustomerBill(QueryCustomerBillDTO queryCustomerBillDTO) {
        var page = new Page<ReceiptSaleMain>(queryCustomerBillDTO.getPage(), queryCustomerBillDTO.getPageSize());
        var queryData = receiptSaleService.lambdaQuery()
                .eq(queryCustomerBillDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, queryCustomerBillDTO.getCustomerId())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(queryCustomerBillDTO.getStartDate() != null, ReceiptSaleMain::getReceiptDate, queryCustomerBillDTO.getStartDate())
                .le(queryCustomerBillDTO.getEndDate() != null, ReceiptSaleMain::getReceiptDate, queryCustomerBillDTO.getEndDate())
                .page(page);

        var result = new Page<CustomerBillVO>();
        if (!queryData.getRecords().isEmpty()) {
            var customerBillVos = new ArrayList<CustomerBillVO>();
            for (ReceiptSaleMain record : queryData.getRecords()) {
                var customer = customerService.getById(record.getCustomerId());
                var customerBillVo = CustomerBillVO.builder()
                        .customerId(record.getCustomerId())
                        .customerName(customer.getCustomerName())
                        .contactName(customer.getContact())
                        .contactPhone(customer.getPhoneNumber())
                        .email(customer.getEmail())
                        .firstQuarterReceivable(customer.getFirstQuarterAccountReceivable())
                        .secondQuarterReceivable(customer.getSecondQuarterAccountReceivable())
                        .thirdQuarterReceivable(customer.getThirdQuarterAccountReceivable())
                        .fourthQuarterReceivable(customer.getFourthQuarterAccountReceivable())
                        .totalQuarterArrears(record.getArrearsAmount())
                        .totalQuarterReceivable(record.getDiscountLastAmount())
                        .build();

                // 添加前判断是否已经存在 存在则进行累加 否则直接添加
                if (customerBillVos.stream().anyMatch(matchCustomerBillVo -> matchCustomerBillVo.getCustomerId().equals(record.getCustomerId()))) {
                    customerBillVos.forEach(matchCustomerBillVo -> {
                        if (matchCustomerBillVo.getCustomerId().equals(record.getCustomerId())) {
                            matchCustomerBillVo.setFirstQuarterReceivable(Optional.ofNullable(matchCustomerBillVo.getFirstQuarterReceivable()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(customerBillVo.getFirstQuarterReceivable()).orElse(BigDecimal.ZERO)));
                            matchCustomerBillVo.setSecondQuarterReceivable(Optional.ofNullable(matchCustomerBillVo.getSecondQuarterReceivable()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(customerBillVo.getSecondQuarterReceivable()).orElse(BigDecimal.ZERO)));
                            matchCustomerBillVo.setThirdQuarterReceivable(Optional.ofNullable(matchCustomerBillVo.getThirdQuarterReceivable()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(customerBillVo.getThirdQuarterReceivable()).orElse(BigDecimal.ZERO)));
                            matchCustomerBillVo.setFourthQuarterReceivable(Optional.ofNullable(matchCustomerBillVo.getFourthQuarterReceivable()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(customerBillVo.getFourthQuarterReceivable()).orElse(BigDecimal.ZERO)));
                            matchCustomerBillVo.setTotalQuarterArrears(Optional.ofNullable(matchCustomerBillVo.getTotalQuarterArrears()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(customerBillVo.getTotalQuarterArrears()).orElse(BigDecimal.ZERO)));
                            matchCustomerBillVo.setTotalQuarterReceivable(Optional.ofNullable(matchCustomerBillVo.getTotalQuarterReceivable()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(customerBillVo.getTotalQuarterReceivable()).orElse(BigDecimal.ZERO)));
                        }
                    });
                } else {
                    customerBillVos.add(customerBillVo);
                }
            }
            // 遍历customerBillVos 计算出remainingReceivableArrears金额 = 4个季度+totalQuarterArrears
            for (CustomerBillVO customerBillVo : customerBillVos) {
                customerBillVo.setRemainingReceivableArrears(Optional.ofNullable(customerBillVo.getFirstQuarterReceivable()).orElse(BigDecimal.ZERO)
                        .add(Optional.ofNullable(customerBillVo.getSecondQuarterReceivable()).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(customerBillVo.getThirdQuarterReceivable()).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(customerBillVo.getFourthQuarterReceivable()).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(customerBillVo.getTotalQuarterArrears()).orElse(BigDecimal.ZERO)));
            }

            // 进行手动分页
            int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
            int endIndex = (int) Math.min(startIndex + result.getSize(), customerBillVos.size());
            startIndex = Math.min(startIndex, endIndex);

            List<CustomerBillVO> customerBillVOS = new ArrayList<>(customerBillVos.subList(startIndex, endIndex));
            result.setRecords(customerBillVOS);
            result.setTotal(queryData.getTotal());
            return Response.responseData(result);
        }

        return Response.responseData(result);
    }

    @Override
    public Response<Page<CustomerBillDetailVO>> getCustomerBillDetail(QueryCustomerBillDetailDTO queryCustomerBillDetailDTO) {
        var page = new Page<ReceiptSaleMain>(queryCustomerBillDetailDTO.getPage(), queryCustomerBillDetailDTO.getPageSize());

        var queryDataPage = receiptSaleService.lambdaQuery()
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptSaleMain::getCustomerId, queryCustomerBillDetailDTO.getCustomerId())
                .in(ReceiptSaleMain::getSubType, "销售出库", "销售退货")
                .eq(StringUtils.hasLength(queryCustomerBillDetailDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, queryCustomerBillDetailDTO.getReceiptNumber())
                .ge(queryCustomerBillDetailDTO.getStartDate() != null, ReceiptSaleMain::getReceiptDate, queryCustomerBillDetailDTO.getStartDate())
                .le(queryCustomerBillDetailDTO.getEndDate() != null, ReceiptSaleMain::getReceiptDate, queryCustomerBillDetailDTO.getEndDate())
                .page(page);

        var result = new Page<CustomerBillDetailVO>();
        if (!queryDataPage.getRecords().isEmpty()) {
            var customerBillDetailVos = new ArrayList<CustomerBillDetailVO>();
            for (ReceiptSaleMain record : queryDataPage.getRecords()) {
                var operatorName = "";
                var operator = userService.getById(record.getCreateBy());
                if (operator != null) {
                    operatorName = operator.getName();
                }
                var customerBillVo = CustomerBillDetailVO.builder()
                        .customerName(commonService.getCustomerName(record.getCustomerId()))
                        .receiptNumber(record.getReceiptNumber())
                        .receiptDate(record.getReceiptDate())
                        .operator(operatorName)
                        .thisReceiptArrears(record.getArrearsAmount())
                        .build();

                var receiptSaleSub = receiptSaleSubService.lambdaQuery()
                        .eq(ReceiptSaleSub::getReceiptSaleMainId, record.getId())
                        .eq(ReceiptSaleSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                        .list();
                var productInfo = "";
                if(!receiptSaleSub.isEmpty()) {
                    var productIds = receiptSaleSub.stream().map(ReceiptSaleSub::getProductId).toList();
                    var products = productService.lambdaQuery()
                            .in(Product::getId, productIds)
                            .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED)
                            .like(StringUtils.hasLength(queryCustomerBillDetailDTO.getProductInfo()), Product::getProductName, queryCustomerBillDetailDTO.getProductInfo())
                            .list();
                    if (!products.isEmpty()) {
                        productInfo = products.stream().map(Product::getProductName).collect(Collectors.joining("|"));
                    }
                }
                customerBillVo.setProductInfo(productInfo);
                var queryWrapper = new QueryWrapper<FinancialMain>();
                queryWrapper.eq("related_person_id", record.getCustomerId());
                queryWrapper.eq("type", "收款");
                var financialMains = financialMainMapper.selectList(queryWrapper);
                if (!financialMains.isEmpty()) {
                    var financialSubs = financialSubMapper.selectList(new QueryWrapper<FinancialSub>()
                            .in("financial_main_id",
                                    financialMains.stream().map(FinancialMain::getId).toList()));
                    if(!financialSubs.isEmpty()) {
                        var totalAmount = BigDecimal.ZERO;
                        for (FinancialSub customerPayment : financialSubs) {
                            if(customerPayment.getOtherReceipt().equals(record.getReceiptNumber())) {
                                totalAmount = customerPayment.getReceivedPrepaidArrears();
                            }
                            customerBillVo.setReceivedArrears(totalAmount);
                            // 计算待收欠款 = 本次欠款 - 已收欠款
                            customerBillVo.setReceivableArrears(record.getArrearsAmount().subtract(totalAmount));
                        }
                    } else {
                        customerBillVo.setReceivedArrears(BigDecimal.ZERO);
                        customerBillVo.setReceivableArrears(record.getArrearsAmount());
                    }
                } else {
                    customerBillVo.setReceivedArrears(BigDecimal.ZERO);
                    customerBillVo.setReceivableArrears(record.getArrearsAmount());
                }

                customerBillDetailVos.add(customerBillVo);
            }

            result.setRecords(customerBillDetailVos);
            result.setTotal(queryDataPage.getTotal());
        }
        return Response.responseData(result);
    }

    @Override
    public Response<Page<SupplierBillVO>> getSupplierBill(QuerySupplierBillDTO querySupplierBillDTO) {
        var page = new Page<ReceiptPurchaseMain>(querySupplierBillDTO.getPage(), querySupplierBillDTO.getPageSize());
        var queryData = receiptPurchaseService.lambdaQuery()
                .eq(querySupplierBillDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, querySupplierBillDTO.getSupplierId())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(querySupplierBillDTO.getStartDate() != null, ReceiptPurchaseMain::getReceiptDate, querySupplierBillDTO.getStartDate())
                .le(querySupplierBillDTO.getEndDate() != null, ReceiptPurchaseMain::getReceiptDate, querySupplierBillDTO.getEndDate())
                .page(page);

        var result = new Page<SupplierBillVO>();
        if (!queryData.getRecords().isEmpty()) {
            var supplierBillVos = new ArrayList<SupplierBillVO>();
            for (ReceiptPurchaseMain record : queryData.getRecords()) {
                var supplier = supplierService.getById(record.getSupplierId());
                var supplierBillVo = SupplierBillVO.builder()
                        .supplierId(supplier.getId())
                        .supplierName(supplier.getSupplierName())
                        .contactName(supplier.getContact())
                        .contactPhone(supplier.getPhoneNumber())
                        .email(supplier.getEmail())
                        .firstQuarterPayment(supplier.getFirstQuarterAccountPayment())
                        .secondQuarterPayment(supplier.getSecondQuarterAccountPayment())
                        .thirdQuarterPayment(supplier.getThirdQuarterAccountPayment())
                        .fourthQuarterPayment(supplier.getFourthQuarterAccountPayment())
                        .totalPayment(record.getDiscountLastAmount())
                        .totalArrears(record.getArrearsAmount())
                        .build();

                // 添加前判断是否已经存在 存在则进行累加 否则直接添加
                if (supplierBillVos.stream().anyMatch(matchSupplierBillVo -> matchSupplierBillVo.getSupplierId().equals(record.getSupplierId()))) {
                    supplierBillVos.forEach(matchSupplierBillVo -> {
                        if (matchSupplierBillVo.getSupplierId().equals(record.getSupplierId())) {
                            matchSupplierBillVo.setFirstQuarterPayment(Optional.ofNullable(matchSupplierBillVo.getFirstQuarterPayment()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(supplierBillVo.getFirstQuarterPayment()).orElse(BigDecimal.ZERO)));
                            matchSupplierBillVo.setSecondQuarterPayment(Optional.ofNullable(matchSupplierBillVo.getSecondQuarterPayment()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(supplierBillVo.getSecondQuarterPayment()).orElse(BigDecimal.ZERO)));
                            matchSupplierBillVo.setThirdQuarterPayment(Optional.ofNullable(matchSupplierBillVo.getThirdQuarterPayment()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(supplierBillVo.getThirdQuarterPayment()).orElse(BigDecimal.ZERO)));
                            matchSupplierBillVo.setFourthQuarterPayment(Optional.ofNullable(matchSupplierBillVo.getFourthQuarterPayment()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(supplierBillVo.getFourthQuarterPayment()).orElse(BigDecimal.ZERO)));
                            matchSupplierBillVo.setTotalPayment(Optional.ofNullable(matchSupplierBillVo.getTotalPayment()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(supplierBillVo.getTotalPayment()).orElse(BigDecimal.ZERO)));
                            matchSupplierBillVo.setTotalArrears(Optional.ofNullable(matchSupplierBillVo.getTotalArrears()).orElse(BigDecimal.ZERO)
                                    .add(Optional.ofNullable(supplierBillVo.getTotalArrears()).orElse(BigDecimal.ZERO)));
                        }
                    });
                } else {
                    supplierBillVos.add(supplierBillVo);
                }
            }
            for (SupplierBillVO supplierBillVO : supplierBillVos) {
                supplierBillVO.setRemainingPaymentArrears(Optional.ofNullable(supplierBillVO.getFirstQuarterPayment()).orElse(BigDecimal.ZERO)
                        .add(Optional.ofNullable(supplierBillVO.getSecondQuarterPayment()).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(supplierBillVO.getThirdQuarterPayment()).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(supplierBillVO.getFourthQuarterPayment()).orElse(BigDecimal.ZERO))
                        .add(Optional.ofNullable(supplierBillVO.getTotalArrears()).orElse(BigDecimal.ZERO)));
            }

            // 进行手动分页
            int startIndex = (int) ((result.getCurrent() - 1) * result.getSize());
            int endIndex = (int) Math.min(startIndex + result.getSize(), supplierBillVos.size());
            startIndex = Math.min(startIndex, endIndex);

            List<SupplierBillVO> supplierBillVOS = new ArrayList<>(supplierBillVos.subList(startIndex, endIndex));
            result.setRecords(supplierBillVOS);
            result.setTotal(queryData.getTotal());
            return Response.responseData(result);
        }

        return Response.responseData(result);
    }

    @Override
    public Response<Page<SupplierBillDetailVO>> getSupplierBillDetail(QuerySupplierBillDetailDTO querySupplierBillDetailDTO) {
        var page = new Page<ReceiptPurchaseMain>(querySupplierBillDetailDTO.getPage(), querySupplierBillDetailDTO.getPageSize());

        var queryDataPage = receiptPurchaseService.lambdaQuery()
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(ReceiptPurchaseMain::getSupplierId, querySupplierBillDetailDTO.getSupplierId())
                .in(ReceiptPurchaseMain::getSubType, "采购入库", "采购退货")
                .eq(StringUtils.hasLength(querySupplierBillDetailDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, querySupplierBillDetailDTO.getReceiptNumber())
                .ge(querySupplierBillDetailDTO.getStartDate() != null, ReceiptPurchaseMain::getReceiptDate, querySupplierBillDetailDTO.getStartDate())
                .le(querySupplierBillDetailDTO.getEndDate() != null, ReceiptPurchaseMain::getReceiptDate, querySupplierBillDetailDTO.getEndDate())
                .page(page);

        var result = new Page<SupplierBillDetailVO>();
        if (!queryDataPage.getRecords().isEmpty()) {
            var supplierBillDetailVos = new ArrayList<SupplierBillDetailVO>();
            for (ReceiptPurchaseMain record : queryDataPage.getRecords()) {
                var operatorName = "";
                var operator = userService.getById(record.getCreateBy());
                if (operator != null) {
                    operatorName = operator.getName();
                }
                var supplierBillVo = SupplierBillDetailVO.builder()
                        .supplierName(commonService.getSupplierName(record.getSupplierId()))
                        .receiptNumber(record.getReceiptNumber())
                        .receiptDate(record.getReceiptDate())
                        .operator(operatorName)
                        .thisReceiptArrears(record.getArrearsAmount())
                        .build();

                var receiptPurchaseSub = receiptPurchaseSubService.lambdaQuery()
                        .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, record.getId())
                        .eq(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                        .list();
                var productInfo = "";
                if(!receiptPurchaseSub.isEmpty()) {
                    var productIds = receiptPurchaseSub.stream().map(ReceiptPurchaseSub::getProductId).toList();
                    var products = productService.lambdaQuery()
                            .in(Product::getId, productIds)
                            .eq(Product::getDeleteFlag, CommonConstants.NOT_DELETED)
                            .like(StringUtils.hasLength(querySupplierBillDetailDTO.getProductInfo()), Product::getProductName, querySupplierBillDetailDTO.getProductInfo())
                            .list();
                    if (!products.isEmpty()) {
                        productInfo = products.stream().map(Product::getProductName).collect(Collectors.joining("|"));
                    }
                }
                supplierBillVo.setProductInfo(productInfo);
                var queryWrapper = new QueryWrapper<FinancialMain>();
                queryWrapper.eq("related_person_id", record.getSupplierId());
                queryWrapper.eq("type", "付款");
                var financialMains = financialMainMapper.selectList(queryWrapper);
                if (!financialMains.isEmpty()) {
                    var financialSubs = financialSubMapper.selectList(new QueryWrapper<FinancialSub>()
                            .in("financial_main_id",
                                    financialMains.stream().map(FinancialMain::getId).toList()));
                    if(!financialSubs.isEmpty()) {
                        var totalAmount = BigDecimal.ZERO;
                        for (FinancialSub purchasePayment : financialSubs) {
                            if(purchasePayment.getOtherReceipt().equals(record.getReceiptNumber())) {
                                totalAmount = purchasePayment.getReceivedPrepaidArrears();
                            }
                            supplierBillVo.setPrepaidArrears(totalAmount);
                            // 计算待付欠款 = 本次付款 - 已付欠款
                            supplierBillVo.setPaymentArrears(record.getArrearsAmount().subtract(totalAmount));
                        }
                    } else {
                        supplierBillVo.setPrepaidArrears(BigDecimal.ZERO);
                        supplierBillVo.setPaymentArrears(record.getArrearsAmount());
                    }
                } else {
                    supplierBillVo.setPrepaidArrears(BigDecimal.ZERO);
                    supplierBillVo.setPaymentArrears(record.getArrearsAmount());
                }

                supplierBillDetailVos.add(supplierBillVo);
            }

            result.setRecords(supplierBillDetailVos);
            result.setTotal(queryDataPage.getTotal());
        }
        return Response.responseData(result);
    }
}
