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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.receipt.QueryReceiptDTO;
import com.wansenai.dto.report.QueryAccountStatisticsDTO;
import com.wansenai.dto.report.QueryProductStockDTO;
import com.wansenai.dto.report.QueryStockFlowDTO;
import com.wansenai.entities.basic.Customer;
import com.wansenai.entities.basic.Member;
import com.wansenai.entities.basic.Supplier;
import com.wansenai.entities.financial.FinancialAccount;
import com.wansenai.entities.product.Product;
import com.wansenai.entities.receipt.*;
import com.wansenai.entities.warehouse.Warehouse;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.service.basic.CustomerService;
import com.wansenai.service.basic.MemberService;
import com.wansenai.service.basic.SupplierService;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.receipt.*;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.service.warehouse.WarehouseService;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.ReceiptDetailVO;
import com.wansenai.vo.receipt.ReceiptVO;
import com.wansenai.vo.receipt.retail.RetailStatisticalDataVO;
import com.wansenai.vo.report.AccountFlowVO;
import com.wansenai.vo.report.AccountStatisticsVO;
import com.wansenai.vo.report.ProductStockVO;
import com.wansenai.vo.report.StockFlowVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    private final WarehouseService warehouseService;

    private final IFinancialAccountService accountService;

    private final CommonService commonService;

    public ReceiptServiceImpl(ReceiptRetailService receiptRetailService, ReceiptRetailSubService receiptRetailSubService, ReceiptSaleService receiptSaleService, ReceiptSaleSubService receiptSaleSubService, ReceiptPurchaseService receiptPurchaseService, ReceiptPurchaseSubService receiptPurchaseSubService, MemberService memberService, CustomerService customerService, SupplierService supplierService, ISysUserService userService, ProductService productService, ProductStockMapper productStockMapper, WarehouseService warehouseService, IFinancialAccountService accountService, CommonService commonService) {
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
        this.warehouseService = warehouseService;
        this.accountService = accountService;
        this.commonService = commonService;
    }

    private String getProductName(Long id) {
        var product = productService.lambdaQuery()
                .eq(Product::getId, id)
                .one();
        if (product != null) {
            return product.getProductName();
        }
        return "";
    }

    private String getWarehouseName(Long id) {
        var warehouse = warehouseService.lambdaQuery()
                .eq(Warehouse::getId, id)
                .one();
        if (warehouse != null) {
            return warehouse.getWarehouseName();
        }
        return "";
    }

    @Override
    public Response<RetailStatisticalDataVO> getRetailStatistics() {
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
                        .productName(getProductName(item.getProductId()))
                        .warehouseName(getWarehouseName(item.getWarehouseId()))
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
                        .productName(getProductName(item.getProductId()))
                        .warehouseName(getWarehouseName(item.getWarehouseId()))
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
                        .productName(getProductName(item.getProductId()))
                        .warehouseName(getWarehouseName(item.getWarehouseId()))
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
}
