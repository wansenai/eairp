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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.SalesDataBO;
import com.wansenai.dto.receipt.sale.*;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.entities.product.ProductStock;
import com.wansenai.entities.receipt.ReceiptSaleMain;
import com.wansenai.entities.receipt.ReceiptSaleSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.mappers.receipt.ReceiptSaleMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.CustomerService;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.CollectionReceiptService;
import com.wansenai.service.financial.FinancialSubService;
import com.wansenai.service.financial.IFinancialAccountService;
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
import com.wansenai.vo.receipt.sale.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReceiptSaleServiceImpl extends ServiceImpl<ReceiptSaleMainMapper, ReceiptSaleMain> implements ReceiptSaleService {
    
    private final ReceiptSaleMainMapper receiptSaleMainMapper;
    private final CustomerService customerService;
    private final ISysUserService userService;
    private final ReceiptSaleSubService receiptSaleSubService;
    private final SysFileMapper fileMapper;
    private final CommonService commonService;
    private final ProductStockMapper productStockMapper;
    private final IFinancialAccountService accountService;
    private final CollectionReceiptService collectionReceiptService;
    private final FinancialSubService financialSubService;

    public ReceiptSaleServiceImpl(ReceiptSaleMainMapper receiptSaleMainMapper, CustomerService customerService, ISysUserService userService, ReceiptSaleSubService receiptSaleSubService, SysFileMapper fileMapper, CommonService commonService, ProductStockMapper productStockMapper, IFinancialAccountService accountService, CollectionReceiptService collectionReceiptService, FinancialSubService financialSubService) {
        this.receiptSaleMainMapper = receiptSaleMainMapper;
        this.customerService = customerService;
        this.userService = userService;
        this.receiptSaleSubService = receiptSaleSubService;
        this.fileMapper = fileMapper;
        this.commonService = commonService;
        this.productStockMapper = productStockMapper;
        this.accountService = accountService;
        this.collectionReceiptService = collectionReceiptService;
        this.financialSubService = financialSubService;
    }

    private final Map<Long, List<ReceiptSaleSub>> receiptSubListCache = new ConcurrentHashMap<>();

    private List<ReceiptSaleSub> getReceiptSubList(Long receiptSaleMainId) {
        return receiptSubListCache.computeIfAbsent(receiptSaleMainId, id ->
                receiptSaleSubService.lambdaQuery()
                        .eq(ReceiptSaleSub::getReceiptSaleMainId, id)
                        .list()
        );
    }

    private BigDecimal calculateTotalAmount(List<ReceiptSaleSub> subList, Function<ReceiptSaleSub, BigDecimal> mapper) {
        return subList.stream()
                .map(mapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateArrearsAmount(List<FinancialSub> subList, Function<FinancialSub, BigDecimal> mapper) {
        return subList.stream()
                .map(mapper.andThen(bd -> bd != null ? bd : BigDecimal.ZERO)) // 在这里添加空值检查
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int calculateProductNumber(List<ReceiptSaleSub> subList) {
        return subList.stream()
                .mapToInt(ReceiptSaleSub::getProductNumber)
                .sum();
    }

    private String getCustomerName(Long customerId) {
        return (customerId != null) ? customerService.getById(customerId).getCustomerName() : null;
    }

    private String getWarehouseName(Long warehouseId) {
        return (warehouseId != null) ? commonService.getWarehouseName(warehouseId) : null;
    }

    private String getUserName(Long userId) {
        return (userId != null) ? userService.getById(userId).getName() : null;
    }

    private List<Long> parseAndCollectLongList(String input) {
        if (StringUtils.hasLength(input)) {
            return Arrays.stream(input.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private SalesDataBO createSalesDataFromReceiptSub(ReceiptSaleSub item) {
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

        var data = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
        if (data != null) {
            saleData.setWarehouseId(data.getWarehouseId());
            saleData.setProductName(data.getProductName());
            saleData.setProductStandard(data.getProductStandard());
            saleData.setProductColor(data.getProductColor());
            saleData.setProductModel(data.getProductModel());
            saleData.setProductUnit(data.getProductUnit());
            saleData.setStock(data.getStock());

            if(saleData.getWarehouseId() != null) {
                saleData.setWarehouseName(getWarehouseName(saleData.getWarehouseId()));
            }
        }
        return saleData;
    }

    private String parseIdsToString(List<Long> ids) {
        return (ids != null && !ids.isEmpty()) ? ids.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
    }

    private ArrayList<Long> processFiles(List<FileDataBO> files, Long saleId) {
        var userId = userService.getCurrentUserId();
        var fid = new ArrayList<Long>();
        if (!files.isEmpty()) {
            var receiptMain = getById(saleId);
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

    private Response<String> deleteSale(List<Long> ids, SaleCodeEnum successEnum, SaleCodeEnum errorEnum) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateStatusResult = lambdaUpdate()
                .in(ReceiptSaleMain::getId, ids)
                .set(ReceiptSaleMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        var updateSubResult = receiptSaleSubService.lambdaUpdate()
                .in(ReceiptSaleSub::getReceiptSaleMainId, ids)
                .set(ReceiptSaleSub::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateStatusResult &&updateSubResult) {
            return Response.responseMsg(successEnum);
        } else {
            return Response.responseMsg(errorEnum);
        }
    }

    private Response<String> updateSaleStatus(List<Long> ids, Integer status, SaleCodeEnum successEnum, SaleCodeEnum errorEnum) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptSaleMain::getId, ids)
                .set(ReceiptSaleMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(successEnum);
        } else {
            return Response.responseMsg(errorEnum);
        }
    }

    private void updateProductStock(List<ReceiptSaleSub> receiptSaleSubList, int stockType) {
        var stockMap = new ConcurrentHashMap<Long, Integer>();

        receiptSaleSubList.forEach(item -> {
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
        receiptSaleSubList.forEach(item2 -> {
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
            var receiptSubList = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);

            var customerName = getCustomerName(item.getCustomerId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptSaleSub::getTotalAmount);
            var taxRateTotalPrice = calculateTotalAmount(receiptSubList, ReceiptSaleSub::getTaxIncludedAmount);

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
        return buildSaleOrderDetailResponse(sale);
    }

    @Override
    public Response<SaleOrderDetailVO> getLinkSaleOrderDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = lambdaQuery()
                .eq(ReceiptSaleMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        return buildSaleOrderDetailResponse(sale);
    }

    private Response<SaleOrderDetailVO> buildSaleOrderDetailResponse(ReceiptSaleMain sale) {
        List<FileDataBO> fileList = commonService.getFileList(sale.getFileId());

        var receiptSubList = receiptSaleSubService.lambdaQuery()
                .eq(ReceiptSaleSub::getReceiptSaleMainId, sale.getId())
                .list();

        var tableData = receiptSubList.stream()
                .map(this::createSalesDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));

        var operatorIds = parseAndCollectLongList(sale.getOperatorId());
        var multipleAccountIds = parseAndCollectLongList(sale.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(sale.getMultipleAccountAmount());

        var accountName = "";
        if(!multipleAccountIds.isEmpty() && !multipleAccountAmounts.isEmpty()) {
            var accountNameList = new ArrayList<String>();
            for (int i = 0; i < multipleAccountIds.size(); i++) {
                var account = accountService.getById(multipleAccountIds.get(i));
                var accountAmount = multipleAccountAmounts.get(i);
                accountNameList.add(account.getAccountName() + "(" + accountAmount + "元)");
            }
            accountName = StringUtils.collectionToCommaDelimitedString(accountNameList);
        } else {
            var account = accountService.getById(sale.getAccountId());
            if (account != null) {
                accountName = account.getAccountName();
            }
        }

        var saleOrderDetailVO = SaleOrderDetailVO.builder()
                .receiptNumber(sale.getReceiptNumber())
                .receiptDate(sale.getReceiptDate())
                .customerId(sale.getCustomerId())
                .customerName(getCustomerName(sale.getCustomerId()))
                .accountId(sale.getAccountId())
                .accountName(accountName)
                .operatorIds(operatorIds)
                .discountRate(sale.getDiscountRate())
                .discountAmount(sale.getDiscountAmount())
                .discountLastAmount(sale.getDiscountLastAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .otherReceipt(sale.getOtherReceipt())
                .deposit(sale.getDeposit())
                .remark(sale.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(saleOrderDetailVO);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateSaleOrder(SaleOrderDTO saleOrderDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = saleOrderDTO.getId() != null;

        var operatorIds = parseIdsToString(saleOrderDTO.getOperatorIds());
        var multipleAccountIds = parseIdsToString(saleOrderDTO.getMultipleAccountIds());
        var multipleAccountAmounts = parseIdsToString(saleOrderDTO.getMultipleAccountAmounts());
        var accountId = (saleOrderDTO.getAccountId() != null) ? String.valueOf(saleOrderDTO.getAccountId()) : null;
        var fid = processFiles(saleOrderDTO.getFiles(), saleOrderDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

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
                    .set(!operatorIds.isEmpty(), ReceiptSaleMain::getOperatorId, operatorIds)
                    .set(ReceiptSaleMain::getUpdateBy, userId)
                    .set(ReceiptSaleMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSaleSubService.lambdaUpdate()
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

            var updateSubResult = receiptSaleSubService.saveBatch(receiptList);

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
                    .multipleAccount(multipleAccountIds)
                    .multipleAccountAmount(multipleAccountAmounts)
                    .remark(saleOrderDTO.getRemark())
                    .fileId(fileIds)
                    .status(saleOrderDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
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

            var saveSubResult = receiptSaleSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_ORDER_ERROR);
            }
        }
    }

    @Override
    public Response<String> deleteSaleOrder(List<Long> ids) {
        return deleteSale(ids, SaleCodeEnum.DELETE_SALE_ORDER_SUCCESS, SaleCodeEnum.DELETE_SALE_ORDER_ERROR);

    }

    @Override
    public Response<String> updateSaleOrderStatus(List<Long> ids, Integer status) {
        return updateSaleStatus(ids, status, SaleCodeEnum.UPDATE_SALE_ORDER_SUCCESS, SaleCodeEnum.UPDATE_SALE_ORDER_ERROR);
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
            var receiptSubList = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var customerName = getCustomerName(item.getCustomerId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptSaleSub::getTotalAmount);
            var taxRateTotalPrice = calculateTotalAmount(receiptSubList, ReceiptSaleSub::getTaxIncludedAmount);
            // 计算代收金额 = 本次欠款 + 本次收款
            var totalCollectAmount = item.getArrearsAmount().add(item.getChangeAmount());

            var saleShipmentVO = SaleShipmentsVO.builder()
                    .id(item.getId())
                    .customerName(customerName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxIncludedAmount(taxRateTotalPrice)
                    .totalCollectAmount(totalCollectAmount)
                    .thisCollectAmount(item.getChangeAmount())
                    .thisArrearsAmount(item.getArrearsAmount())
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
    public Response<SaleShipmentsDetailVO> getSaleShipmentsDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = getById(id);
        return buildSaleShipmentsDetailResponse(sale);
    }

    @Override
    public Response<SaleShipmentsDetailVO> getLinkSaleShipmentsDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = lambdaQuery()
                .eq(ReceiptSaleMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        return buildSaleShipmentsDetailResponse(sale);
    }

    private Response<SaleShipmentsDetailVO> buildSaleShipmentsDetailResponse(ReceiptSaleMain sale) {
        List<FileDataBO> fileList = commonService.getFileList(sale.getFileId());

        var receiptSaleSubs = receiptSaleSubService.lambdaQuery()
                .eq(ReceiptSaleSub::getReceiptSaleMainId, sale.getId())
                .list();

        var tableData = receiptSaleSubs.stream()
                .map(this::createSalesDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));

        var operatorIds = parseAndCollectLongList(sale.getOperatorId());
        var multipleAccountIds = parseAndCollectLongList(sale.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(sale.getMultipleAccountAmount());

        var accountName = "";
        if(!multipleAccountIds.isEmpty() && !multipleAccountAmounts.isEmpty()) {
            var accountNameList = new ArrayList<String>();
            for (int i = 0; i < multipleAccountIds.size(); i++) {
                var account = accountService.getById(multipleAccountIds.get(i));
                var accountAmount = multipleAccountAmounts.get(i);
                accountNameList.add(account.getAccountName() + "(" + accountAmount + "元)");
            }
            accountName = StringUtils.collectionToCommaDelimitedString(accountNameList);
        } else {
            var account = accountService.getById(sale.getAccountId());
            if (account != null) {
                accountName = account.getAccountName();
            }
        }

        var saleShipmentsDetail = SaleShipmentsDetailVO.builder()
                .receiptNumber(sale.getReceiptNumber())
                .receiptDate(sale.getReceiptDate())
                .customerId(sale.getCustomerId())
                .customerName(getCustomerName(sale.getCustomerId()))
                .accountId(sale.getAccountId())
                .accountName(accountName)
                .operatorIds(operatorIds)
                .collectOfferRate(sale.getDiscountRate())
                .collectOfferAmount(sale.getDiscountAmount())
                .collectOfferLastAmount(sale.getDiscountLastAmount())
                .otherAmount(sale.getOtherAmount())
                .otherReceipt(sale.getOtherReceipt())
                .thisCollectAmount(sale.getChangeAmount())
                .thisArrearsAmount(sale.getArrearsAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .remark(sale.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(saleShipmentsDetail);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateSaleShipments(SaleShipmentsDTO shipmentsDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = shipmentsDTO.getId() != null;

        var operatorIds = parseIdsToString(shipmentsDTO.getOperatorIds());
        var multipleAccountIds = parseIdsToString(shipmentsDTO.getMultipleAccountIds());
        var multipleAccountAmounts = parseIdsToString(shipmentsDTO.getMultipleAccountAmounts());
        String accountId = (shipmentsDTO.getAccountId() != null) ? String.valueOf(shipmentsDTO.getAccountId()) : null;

        var fid = processFiles(shipmentsDTO.getFiles(), shipmentsDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

        if (isUpdate) {
            var beforeReceipt = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, shipmentsDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 1);
            }
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptSaleMain::getId, shipmentsDTO.getId())
                    .set(shipmentsDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, shipmentsDTO.getCustomerId())
                    .set(shipmentsDTO.getCollectOfferRate() != null, ReceiptSaleMain::getDiscountRate, shipmentsDTO.getCollectOfferRate())
                    .set(shipmentsDTO.getCollectOfferAmount() != null, ReceiptSaleMain::getDiscountAmount, shipmentsDTO.getCollectOfferAmount())
                    .set(shipmentsDTO.getCollectOfferLastAmount() != null, ReceiptSaleMain::getDiscountLastAmount, shipmentsDTO.getCollectOfferLastAmount())
                    .set(shipmentsDTO.getOtherAmount() != null, ReceiptSaleMain::getOtherAmount, shipmentsDTO.getOtherAmount())
                    .set(shipmentsDTO.getThisCollectAmount() != null, ReceiptSaleMain::getChangeAmount, shipmentsDTO.getThisCollectAmount())
                    .set(shipmentsDTO.getThisArrearsAmount() != null, ReceiptSaleMain::getArrearsAmount, shipmentsDTO.getThisArrearsAmount())
                    .set(shipmentsDTO.getStatus() != null, ReceiptSaleMain::getStatus, shipmentsDTO.getStatus())
                    .set(StringUtils.hasLength(shipmentsDTO.getOtherReceipt()), ReceiptSaleMain::getOtherReceipt, shipmentsDTO.getOtherReceipt())
                    .set(StringUtils.hasText(shipmentsDTO.getReceiptDate()), ReceiptSaleMain::getReceiptDate, shipmentsDTO.getReceiptDate())
                    .set(StringUtils.hasText(shipmentsDTO.getRemark()), ReceiptSaleMain::getRemark, shipmentsDTO.getRemark())
                    .set(ReceiptSaleMain::getAccountId, accountId)
                    .set(ReceiptSaleMain::getFileId, fileIds)
                    .set(ReceiptSaleMain::getMultipleAccount, String.valueOf(multipleAccountIds))
                    .set(ReceiptSaleMain::getMultipleAccountAmount, String.valueOf(multipleAccountAmounts))
                    .set(!operatorIds.isEmpty(), ReceiptSaleMain::getOperatorId, operatorIds)
                    .set(ReceiptSaleMain::getUpdateBy, userId)
                    .set(ReceiptSaleMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSaleSubService.lambdaUpdate()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, shipmentsDTO.getId())
                    .remove();

            var tableData = shipmentsDTO.getTableData();
            var receiptSaleList = tableData.stream()
                    .map(item -> ReceiptSaleSub.builder()
                            .receiptSaleMainId(shipmentsDTO.getId())
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
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptSaleSubService.saveBatch(receiptSaleList);
            shipmentsDTO.getTableData().forEach(item -> {
                updateProductStock(receiptSaleList, 2);
            });

            var account = accountService.getById(shipmentsDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var thisCollectAmount = shipmentsDTO.getThisCollectAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(ReceiptSaleSub::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.subtract(beforeChangeAmount);
                if (thisCollectAmount != null) {
                    accountBalance = accountBalance.add(thisCollectAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_SHIPMENTS_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var receiptSaleShipmentMain = ReceiptSaleMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_SHIPMENT)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_SALES_SHIPMENTS)
                    .initReceiptNumber(shipmentsDTO.getReceiptNumber())
                    .receiptNumber(shipmentsDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(shipmentsDTO.getReceiptDate()))
                    .customerId(shipmentsDTO.getCustomerId())
                    .operatorId(String.valueOf(operatorIds))
                    .accountId(shipmentsDTO.getAccountId())
                    .discountRate(shipmentsDTO.getCollectOfferRate())
                    .discountAmount(shipmentsDTO.getCollectOfferAmount())
                    .discountLastAmount(shipmentsDTO.getCollectOfferLastAmount())
                    .otherAmount(shipmentsDTO.getOtherAmount())
                    .otherReceipt(shipmentsDTO.getOtherReceipt())
                    .changeAmount(shipmentsDTO.getThisCollectAmount())
                    .arrearsAmount(shipmentsDTO.getThisArrearsAmount())
                    .multipleAccount(multipleAccountIds)
                    .multipleAccountAmount(multipleAccountAmounts)
                    .remark(shipmentsDTO.getRemark())
                    .fileId(fileIds)
                    .status(shipmentsDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(receiptSaleShipmentMain);

            var receiptSubList = shipmentsDTO.getTableData();
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

            var saveSubResult = receiptSaleSubService.saveBatch(receiptList);
            updateProductStock(receiptList, 2);

            var account = accountService.getById(shipmentsDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var changeAmount = shipmentsDTO.getThisCollectAmount();
                if (changeAmount != null) {
                    accountBalance = accountBalance.add(changeAmount);
                    account.setId(shipmentsDTO.getAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_SHIPMENTS_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_SHIPMENTS_ERROR);
            }
        }
    }

    @Override
    public Response<String> updateSaleShipmentsStatus(List<Long> ids, Integer status) {
        return updateSaleStatus(ids, status, SaleCodeEnum.UPDATE_SALE_SHIPMENTS_SUCCESS, SaleCodeEnum.UPDATE_SALE_SHIPMENTS_ERROR);

    }

    @Override
    public Response<String> deleteSaleShipments(List<Long> ids) {
        return deleteSale(ids, SaleCodeEnum.DELETE_SALE_SHIPMENTS_SUCCESS, SaleCodeEnum.DELETE_SALE_SHIPMENTS_ERROR);
    }

    @Override
    public Response<Page<SaleRefundVO>> getSaleRefundPage(QuerySaleRefundDTO refundDTO) {
        var result = new Page<SaleRefundVO>();
        var saleRefundVOList = new ArrayList<SaleRefundVO>();
        var page = new Page<ReceiptSaleMain>(refundDTO.getPage(), refundDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptSaleMain>()
                .eq(ReceiptSaleMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptSaleMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_SALES_REFUND)
                .eq(StringUtils.hasText(refundDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, refundDTO.getReceiptNumber())
                .like(StringUtils.hasText(refundDTO.getRemark()), ReceiptSaleMain::getRemark, refundDTO.getRemark())
                .eq(refundDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, refundDTO.getCustomerId())
                .eq(refundDTO.getOperatorId() != null, ReceiptSaleMain::getCreateBy, refundDTO.getOperatorId())
                .eq(refundDTO.getStatus() != null, ReceiptSaleMain::getStatus, refundDTO.getStatus())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(refundDTO.getStartDate()), ReceiptSaleMain::getCreateTime, refundDTO.getStartDate())
                .le(StringUtils.hasText(refundDTO.getEndDate()), ReceiptSaleMain::getCreateTime, refundDTO.getEndDate());

        var queryResult = receiptSaleMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var customerName = getCustomerName(item.getCustomerId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptSaleSub::getTotalAmount);
            var taxRateTotalPrice = calculateTotalAmount(receiptSubList, ReceiptSaleSub::getTaxIncludedAmount);

            var totalRefundAmount = item.getArrearsAmount().add(item.getChangeAmount());

            var saleRefundVO = SaleRefundVO.builder()
                    .id(item.getId())
                    .customerName(customerName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxIncludedAmount(taxRateTotalPrice)
                    .refundTotalAmount(totalRefundAmount)
                    .thisRefundAmount(item.getChangeAmount())
                    .thisArrearsAmount(item.getArrearsAmount())
                    .status(item.getStatus())
                    .build();
            saleRefundVOList.add(saleRefundVO);
        });
        result.setRecords(saleRefundVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public Response<SaleRefundDetailVO> getSaleRefundDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = getById(id);
        return getSaleRefundDetailResponse(sale);
    }

    @Override
    public Response<SaleRefundDetailVO> getLinkSaleRefundDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var sale = lambdaQuery()
                .eq(ReceiptSaleMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        return getSaleRefundDetailResponse(sale);
    }

    private Response<SaleRefundDetailVO> getSaleRefundDetailResponse(ReceiptSaleMain sale) {
        List<FileDataBO> fileList = commonService.getFileList(sale.getFileId());
        var receiptSaleSubs = receiptSaleSubService.lambdaQuery()
                .eq(ReceiptSaleSub::getReceiptSaleMainId, sale.getId())
                .list();

        var tableData = receiptSaleSubs.stream()
                .map(this::createSalesDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));

        var operatorIds = parseAndCollectLongList(sale.getOperatorId());
        var multipleAccountIds = parseAndCollectLongList(sale.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(sale.getMultipleAccountAmount());

        var accountName = "";
        if(!multipleAccountIds.isEmpty() && !multipleAccountAmounts.isEmpty()) {
            var accountNameList = new ArrayList<String>();
            for (int i = 0; i < multipleAccountIds.size(); i++) {
                var account = accountService.getById(multipleAccountIds.get(i));
                var accountAmount = multipleAccountAmounts.get(i);
                accountNameList.add(account.getAccountName() + "(" + accountAmount + "元)");
            }
            accountName = StringUtils.collectionToCommaDelimitedString(accountNameList);
        } else {
            var account = accountService.getById(sale.getAccountId());
            if (account != null) {
                accountName = account.getAccountName();
            }
        }

        var saleRefundDetail = SaleRefundDetailVO.builder()
                .receiptNumber(sale.getReceiptNumber())
                .receiptDate(sale.getReceiptDate())
                .customerId(sale.getCustomerId())
                .customerName(getCustomerName(sale.getCustomerId()))
                .accountId(sale.getAccountId())
                .accountName(accountName)
                .operatorIds(operatorIds)
                .refundOfferRate(sale.getDiscountRate())
                .refundOfferAmount(sale.getDiscountAmount())
                .refundLastAmount(sale.getDiscountLastAmount())
                .otherAmount(sale.getOtherAmount())
                .otherReceipt(sale.getOtherReceipt())
                .thisRefundAmount(sale.getChangeAmount())
                .thisArrearsAmount(sale.getArrearsAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .remark(sale.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(saleRefundDetail);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateSaleRefund(SaleRefundDTO refundDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = refundDTO.getId() != null;

        var operatorIds = parseIdsToString(refundDTO.getOperatorIds());
        var multipleAccountIds = parseIdsToString(refundDTO.getMultipleAccountIds());
        var multipleAccountAmounts = parseIdsToString(refundDTO.getMultipleAccountAmounts());
        String accountId = (refundDTO.getAccountId() != null) ? String.valueOf(refundDTO.getAccountId()) : null;

        var fid = processFiles(refundDTO.getFiles(), refundDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

        if (isUpdate) {
            var beforeReceipt = receiptSaleSubService.lambdaQuery()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, refundDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 2);
            }
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptSaleMain::getId, refundDTO.getId())
                    .set(refundDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, refundDTO.getCustomerId())
                    .set(refundDTO.getRefundOfferRate() != null, ReceiptSaleMain::getDiscountRate, refundDTO.getRefundOfferRate())
                    .set(refundDTO.getRefundOfferAmount() != null, ReceiptSaleMain::getDiscountAmount, refundDTO.getRefundOfferAmount())
                    .set(refundDTO.getRefundLastAmount() != null, ReceiptSaleMain::getDiscountLastAmount, refundDTO.getRefundLastAmount())
                    .set(refundDTO.getOtherAmount() != null, ReceiptSaleMain::getOtherAmount, refundDTO.getOtherAmount())
                    .set(refundDTO.getThisRefundAmount() != null, ReceiptSaleMain::getChangeAmount, refundDTO.getThisRefundAmount().negate())
                    .set(refundDTO.getThisArrearsAmount() != null, ReceiptSaleMain::getArrearsAmount, refundDTO.getThisArrearsAmount())
                    .set(refundDTO.getStatus() != null, ReceiptSaleMain::getStatus, refundDTO.getStatus())
                    .set(StringUtils.hasLength(refundDTO.getOtherReceipt()), ReceiptSaleMain::getOtherReceipt, refundDTO.getOtherReceipt())
                    .set(StringUtils.hasText(refundDTO.getReceiptDate()), ReceiptSaleMain::getReceiptDate, refundDTO.getReceiptDate())
                    .set(StringUtils.hasText(refundDTO.getRemark()), ReceiptSaleMain::getRemark, refundDTO.getRemark())
                    .set(ReceiptSaleMain::getAccountId, accountId)
                    .set(ReceiptSaleMain::getFileId, fileIds)
                    .set(ReceiptSaleMain::getMultipleAccount, String.valueOf(multipleAccountIds))
                    .set(ReceiptSaleMain::getMultipleAccountAmount, String.valueOf(multipleAccountAmounts))
                    .set(!operatorIds.isEmpty(), ReceiptSaleMain::getOperatorId, operatorIds)
                    .set(ReceiptSaleMain::getUpdateBy, userId)
                    .set(ReceiptSaleMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptSaleSubService.lambdaUpdate()
                    .eq(ReceiptSaleSub::getReceiptSaleMainId, refundDTO.getId())
                    .remove();

            var tableData = refundDTO.getTableData();
            var receiptSaleList = tableData.stream()
                    .map(item -> ReceiptSaleSub.builder()
                            .receiptSaleMainId(refundDTO.getId())
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
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptSaleSubService.saveBatch(receiptSaleList);
            refundDTO.getTableData().forEach(item -> {
                updateProductStock(receiptSaleList, 1);
            });

            var account = accountService.getById(refundDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var thisRefundAmount = refundDTO.getThisRefundAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(item -> item.getTotalAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (beforeChangeAmount != null) {
                    accountBalance = accountBalance.add(beforeChangeAmount);
                }
                if (thisRefundAmount != null) {
                    accountBalance = accountBalance.subtract(thisRefundAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.UPDATE_SALE_REFUND_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var receiptSaleShipmentMain = ReceiptSaleMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_STORAGE)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_SALES_REFUND)
                    .initReceiptNumber(refundDTO.getReceiptNumber())
                    .receiptNumber(refundDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(refundDTO.getReceiptDate()))
                    .customerId(refundDTO.getCustomerId())
                    .operatorId(String.valueOf(operatorIds))
                    .accountId(refundDTO.getAccountId())
                    .discountRate(refundDTO.getRefundOfferRate())
                    .discountAmount(refundDTO.getRefundOfferAmount())
                    .discountLastAmount(refundDTO.getRefundLastAmount())
                    .otherAmount(refundDTO.getOtherAmount())
                    .otherReceipt(refundDTO.getOtherReceipt())
                    .changeAmount(refundDTO.getThisRefundAmount().negate())
                    .arrearsAmount(refundDTO.getThisArrearsAmount())
                    .multipleAccount(multipleAccountIds)
                    .multipleAccountAmount(multipleAccountAmounts)
                    .remark(refundDTO.getRemark())
                    .fileId(fileIds)
                    .status(refundDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(receiptSaleShipmentMain);

            var receiptSubList = refundDTO.getTableData();
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

            var saveSubResult = receiptSaleSubService.saveBatch(receiptList);
            updateProductStock(receiptList,1);
            var account = accountService.getById(refundDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var thisRefundAmount = refundDTO.getThisRefundAmount();
                if (thisRefundAmount != null) {
                    accountBalance = accountBalance.subtract(thisRefundAmount);
                    account.setId(refundDTO.getAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(SaleCodeEnum.ADD_SALE_REFUND_ERROR);
            }
        }
    }

    @Override
    public Response<String> deleteSaleRefund(List<Long> ids) {
        return deleteSale(ids, SaleCodeEnum.DELETE_SALE_REFUND_SUCCESS, SaleCodeEnum.DELETE_SALE_REFUND_ERROR);
    }

    @Override
    public Response<String> updateSaleRefundStatus(List<Long> ids, Integer status) {
        return updateSaleStatus(ids, status, SaleCodeEnum.UPDATE_SALE_REFUND_SUCCESS, SaleCodeEnum.UPDATE_SALE_REFUND_ERROR);
    }

    @Override
    public Response<Page<SaleArrearsVO>> getSaleArrearsPage(QuerySaleArrearsDTO arrearsDTO) {
        var result = new Page<SaleArrearsVO>();
        var saleArrearsVOList = new ArrayList<SaleArrearsVO>();
        var page = new Page<ReceiptSaleMain>(arrearsDTO.getPage(), arrearsDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptSaleMain>()
                .eq(StringUtils.hasText(arrearsDTO.getReceiptNumber()), ReceiptSaleMain::getReceiptNumber, arrearsDTO.getReceiptNumber())
                .eq(arrearsDTO.getCustomerId() != null, ReceiptSaleMain::getCustomerId, arrearsDTO.getCustomerId())
                .eq(ReceiptSaleMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .gt(ReceiptSaleMain::getArrearsAmount, BigDecimal.ZERO)
                .ge(StringUtils.hasText(arrearsDTO.getStartDate()), ReceiptSaleMain::getCreateTime, arrearsDTO.getStartDate())
                .le(StringUtils.hasText(arrearsDTO.getEndDate()), ReceiptSaleMain::getCreateTime, arrearsDTO.getEndDate());

        var queryResult = receiptSaleMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var customerName = getCustomerName(item.getCustomerId());
            var operatorName = getUserName(item.getCreateBy());
            var financeMainList = collectionReceiptService.lambdaQuery()
                    .eq(FinancialMain::getRelatedPersonId, item.getCustomerId())
                    .eq(FinancialMain::getStatus, CommonConstants.NOT_DELETED)
                    .list();
            var saleArrearsVO = SaleArrearsVO.builder()
                    .id(item.getId())
                    .customerName(customerName)
                    .receiptDate(item.getReceiptDate())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(item.getRemark())
                    .operatorName(operatorName)
                    .thisReceiptArrears(item.getArrearsAmount())
                    .build();
            if(!financeMainList.isEmpty()) {

                for (FinancialMain financialMain : financeMainList) {
                    var financeSubList = financialSubService.lambdaQuery()
                            .eq(FinancialSub::getFinancialMainId, financialMain.getId())
                            .eq(FinancialSub::getOtherReceipt, item.getReceiptNumber())
                            .eq(FinancialSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                            .list();
                    var receivableArrears = calculateArrearsAmount(financeSubList, FinancialSub::getReceivablePaymentArrears);
                    var receivedArrears = calculateArrearsAmount(financeSubList, FinancialSub::getReceivedPrepaidArrears);
                    saleArrearsVO.setReceivedArrears(receivedArrears);
                    saleArrearsVO.setReceivableArrears(item.getArrearsAmount().subtract(receivedArrears));
                }
            }
            saleArrearsVOList.add(saleArrearsVO);
        });
        result.setRecords(saleArrearsVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }
}
