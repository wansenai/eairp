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
import com.wansenai.bo.PurchaseDataBO;
import com.wansenai.bo.PurchaseDataExportBO;
import com.wansenai.dto.receipt.purchase.*;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.entities.product.ProductStock;
import com.wansenai.entities.receipt.ReceiptPurchaseMain;
import com.wansenai.entities.receipt.ReceiptPurchaseSub;
import com.wansenai.entities.receipt.ReceiptSaleMain;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.mappers.receipt.ReceiptPurchaseMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.SupplierService;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.FinancialSubService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.financial.PaymentReceiptService;
import com.wansenai.service.receipt.ReceiptPurchaseService;
import com.wansenai.service.receipt.ReceiptPurchaseSubService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.PurchaseCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.purchase.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReceiptPurchaseServiceImpl extends ServiceImpl<ReceiptPurchaseMainMapper, ReceiptPurchaseMain> implements ReceiptPurchaseService {

    private final SysFileMapper fileMapper;
    private final CommonService commonService;
    private final ISysUserService userService;
    private final SupplierService supplierService;
    private final ReceiptPurchaseMainMapper receiptPurchaseMainMapper;
    private final ReceiptPurchaseSubService receiptPurchaseSubService;
    private final ProductStockMapper productStockMapper;
    private final IFinancialAccountService accountService;
    private final PaymentReceiptService paymentReceiptService;
    private final FinancialSubService financialSubService;

    public ReceiptPurchaseServiceImpl(SysFileMapper fileMapper , CommonService commonService, ISysUserService userService, SupplierService supplierService, ReceiptPurchaseMainMapper receiptPurchaseMainMapper, ReceiptPurchaseSubService receiptPurchaseSubService, ProductStockMapper productStockMapper, IFinancialAccountService accountService, PaymentReceiptService paymentReceiptService, FinancialSubService financialSubService) {
        this.fileMapper = fileMapper;
        this.commonService = commonService;
        this.userService = userService;
        this.supplierService = supplierService;
        this.receiptPurchaseMainMapper = receiptPurchaseMainMapper;
        this.receiptPurchaseSubService = receiptPurchaseSubService;
        this.productStockMapper = productStockMapper;
        this.accountService = accountService;
        this.paymentReceiptService = paymentReceiptService;
        this.financialSubService = financialSubService;
    }
    private final Map<Long, List<ReceiptPurchaseSub>> receiptSubListCache = new ConcurrentHashMap<>();

    private List<ReceiptPurchaseSub> getReceiptSubList (Long receiptPurchaseMainId) {
        return receiptSubListCache.computeIfAbsent(receiptPurchaseMainId, id ->
                receiptPurchaseSubService.lambdaQuery()
                        .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, id)
                        .list()
        );
    }

    private BigDecimal calculateTotalAmount(List<ReceiptPurchaseSub> subList, Function<ReceiptPurchaseSub, BigDecimal> mapper) {
        return subList.stream()
                .map(mapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int calculateProductNumber(List<ReceiptPurchaseSub> subList) {
        return subList.stream()
                .mapToInt(ReceiptPurchaseSub::getProductNumber)
                .sum();
    }

    private String getUserName(Long userId) {
        return (userId != null) ? userService.getById(userId).getName() : null;
    }

    private String getWarehouseName(Long warehouseId) {
        return (warehouseId != null) ? commonService.getWarehouseName(warehouseId) : null;
    }

    private List<Long> parseAndCollectLongList(String input) {
        if (StringUtils.hasLength(input)) {
            return Arrays.stream(input.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private PurchaseDataBO createPurchaseDataFromReceiptSub(ReceiptPurchaseSub item) {
        var purchaseData = PurchaseDataBO.builder()
                .productId(item.getProductId())
                .barCode(item.getProductBarcode())
                .productNumber(item.getProductNumber())
                .unitPrice(item.getUnitPrice())
                .amount(item.getTotalAmount())
                .taxRate(item.getTaxRate())
                .taxAmount(item.getTaxAmount())
                .taxTotalPrice(item.getTaxIncludedAmount())
                .warehouseId(item.getWarehouseId())
                .remark(item.getRemark())
                .build();

        var data = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
        if (data != null) {
            purchaseData.setProductName(data.getProductName());
            purchaseData.setProductModel(data.getProductModel());
            purchaseData.setProductStandard(data.getProductStandard());
            purchaseData.setProductColor(data.getProductColor());
            purchaseData.setProductUnit(data.getProductUnit());
            purchaseData.setProductStandard(data.getProductStandard());
            purchaseData.setStock(data.getStock());

            if(purchaseData.getWarehouseId() != null) {
                purchaseData.setWarehouseName(getWarehouseName(purchaseData.getWarehouseId()));
            }
        }
        return purchaseData;
    }

    private String parseIdsToString(List<Long> ids) {
        return (ids != null && !ids.isEmpty()) ? ids.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
    }

    private ArrayList<Long> processFiles(List<FileDataBO> files, Long purchaseId) {
        var userId = userService.getCurrentUserId();
        var fid = new ArrayList<Long>();
        if (!files.isEmpty()) {
            var receiptMain = getById(purchaseId);
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

    private Response<String> deletePurchase(List<Long> ids, PurchaseCodeEnum successEnum, PurchaseCodeEnum errorEnum) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateStatusResult = lambdaUpdate()
                .in(ReceiptPurchaseMain::getId, ids)
                .set(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        var updateSubResult = receiptPurchaseSubService.lambdaUpdate()
                .in(ReceiptPurchaseSub::getReceiptPurchaseMainId, ids)
                .set(ReceiptPurchaseSub::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateStatusResult &&updateSubResult) {
            return Response.responseMsg(successEnum);
        } else {
            return Response.responseMsg(errorEnum);
        }
    }

    private BigDecimal calculateArrearsAmount(List<FinancialSub> subList, Function<FinancialSub, BigDecimal> mapper) {
        return subList.stream()
                .map(mapper.andThen(bd -> bd != null ? bd : BigDecimal.ZERO)) // 在这里添加空值检查
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Response<String> updatePurchaseStatus(List<Long> ids, Integer status, PurchaseCodeEnum successEnum, PurchaseCodeEnum errorEnum) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptPurchaseMain::getId, ids)
                .set(ReceiptPurchaseMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(successEnum);
        } else {
            return Response.responseMsg(errorEnum);
        }
    }

    private void updateProductStock(List<ReceiptPurchaseSub> receiptSubList, int stockType) {
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
    public Response<Page<PurchaseOrderVO>> getPurchaseOrderPage(QueryPurchaseOrderDTO queryPurchaseOrderDTO) {
        var result = new Page<PurchaseOrderVO>();
        var purchaseOrderVOList = new ArrayList<PurchaseOrderVO>();
        var page = new Page<ReceiptPurchaseMain>(queryPurchaseOrderDTO.getPage(), queryPurchaseOrderDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_ORDER)
                .in(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_ORDER)
                .eq(StringUtils.hasText(queryPurchaseOrderDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryPurchaseOrderDTO.getReceiptNumber())
                .like(StringUtils.hasText(queryPurchaseOrderDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryPurchaseOrderDTO.getRemark())
                .eq(queryPurchaseOrderDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseOrderDTO.getSupplierId())
                .eq(queryPurchaseOrderDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryPurchaseOrderDTO.getOperatorId())
                .eq(queryPurchaseOrderDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, queryPurchaseOrderDTO.getStatus())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(queryPurchaseOrderDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseOrderDTO.getStartDate())
                .le(StringUtils.hasText(queryPurchaseOrderDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseOrderDTO.getEndDate())
                .orderByDesc(ReceiptPurchaseMain::getCreateTime);

        var queryResult = receiptPurchaseMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTotalAmount);
            var taxRateTotalPrice = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTaxIncludedAmount);

            var purchaseOrderVO = PurchaseOrderVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxRateTotalAmount(taxRateTotalPrice)
                    .deposit(item.getDeposit())
                    .status(item.getStatus())
                    .build();
            purchaseOrderVOList.add(purchaseOrderVO);
        });
        result.setRecords(purchaseOrderVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    private List<PurchaseOrderVO> getPurchaseOrderList(QueryPurchaseOrderDTO queryPurchaseOrderDTO) {
        var purchaseOrderVOList = new ArrayList<PurchaseOrderVO>();
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_ORDER)
                .in(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_ORDER)
                .eq(StringUtils.hasText(queryPurchaseOrderDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryPurchaseOrderDTO.getReceiptNumber())
                .like(StringUtils.hasText(queryPurchaseOrderDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryPurchaseOrderDTO.getRemark())
                .eq(queryPurchaseOrderDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseOrderDTO.getSupplierId())
                .eq(queryPurchaseOrderDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryPurchaseOrderDTO.getOperatorId())
                .eq(queryPurchaseOrderDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, queryPurchaseOrderDTO.getStatus())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(queryPurchaseOrderDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseOrderDTO.getStartDate())
                .le(StringUtils.hasText(queryPurchaseOrderDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseOrderDTO.getEndDate());

        var queryResult = receiptPurchaseMainMapper.selectList(queryWrapper);

        queryResult.forEach(item -> {
            var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTotalAmount);
            var taxRateTotalPrice = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTaxIncludedAmount);

            var purchaseOrderVO = PurchaseOrderVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxRateTotalAmount(taxRateTotalPrice)
                    .deposit(item.getDeposit())
                    .status(item.getStatus())
                    .build();
            purchaseOrderVOList.add(purchaseOrderVO);
        });
        return purchaseOrderVOList;
    }

    private PurchaseOrderDetailVO createPurchaseOrderDetail(ReceiptPurchaseMain purchaseMain) {
        List<FileDataBO> fileList = commonService.getFileList(purchaseMain.getFileId());

        var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseMain.getId())
                .list();

        var tableData = receiptSubList.stream()
                .map(this::createPurchaseDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));

        var operatorIds = parseAndCollectLongList(purchaseMain.getOperatorId());
        var multipleAccountIds = parseAndCollectLongList(purchaseMain.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(purchaseMain.getMultipleAccountAmount());

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
            var account = accountService.getById(purchaseMain.getAccountId());
            if (account != null) {
                accountName = account.getAccountName();
            }
        }

        return PurchaseOrderDetailVO.builder()
                .receiptNumber(purchaseMain.getReceiptNumber())
                .receiptDate(purchaseMain.getReceiptDate())
                .supplierId(purchaseMain.getSupplierId())
                .supplierName(commonService.getSupplierName(purchaseMain.getSupplierId()))
                .accountId(purchaseMain.getAccountId())
                .accountName(accountName)
                .operatorIds(operatorIds)
                .discountRate(purchaseMain.getDiscountRate())
                .discountAmount(purchaseMain.getDiscountAmount())
                .discountLastAmount(purchaseMain.getDiscountLastAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .deposit(purchaseMain.getDeposit())
                .remark(purchaseMain.getRemark())
                .tableData(tableData)
                .status(purchaseMain.getStatus())
                .files(fileList)
                .status(purchaseMain.getStatus())
                .build();
    }

    @Override
    public Response<PurchaseOrderDetailVO> getPurchaseOrderDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = getById(id);
        var purchaseOrderDetailVO = createPurchaseOrderDetail(purchaseMain);
        return Response.responseData(purchaseOrderDetailVO);
    }

    @Override
    public Response<PurchaseOrderDetailVO> getLinkPurchaseOrderDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = lambdaQuery()
                .eq(ReceiptPurchaseMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();

        var purchaseOrderDetailVO = createPurchaseOrderDetail(purchaseMain);
        return Response.responseData(purchaseOrderDetailVO);
    }

    @Override
    public Response<String> addOrUpdatePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = purchaseOrderDTO.getId() != null;

        var operatorIds = parseIdsToString(purchaseOrderDTO.getOperatorIds());

        var multipleAccountIds = parseIdsToString(purchaseOrderDTO.getMultipleAccountIds());
        var multipleAccountAmounts = parseIdsToString(purchaseOrderDTO.getMultipleAccountAmounts());
        var accountId = (purchaseOrderDTO.getAccountId() != null) ? String.valueOf(purchaseOrderDTO.getAccountId()) : null;
        var fid = processFiles(purchaseOrderDTO.getFiles(), purchaseOrderDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        if (isUpdate) {
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptPurchaseMain::getId, purchaseOrderDTO.getId())
                    .set(purchaseOrderDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, purchaseOrderDTO.getSupplierId())
                    .set(purchaseOrderDTO.getDiscountRate() != null, ReceiptPurchaseMain::getDiscountRate, purchaseOrderDTO.getDiscountRate())
                    .set(purchaseOrderDTO.getDiscountAmount() != null, ReceiptPurchaseMain::getDiscountAmount, purchaseOrderDTO.getDiscountAmount())
                    .set(purchaseOrderDTO.getDiscountLastAmount() != null, ReceiptPurchaseMain::getDiscountLastAmount, purchaseOrderDTO.getDiscountLastAmount())
                    .set(purchaseOrderDTO.getDeposit() != null, ReceiptPurchaseMain::getDeposit, purchaseOrderDTO.getDeposit())
                    .set(purchaseOrderDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, purchaseOrderDTO.getStatus())
                    .set(StringUtils.hasText(purchaseOrderDTO.getReceiptDate()), ReceiptPurchaseMain::getReceiptDate, purchaseOrderDTO.getReceiptDate())
                    .set(StringUtils.hasText(purchaseOrderDTO.getRemark()), ReceiptPurchaseMain::getRemark, purchaseOrderDTO.getRemark())
                    .set(ReceiptPurchaseMain::getAccountId, accountId)
                    .set(ReceiptPurchaseMain::getFileId, fileIds)
                    .set(ReceiptPurchaseMain::getMultipleAccount, String.valueOf(multipleAccountIds))
                    .set(ReceiptPurchaseMain::getMultipleAccountAmount, String.valueOf(multipleAccountAmounts))
                    .set(!operatorIds.isEmpty(), ReceiptPurchaseMain::getOperatorId, operatorIds)
                    .set(ReceiptPurchaseMain::getUpdateBy, userId)
                    .set(ReceiptPurchaseMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptPurchaseSubService.lambdaUpdate()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseOrderDTO.getId())
                    .remove();

            var receiptSubList = purchaseOrderDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptPurchaseSub.builder()
                            .receiptPurchaseMainId(purchaseOrderDTO.getId())
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxIncludedAmount(item.getTaxTotalPrice())
                            .remark(item.getRemark())
                            .updateBy(userId)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptPurchaseSubService.saveBatch(receiptList);

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(PurchaseCodeEnum.UPDATE_PURCHASE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(PurchaseCodeEnum.UPDATE_PURCHASE_ORDER_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var receiptMain = ReceiptPurchaseMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_ORDER)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_ORDER)
                    .initReceiptNumber(purchaseOrderDTO.getReceiptNumber())
                    .receiptNumber(purchaseOrderDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(purchaseOrderDTO.getReceiptDate()))
                    .supplierId(purchaseOrderDTO.getSupplierId())
                    .discountRate(purchaseOrderDTO.getDiscountRate())
                    .accountId(purchaseOrderDTO.getAccountId())
                    .operatorId(operatorIds)
                    .discountAmount(purchaseOrderDTO.getDiscountAmount())
                    .discountLastAmount(purchaseOrderDTO.getDiscountLastAmount())
                    .deposit(purchaseOrderDTO.getDeposit())
                    .multipleAccount(multipleAccountIds)
                    .multipleAccountAmount(multipleAccountAmounts)
                    .remark(purchaseOrderDTO.getRemark())
                    .fileId(fileIds)
                    .status(purchaseOrderDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(receiptMain);

            var receiptSubList = purchaseOrderDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptPurchaseSub.builder()
                            .receiptPurchaseMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxIncludedAmount(item.getTaxTotalPrice())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptPurchaseSubService.saveBatch(receiptList);

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(PurchaseCodeEnum.ADD_PURCHASE_ORDER_SUCCESS);
            } else {
                return Response.responseMsg(PurchaseCodeEnum.ADD_PURCHASE_ORDER_ERROR);
            }
        }
    }

    @Override
    public Response<String> deletePurchaseOrder(List<Long> ids) {
        return deletePurchase(ids, PurchaseCodeEnum.DELETE_PURCHASE_ORDER_SUCCESS, PurchaseCodeEnum.DELETE_PURCHASE_ORDER_ERROR);
    }

    @Override
    public Response<String> updatePurchaseOrderStatus(List<Long> ids, Integer status) {
        return updatePurchaseStatus(ids, status, PurchaseCodeEnum.UPDATE_PURCHASE_ORDER_SUCCESS, PurchaseCodeEnum.UPDATE_PURCHASE_ORDER_ERROR);
    }

    @Override
    public Response<Page<PurchaseStorageVO>> getPurchaseStoragePage(QueryPurchaseStorageDTO queryPurchaseStorageDTO) {
        var result = new Page<PurchaseStorageVO>();
        var purchaseStorageVOList = new ArrayList<PurchaseStorageVO>();
        var page = new Page<ReceiptPurchaseMain>(queryPurchaseStorageDTO.getPage(), queryPurchaseStorageDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_STORAGE)
                .eq(StringUtils.hasText(queryPurchaseStorageDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryPurchaseStorageDTO.getReceiptNumber())
                .like(StringUtils.hasText(queryPurchaseStorageDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryPurchaseStorageDTO.getRemark())
                .eq(queryPurchaseStorageDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseStorageDTO.getSupplierId())
                .eq(queryPurchaseStorageDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryPurchaseStorageDTO.getOperatorId())
                .eq(queryPurchaseStorageDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, queryPurchaseStorageDTO.getStatus())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(queryPurchaseStorageDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseStorageDTO.getStartDate())
                .le(StringUtils.hasText(queryPurchaseStorageDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseStorageDTO.getEndDate())
                .orderByDesc(ReceiptPurchaseMain::getCreateTime);

        var queryResult = receiptPurchaseMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTotalAmount);
            var taxIncludedAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTaxIncludedAmount);


            var totalPaymentAmount = Optional.ofNullable(item.getArrearsAmount()).orElse(BigDecimal.ZERO).add(item.getChangeAmount());

            var purchaseStorageVO = PurchaseStorageVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxIncludedAmount(taxIncludedAmount)
                    .totalPaymentAmount(totalPaymentAmount)
                    .thisPaymentAmount(item.getChangeAmount())
                    .thisArrearsAmount(item.getArrearsAmount())
                    .status(item.getStatus())
                    .build();
            purchaseStorageVOList.add(purchaseStorageVO);
        });
        result.setRecords(purchaseStorageVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    private List<PurchaseStorageVO> getPurchaseStorageList(QueryPurchaseStorageDTO queryPurchaseStorageDTO) {
        var purchaseStorageVOList = new ArrayList<PurchaseStorageVO>();
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_STORAGE)
                .eq(StringUtils.hasText(queryPurchaseStorageDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryPurchaseStorageDTO.getReceiptNumber())
                .like(StringUtils.hasText(queryPurchaseStorageDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryPurchaseStorageDTO.getRemark())
                .eq(queryPurchaseStorageDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseStorageDTO.getSupplierId())
                .eq(queryPurchaseStorageDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryPurchaseStorageDTO.getOperatorId())
                .eq(queryPurchaseStorageDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, queryPurchaseStorageDTO.getStatus())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(queryPurchaseStorageDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseStorageDTO.getStartDate())
                .le(StringUtils.hasText(queryPurchaseStorageDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseStorageDTO.getEndDate());

        var queryResult = receiptPurchaseMainMapper.selectList(queryWrapper);

        queryResult.forEach(item -> {
            var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTotalAmount);
            var taxIncludedAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTaxIncludedAmount);
            var totalPaymentAmount = Optional.ofNullable(item.getArrearsAmount()).orElse(BigDecimal.ZERO).add(item.getChangeAmount());
            var purchaseStorageVO = PurchaseStorageVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxIncludedAmount(taxIncludedAmount)
                    .totalPaymentAmount(totalPaymentAmount)
                    .thisPaymentAmount(item.getChangeAmount())
                    .thisArrearsAmount(item.getArrearsAmount())
                    .status(item.getStatus())
                    .build();
            purchaseStorageVOList.add(purchaseStorageVO);
        });
        return purchaseStorageVOList;
    }

    private PurchaseStorageDetailVO createPurchaseStorageDetail(ReceiptPurchaseMain purchaseMain) {
        if (purchaseMain == null) {
            throw new IllegalArgumentException("purchaseMain cannot be null");
        }

        List<FileDataBO> fileList = commonService.getFileList(purchaseMain.getFileId());
        List<ReceiptPurchaseSub> receiptSubList = receiptPurchaseSubService.lambdaQuery()
                .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseMain.getId())
                .list();

        var tableData = receiptSubList.stream()
                .map(this::createPurchaseDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));
        var operatorIds = parseAndCollectLongList(purchaseMain.getOperatorId());
        var multipleAccountIds = parseAndCollectLongList(purchaseMain.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(purchaseMain.getMultipleAccountAmount());

        var accountName = createAccountName(multipleAccountIds, multipleAccountAmounts);
        if (!StringUtils.hasLength(accountName)) {
            var account = accountService.getById(purchaseMain.getAccountId());
            if (account != null) {
                accountName = account.getAccountName();
            }
        }

        return PurchaseStorageDetailVO.builder()
                .receiptNumber(purchaseMain.getReceiptNumber())
                .receiptDate(purchaseMain.getReceiptDate())
                .supplierId(purchaseMain.getSupplierId())
                .supplierName(commonService.getSupplierName(purchaseMain.getSupplierId()))
                .accountId(purchaseMain.getAccountId())
                .operatorIds(operatorIds)
                .paymentRate(purchaseMain.getDiscountRate())
                .paymentAmount(purchaseMain.getDiscountAmount())
                .paymentLastAmount(purchaseMain.getDiscountLastAmount())
                .otherAmount(purchaseMain.getOtherAmount())
                .otherReceipt(purchaseMain.getOtherReceipt())
                .thisPaymentAmount(purchaseMain.getChangeAmount())
                .thisArrearsAmount(purchaseMain.getArrearsAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .accountName(accountName)
                .remark(purchaseMain.getRemark())
                .status(purchaseMain.getStatus())
                .tableData(tableData)
                .files(fileList)
                .build();
    }

    private String createAccountName(List<Long> multipleAccountIds, List<Long> multipleAccountAmounts) {
        if (multipleAccountIds.isEmpty() || multipleAccountAmounts.isEmpty()) {
            return "";
        }
        var accountNameList = new ArrayList<String>();
        for (int i = 0; i < multipleAccountIds.size(); i++) {
            var account = accountService.getById(multipleAccountIds.get(i));
            var accountAmount = multipleAccountAmounts.get(i);
            accountNameList.add(account.getAccountName() + "(" + accountAmount + "元)");
        }
        return StringUtils.collectionToCommaDelimitedString(accountNameList);
    }

    @Override
    public Response<PurchaseStorageDetailVO> getPurchaseStorageDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = lambdaQuery()
                .eq(ReceiptPurchaseMain::getId, id)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        var purchasesStorageDetailVO = createPurchaseStorageDetail(purchaseMain);
        return Response.responseData(purchasesStorageDetailVO);
    }

    @Override
    public Response<PurchaseStorageDetailVO> getLinkPurchaseStorageDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = lambdaQuery()
                .eq(ReceiptPurchaseMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        var purchasesStorageDetailVO = createPurchaseStorageDetail(purchaseMain);
        return Response.responseData(purchasesStorageDetailVO);
    }

    @Override
    public Response<String> addOrUpdatePurchaseStorage(PurchaseStorageDTO purchaseStorageDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = purchaseStorageDTO.getId() != null;

        var operatorIds = parseIdsToString(purchaseStorageDTO.getOperatorIds());
        var multipleAccountIds = parseIdsToString(purchaseStorageDTO.getMultipleAccountIds());
        var multipleAccountAmounts = parseIdsToString(purchaseStorageDTO.getMultipleAccountAmounts());
        String accountId = (purchaseStorageDTO.getAccountId() != null) ? String.valueOf(purchaseStorageDTO.getAccountId()) : null;

        var fid = processFiles(purchaseStorageDTO.getFiles(), purchaseStorageDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

        if (isUpdate) {
            var beforeReceipt = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseStorageDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 2);
            }

            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptPurchaseMain::getId, purchaseStorageDTO.getId())
                    .set(purchaseStorageDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, purchaseStorageDTO.getSupplierId())
                    .set(purchaseStorageDTO.getPaymentRate() != null, ReceiptPurchaseMain::getDiscountRate, purchaseStorageDTO.getPaymentRate())
                    .set(purchaseStorageDTO.getPaymentAmount() != null, ReceiptPurchaseMain::getDiscountAmount, purchaseStorageDTO.getPaymentAmount())
                    .set(purchaseStorageDTO.getPaymentLastAmount() != null, ReceiptPurchaseMain::getDiscountLastAmount, purchaseStorageDTO.getPaymentLastAmount())
                    .set(purchaseStorageDTO.getOtherAmount() != null, ReceiptPurchaseMain::getOtherAmount, purchaseStorageDTO.getOtherAmount())
                    .set(purchaseStorageDTO.getThisPaymentAmount() != null, ReceiptPurchaseMain::getChangeAmount, purchaseStorageDTO.getThisPaymentAmount().negate())
                    .set(purchaseStorageDTO.getThisArrearsAmount() != null, ReceiptPurchaseMain::getArrearsAmount, purchaseStorageDTO.getThisArrearsAmount())
                    .set(purchaseStorageDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, purchaseStorageDTO.getStatus())
                    .set(StringUtils.hasText(purchaseStorageDTO.getOtherReceipt()), ReceiptPurchaseMain::getOtherReceipt, purchaseStorageDTO.getOtherReceipt())
                    .set(StringUtils.hasText(purchaseStorageDTO.getReceiptDate()), ReceiptPurchaseMain::getReceiptDate, purchaseStorageDTO.getReceiptDate())
                    .set(StringUtils.hasText(purchaseStorageDTO.getRemark()), ReceiptPurchaseMain::getRemark, purchaseStorageDTO.getRemark())
                    .set(ReceiptPurchaseMain::getAccountId, accountId)
                    .set(ReceiptPurchaseMain::getFileId, fileIds)
                    .set(ReceiptPurchaseMain::getMultipleAccount, multipleAccountIds)
                    .set(ReceiptPurchaseMain::getMultipleAccountAmount, multipleAccountAmounts)
                    .set(!operatorIds.isEmpty(), ReceiptPurchaseMain::getOperatorId, operatorIds)
                    .set(ReceiptPurchaseMain::getUpdateBy, userId)
                    .set(ReceiptPurchaseMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptPurchaseSubService.lambdaUpdate()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseStorageDTO.getId())
                    .remove();

            var tableData = purchaseStorageDTO.getTableData();
            var receiptPurchaseStorageList = tableData.stream()
                    .map(item -> ReceiptPurchaseSub.builder()
                            .receiptPurchaseMainId(purchaseStorageDTO.getId())
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

            var updateSubResult = receiptPurchaseSubService.saveBatch(receiptPurchaseStorageList);
            updateProductStock(receiptPurchaseStorageList, 1);

            var account = accountService.getById(purchaseStorageDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var thisPaymentAmount = purchaseStorageDTO.getThisPaymentAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(ReceiptPurchaseSub::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.add(beforeChangeAmount);
                if (thisPaymentAmount != null) {
                    accountBalance = accountBalance.subtract(thisPaymentAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }

            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(PurchaseCodeEnum.UPDATE_PURCHASE_RECEIPT_SUCCESS);
            } else {
                return Response.responseMsg(PurchaseCodeEnum.UPDATE_PURCHASE_RECEIPT_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var receiptMain = ReceiptPurchaseMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_STORAGE)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_STORAGE)
                    .initReceiptNumber(purchaseStorageDTO.getReceiptNumber())
                    .receiptNumber(purchaseStorageDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(purchaseStorageDTO.getReceiptDate()))
                    .supplierId(purchaseStorageDTO.getSupplierId())
                    .discountRate(purchaseStorageDTO.getPaymentRate())
                    .accountId(purchaseStorageDTO.getAccountId())
                    .operatorId(operatorIds)
                    .discountAmount(purchaseStorageDTO.getPaymentAmount())
                    .discountLastAmount(purchaseStorageDTO.getPaymentLastAmount())
                    .otherAmount(purchaseStorageDTO.getOtherAmount())
                    .otherReceipt(purchaseStorageDTO.getOtherReceipt())
                    .changeAmount(purchaseStorageDTO.getThisPaymentAmount().negate())
                    .arrearsAmount(purchaseStorageDTO.getThisArrearsAmount())
                    .multipleAccount(multipleAccountIds)
                    .multipleAccountAmount(multipleAccountAmounts)
                    .remark(purchaseStorageDTO.getRemark())
                    .fileId(fileIds)
                    .status(purchaseStorageDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(receiptMain);

            var receiptSubList = purchaseStorageDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptPurchaseSub.builder()
                            .receiptPurchaseMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxIncludedAmount(item.getTaxTotalPrice())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptPurchaseSubService.saveBatch(receiptList);
            updateProductStock(receiptList, 1);
            var account = accountService.getById(purchaseStorageDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var changeAmount = purchaseStorageDTO.getThisPaymentAmount();
                if (changeAmount != null) {
                    accountBalance = accountBalance.subtract(changeAmount);
                    account.setId(purchaseStorageDTO.getAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(PurchaseCodeEnum.ADD_PURCHASE_RECEIPT_SUCCESS);
            } else {
                return Response.responseMsg(PurchaseCodeEnum.ADD_PURCHASE_RECEIPT_ERROR);
            }
        }
    }

    @Override
    public Response<String> deletePurchaseStorage(List<Long> ids) {
        return deletePurchase(ids, PurchaseCodeEnum.DELETE_PURCHASE_RECEIPT_SUCCESS, PurchaseCodeEnum.DELETE_PURCHASE_RECEIPT_ERROR);
    }

    @Override
    public Response<String> updatePurchaseStorageStatus(List<Long> ids, Integer status) {
        return updatePurchaseStatus(ids, status, PurchaseCodeEnum.UPDATE_PURCHASE_RECEIPT_SUCCESS, PurchaseCodeEnum.UPDATE_PURCHASE_RECEIPT_ERROR);
    }

    @Override
    public Response<Page<PurchaseRefundVO>> getPurchaseRefundPage(QueryPurchaseRefundDTO queryPurchaseRefundDTO) {
        var result = new Page<PurchaseRefundVO>();
        var purchaseRefundVOList = new ArrayList<PurchaseRefundVO>();
        var page = new Page<ReceiptPurchaseMain>(queryPurchaseRefundDTO.getPage(), queryPurchaseRefundDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_REFUND)
                .eq(StringUtils.hasText(queryPurchaseRefundDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryPurchaseRefundDTO.getReceiptNumber())
                .like(StringUtils.hasText(queryPurchaseRefundDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryPurchaseRefundDTO.getRemark())
                .eq(queryPurchaseRefundDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseRefundDTO.getSupplierId())
                .eq(queryPurchaseRefundDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryPurchaseRefundDTO.getOperatorId())
                .eq(queryPurchaseRefundDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, queryPurchaseRefundDTO.getStatus())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(queryPurchaseRefundDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseRefundDTO.getStartDate())
                .le(StringUtils.hasText(queryPurchaseRefundDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseRefundDTO.getEndDate())
                .orderByDesc(ReceiptPurchaseMain::getCreateTime);

        var queryResult = receiptPurchaseMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTotalAmount);
            var taxIncludedAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTaxIncludedAmount);

            var totalPaymentAmount = Optional.ofNullable(item.getArrearsAmount()).orElse(BigDecimal.ZERO).add(item.getChangeAmount());

            var purchaseRefundVO = PurchaseRefundVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxIncludedAmount(taxIncludedAmount)
                    .refundTotalAmount(totalPaymentAmount)
                    .thisRefundAmount(item.getChangeAmount())
                    .thisArrearsAmount(item.getArrearsAmount())
                    .status(item.getStatus())
                    .build();
            purchaseRefundVOList.add(purchaseRefundVO);
        });
        result.setRecords(purchaseRefundVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    private List<PurchaseRefundVO> getPurchaseRefundList(QueryPurchaseRefundDTO queryPurchaseRefundDTO) {
        var purchaseRefundVOList = new ArrayList<PurchaseRefundVO>();
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(ReceiptPurchaseMain::getType, ReceiptConstants.RECEIPT_TYPE_STORAGE)
                .in(ReceiptPurchaseMain::getSubType, ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_REFUND)
                .eq(StringUtils.hasText(queryPurchaseRefundDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, queryPurchaseRefundDTO.getReceiptNumber())
                .like(StringUtils.hasText(queryPurchaseRefundDTO.getRemark()), ReceiptPurchaseMain::getRemark, queryPurchaseRefundDTO.getRemark())
                .eq(queryPurchaseRefundDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, queryPurchaseRefundDTO.getSupplierId())
                .eq(queryPurchaseRefundDTO.getOperatorId() != null, ReceiptPurchaseMain::getCreateBy, queryPurchaseRefundDTO.getOperatorId())
                .eq(queryPurchaseRefundDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, queryPurchaseRefundDTO.getStatus())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .ge(StringUtils.hasText(queryPurchaseRefundDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseRefundDTO.getStartDate())
                .le(StringUtils.hasText(queryPurchaseRefundDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseRefundDTO.getEndDate());

        var queryResult = receiptPurchaseMainMapper.selectList(queryWrapper);

        queryResult.forEach(item -> {
            var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var crateBy = getUserName(item.getCreateBy());
            var totalAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTotalAmount);
            var taxIncludedAmount = calculateTotalAmount(receiptSubList, ReceiptPurchaseSub::getTaxIncludedAmount);

            var totalPaymentAmount = Optional.ofNullable(item.getArrearsAmount()).orElse(BigDecimal.ZERO).add(item.getChangeAmount());

            var purchaseRefundVO = PurchaseRefundVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalAmount(totalAmount)
                    .taxIncludedAmount(taxIncludedAmount)
                    .refundTotalAmount(totalPaymentAmount)
                    .thisRefundAmount(item.getChangeAmount())
                    .thisArrearsAmount(item.getArrearsAmount())
                    .status(item.getStatus())
                    .build();
            purchaseRefundVOList.add(purchaseRefundVO);
        });
        return purchaseRefundVOList;
    }

    @Override
    public Response<PurchaseRefundDetailVO> getPurchaseRefundDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = getById(id);
        var purchasesStorageDetailVO = getPurchaseRefundDetail(purchaseMain);
        return Response.responseData(purchasesStorageDetailVO);
    }

    @Override
    public Response<PurchaseRefundDetailVO> getLinkPurchaseRefundDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = lambdaQuery()
                .eq(ReceiptPurchaseMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        var PurchaseRefundDetailVO = getPurchaseRefundDetail(purchaseMain);
        return Response.responseData(PurchaseRefundDetailVO);
    }

    private PurchaseRefundDetailVO getPurchaseRefundDetail(ReceiptPurchaseMain purchaseMain) {
        if (purchaseMain == null) {
            throw new IllegalArgumentException("purchaseMain cannot be null");
        }

        List<FileDataBO> fileList = commonService.getFileList(purchaseMain.getFileId());
        List<ReceiptPurchaseSub> receiptSubList = receiptPurchaseSubService.lambdaQuery()
                .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseMain.getId())
                .list();

        var tableData = receiptSubList.stream()
                .map(this::createPurchaseDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));
        var operatorIds = parseAndCollectLongList(purchaseMain.getOperatorId());
        var multipleAccountIds = parseAndCollectLongList(purchaseMain.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(purchaseMain.getMultipleAccountAmount());

        var accountName = createAccountName(multipleAccountIds, multipleAccountAmounts);
        if (!StringUtils.hasLength(accountName)) {
            var account = accountService.getById(purchaseMain.getAccountId());
            if (account != null) {
                accountName = account.getAccountName();
            }
        }

        return PurchaseRefundDetailVO.builder()
                .receiptNumber(purchaseMain.getReceiptNumber())
                .receiptDate(purchaseMain.getReceiptDate())
                .supplierId(purchaseMain.getSupplierId())
                .supplierName(commonService.getSupplierName(purchaseMain.getSupplierId()))
                .accountId(purchaseMain.getAccountId())
                .refundOfferRate(purchaseMain.getDiscountRate())
                .refundOfferAmount(purchaseMain.getDiscountAmount())
                .refundLastAmount(purchaseMain.getDiscountLastAmount())
                .otherAmount(purchaseMain.getOtherAmount())
                .otherReceipt(purchaseMain.getOtherReceipt())
                .thisRefundAmount(purchaseMain.getChangeAmount())
                .thisArrearsAmount(purchaseMain.getArrearsAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .accountName(accountName)
                .accountId(purchaseMain.getAccountId())
                .operatorIds(operatorIds)
                .remark(purchaseMain.getRemark())
                .status(purchaseMain.getStatus())
                .tableData(tableData)
                .files(fileList)
                .build();
    }

    private List<PurchaseDataBO> createTableDataList(List<ReceiptPurchaseSub> receiptSubList) {
        var tableData = new ArrayList<PurchaseDataBO>(receiptSubList.size() + 1);
        for (ReceiptPurchaseSub item : receiptSubList) {
            var purchaseData = createPurchaseDataFromReceiptSub(item);
            tableData.add(purchaseData);
        }
        return tableData;
    }

    @Override
    public Response<String> addOrUpdatePurchaseRefund(PurchaseRefundDTO purchaseRefundDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = purchaseRefundDTO.getId() != null;

        var operatorIds = parseIdsToString(purchaseRefundDTO.getOperatorIds());
        var multipleAccountIds = parseIdsToString(purchaseRefundDTO.getMultipleAccountIds());
        var multipleAccountAmounts = parseIdsToString(purchaseRefundDTO.getMultipleAccountAmounts());
        String accountId = (purchaseRefundDTO.getAccountId() != null) ? String.valueOf(purchaseRefundDTO.getAccountId()) : null;

        var fid = processFiles(purchaseRefundDTO.getFiles(), purchaseRefundDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

        if (isUpdate) {
            var beforeReceipt = receiptPurchaseSubService.lambdaQuery()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseRefundDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 1);
            }
            var updateMainResult = lambdaUpdate()
                    .eq(ReceiptPurchaseMain::getId, purchaseRefundDTO.getId())
                    .set(purchaseRefundDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, purchaseRefundDTO.getSupplierId())
                    .set(purchaseRefundDTO.getRefundOfferRate() != null, ReceiptPurchaseMain::getDiscountRate, purchaseRefundDTO.getRefundOfferRate())
                    .set(purchaseRefundDTO.getRefundOfferAmount() != null, ReceiptPurchaseMain::getDiscountAmount, purchaseRefundDTO.getRefundOfferAmount())
                    .set(purchaseRefundDTO.getRefundLastAmount() != null, ReceiptPurchaseMain::getDiscountLastAmount, purchaseRefundDTO.getRefundLastAmount())
                    .set(purchaseRefundDTO.getOtherAmount() != null, ReceiptPurchaseMain::getOtherAmount, purchaseRefundDTO.getOtherAmount())
                    .set(purchaseRefundDTO.getThisRefundAmount() != null, ReceiptPurchaseMain::getChangeAmount, purchaseRefundDTO.getThisRefundAmount())
                    .set(purchaseRefundDTO.getThisArrearsAmount() != null, ReceiptPurchaseMain::getArrearsAmount, purchaseRefundDTO.getThisArrearsAmount())
                    .set(purchaseRefundDTO.getStatus() != null, ReceiptPurchaseMain::getStatus, purchaseRefundDTO.getStatus())
                    .set(StringUtils.hasText(purchaseRefundDTO.getOtherReceipt()), ReceiptPurchaseMain::getOtherReceipt, purchaseRefundDTO.getOtherReceipt())
                    .set(StringUtils.hasText(purchaseRefundDTO.getReceiptDate()), ReceiptPurchaseMain::getReceiptDate, purchaseRefundDTO.getReceiptDate())
                    .set(StringUtils.hasText(purchaseRefundDTO.getRemark()), ReceiptPurchaseMain::getRemark, purchaseRefundDTO.getRemark())
                    .set(ReceiptPurchaseMain::getAccountId, accountId)
                    .set(ReceiptPurchaseMain::getFileId, fileIds)
                    .set(ReceiptPurchaseMain::getMultipleAccount, multipleAccountIds)
                    .set(ReceiptPurchaseMain::getMultipleAccountAmount, multipleAccountAmounts)
                    .set(!operatorIds.isEmpty(), ReceiptPurchaseMain::getOperatorId, operatorIds)
                    .set(ReceiptPurchaseMain::getUpdateBy, userId)
                    .set(ReceiptPurchaseMain::getUpdateTime, LocalDateTime.now())
                    .update();

            receiptPurchaseSubService.lambdaUpdate()
                    .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, purchaseRefundDTO.getId())
                    .remove();

            var tableData = purchaseRefundDTO.getTableData();
            var receiptPurchaseRefundList = tableData.stream()
                    .map(item -> ReceiptPurchaseSub.builder()
                            .receiptPurchaseMainId(purchaseRefundDTO.getId())
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

            var updateSubResult = receiptPurchaseSubService.saveBatch(receiptPurchaseRefundList);
            updateProductStock(receiptPurchaseRefundList, 2);

            var account = accountService.getById(purchaseRefundDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var thisRefundAmount = purchaseRefundDTO.getThisRefundAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(ReceiptPurchaseSub::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.subtract(thisRefundAmount);
                if (thisRefundAmount != null) {
                    accountBalance = accountBalance.add(beforeChangeAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }


            if (updateMainResult && updateSubResult) {
                return Response.responseMsg(PurchaseCodeEnum.UPDATE_PURCHASE_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(PurchaseCodeEnum.UPDATE_PURCHASE_REFUND_ERROR);
            }
        } else {
            var id = SnowflakeIdUtil.nextId();

            var receiptMain = ReceiptPurchaseMain.builder()
                    .id(id)
                    .type(ReceiptConstants.RECEIPT_TYPE_STORAGE)
                    .subType(ReceiptConstants.RECEIPT_SUB_TYPE_PURCHASE_REFUND)
                    .initReceiptNumber(purchaseRefundDTO.getReceiptNumber())
                    .receiptNumber(purchaseRefundDTO.getReceiptNumber())
                    .receiptDate(TimeUtil.parse(purchaseRefundDTO.getReceiptDate()))
                    .supplierId(purchaseRefundDTO.getSupplierId())
                    .discountRate(purchaseRefundDTO.getRefundOfferRate())
                    .accountId(purchaseRefundDTO.getAccountId())
                    .operatorId(operatorIds)
                    .discountAmount(purchaseRefundDTO.getRefundOfferAmount())
                    .discountLastAmount(purchaseRefundDTO.getRefundLastAmount())
                    .otherAmount(purchaseRefundDTO.getOtherAmount())
                    .otherReceipt(purchaseRefundDTO.getOtherReceipt())
                    .changeAmount(purchaseRefundDTO.getThisRefundAmount())
                    .arrearsAmount(purchaseRefundDTO.getThisArrearsAmount())
                    .multipleAccount(multipleAccountIds)
                    .multipleAccountAmount(multipleAccountAmounts)
                    .remark(purchaseRefundDTO.getRemark())
                    .fileId(fileIds)
                    .status(purchaseRefundDTO.getStatus())
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveMainResult = save(receiptMain);

            var receiptSubList = purchaseRefundDTO.getTableData();
            var receiptList = receiptSubList.stream()
                    .map(item -> ReceiptPurchaseSub.builder()
                            .receiptPurchaseMainId(id)
                            .productId(item.getProductId())
                            .productNumber(item.getProductNumber())
                            .unitPrice(item.getUnitPrice())
                            .totalAmount(item.getAmount())
                            .productBarcode(item.getBarCode())
                            .warehouseId(item.getWarehouseId())
                            .taxRate(item.getTaxRate())
                            .taxAmount(item.getTaxAmount())
                            .taxIncludedAmount(item.getTaxTotalPrice())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var saveSubResult = receiptPurchaseSubService.saveBatch(receiptList);
            updateProductStock(receiptList, 2);
            var account = accountService.getById(purchaseRefundDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var thisRefundAmount = purchaseRefundDTO.getThisRefundAmount();
                if (thisRefundAmount != null) {
                    accountBalance = accountBalance.add(thisRefundAmount);
                    account.setId(purchaseRefundDTO.getAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }

            if (saveMainResult && saveSubResult) {
                return Response.responseMsg(PurchaseCodeEnum.ADD_PURCHASE_REFUND_SUCCESS);
            } else {
                return Response.responseMsg(PurchaseCodeEnum.ADD_PURCHASE_REFUND_ERROR);
            }
        }
    }

    @Override
    public Response<String> deletePurchaseRefund(List<Long> ids) {
        return deletePurchase(ids, PurchaseCodeEnum.DELETE_PURCHASE_REFUND_SUCCESS, PurchaseCodeEnum.DELETE_PURCHASE_REFUND_ERROR);
    }

    @Override
    public Response<String> updatePurchaseRefundStatus(List<Long> ids, Integer status) {
        return updatePurchaseStatus(ids, status, PurchaseCodeEnum.UPDATE_PURCHASE_REFUND_SUCCESS, PurchaseCodeEnum.UPDATE_PURCHASE_REFUND_ERROR);
    }

    @Override
    public Response<Page<PurchaseArrearsVO>> getPurchaseArrearsPage(QueryPurchaseArrearsDTO arrearsDTO) {
        var result = new Page<PurchaseArrearsVO>();
        var purchaseArrearsVOList = new ArrayList<PurchaseArrearsVO>();
        var page = new Page<ReceiptPurchaseMain>(arrearsDTO.getPage(), arrearsDTO.getPageSize());
        var queryWrapper = new LambdaQueryWrapper<ReceiptPurchaseMain>()
                .eq(StringUtils.hasText(arrearsDTO.getReceiptNumber()), ReceiptPurchaseMain::getReceiptNumber, arrearsDTO.getReceiptNumber())
                .eq(arrearsDTO.getSupplierId() != null, ReceiptPurchaseMain::getSupplierId, arrearsDTO.getSupplierId())
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .gt(ReceiptPurchaseMain::getArrearsAmount, BigDecimal.ZERO)
                .ge(StringUtils.hasText(arrearsDTO.getStartDate()), ReceiptPurchaseMain::getCreateTime, arrearsDTO.getStartDate())
                .le(StringUtils.hasText(arrearsDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, arrearsDTO.getEndDate());

        var queryResult = receiptPurchaseMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var supplierName = commonService.getSupplierName(item.getSupplierId());
            var operatorName = getUserName(item.getCreateBy());
            var financeMainList = paymentReceiptService.lambdaQuery()
                    .eq(FinancialMain::getRelatedPersonId, item.getSupplierId())
                    .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list();
            var purchaseArrearsVO = PurchaseArrearsVO.builder()
                    .id(item.getId())
                    .supplierName(supplierName)
                    .receiptDate(item.getReceiptDate())
                    .receiptNumber(item.getReceiptNumber())
                    .productInfo(item.getRemark())
                    .operatorName(operatorName)
                    .thisReceiptArrears(item.getArrearsAmount())
                    .build();


            BigDecimal prepaidArrears = BigDecimal.ZERO;

            if(!financeMainList.isEmpty()) {
                for (FinancialMain financialMain : financeMainList) {
                    var financeSubList = financialSubService.lambdaQuery()
                            .eq(FinancialSub::getFinancialMainId, financialMain.getId())
                            .eq(FinancialSub::getOtherReceipt, item.getReceiptNumber())
                            .eq(FinancialSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                            .list();
                    prepaidArrears = prepaidArrears.add(calculateArrearsAmount(financeSubList, FinancialSub::getReceivedPrepaidArrears));
                }
            }
            BigDecimal paymentArrears = item.getArrearsAmount().subtract(prepaidArrears);
            // Only add the PurchaseArrearsVO to the list if there are remaining arrears
            if (paymentArrears.compareTo(BigDecimal.ZERO) > 0) {
                purchaseArrearsVO.setPrepaidArrears(prepaidArrears);
                purchaseArrearsVO.setPaymentArrears(paymentArrears);
                purchaseArrearsVOList.add(purchaseArrearsVO);
            }
        });
        result.setRecords(purchaseArrearsVOList);
        result.setTotal(queryResult.getTotal());
        result.setCurrent(queryResult.getCurrent());
        result.setSize(queryResult.getSize());

        return Response.responseData(result);
    }

    @Override
    public void exportPurchaseOrderExcel(QueryPurchaseOrderDTO queryPurchaseOrderDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var mainData = getPurchaseOrderList(queryPurchaseOrderDTO);
        if (!mainData.isEmpty()) {
            if (queryPurchaseOrderDTO.getIsExportDetail()) {
                var subData = new ArrayList<PurchaseDataExportBO>();
                for (PurchaseOrderVO purchaseOrderVO : mainData) {
                    var detail = getPurchaseOrderDetail(purchaseOrderVO.getId()).getData().getTableData();
                    if (!detail.isEmpty()) {
                        detail.forEach(item -> {
                            var purchaseDataBo = PurchaseDataExportBO.builder()
                                    .supplierName(purchaseOrderVO.getSupplierName())
                                    .receiptNumber(purchaseOrderVO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .stock(item.getStock())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .taxRate(item.getTaxRate())
                                    .taxAmount(item.getTaxAmount())
                                    .taxTotalPrice(item.getTaxTotalPrice())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(purchaseDataBo);
                        });
                    }
                }
                exportMap.put("采购订单明细", ExcelUtils.getSheetData(subData));
            }
            exportMap.put("采购订单", ExcelUtils.getSheetData(mainData));
            ExcelUtils.exportManySheet(response, "采购订单", exportMap);
        }
    }

    @Override
    public void exportPurchaseOrderDetailExcel(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(ReceiptPurchaseMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one()
                .getId();
        var detail = getPurchaseOrderDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var exportData = new ArrayList<PurchaseDataExportBO>();
            tableData.forEach(item -> {
                var purchaseBo = new PurchaseDataExportBO();
                purchaseBo.setSupplierName(data.getSupplierName());
                purchaseBo.setReceiptNumber(data.getReceiptNumber());
                BeanUtils.copyProperties(item, purchaseBo);
                exportData.add(purchaseBo);
            });
            var fileName = data.getReceiptNumber() + "-采购订单明细";
            ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
        }
    }

    @Override
    public void exportPurchaseStorageExcel(QueryPurchaseStorageDTO queryPurchaseStorageDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var mainData = getPurchaseStorageList(queryPurchaseStorageDTO);
        if (!mainData.isEmpty()) {
            if (queryPurchaseStorageDTO.getIsExportDetail()) {
                var subData = new ArrayList<PurchaseDataExportBO>();
                for (PurchaseStorageVO purchaseStorageVO : mainData) {
                    var detail = getPurchaseStorageDetail(purchaseStorageVO.getId()).getData().getTableData();
                    if (!detail.isEmpty()) {
                        detail.forEach(item -> {
                            var purchaseDataBo = PurchaseDataExportBO.builder()
                                    .supplierName(purchaseStorageVO.getSupplierName())
                                    .receiptNumber(purchaseStorageVO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .stock(item.getStock())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .taxRate(item.getTaxRate())
                                    .taxAmount(item.getTaxAmount())
                                    .taxTotalPrice(item.getTaxTotalPrice())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(purchaseDataBo);
                        });
                    }
                }
                exportMap.put("采购入库明细", ExcelUtils.getSheetData(subData));
            }
            exportMap.put("采购入库", ExcelUtils.getSheetData(mainData));
            ExcelUtils.exportManySheet(response, "采购入库", exportMap);
        }
    }

    @Override
    public void exportPurchaseStorageDetailExcel(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(ReceiptPurchaseMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one()
                .getId();
        var detail = getPurchaseStorageDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var exportData = new ArrayList<PurchaseDataExportBO>();
            tableData.forEach(item -> {
                var purchaseBo = new PurchaseDataExportBO();
                purchaseBo.setSupplierName(data.getSupplierName());
                purchaseBo.setReceiptNumber(data.getReceiptNumber());
                BeanUtils.copyProperties(item, purchaseBo);
                exportData.add(purchaseBo);
            });
            var fileName = data.getReceiptNumber() + "-采购入库单明细";
            ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
        }
    }

    @Override
    public void exportPurchaseRefundExcel(QueryPurchaseRefundDTO queryPurchaseRefundDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var mainData = getPurchaseRefundList(queryPurchaseRefundDTO);
        if (!mainData.isEmpty()) {
            if (queryPurchaseRefundDTO.getIsExportDetail()) {
                var subData = new ArrayList<PurchaseDataExportBO>();
                for (PurchaseRefundVO purchaseRefundVO : mainData) {
                    var detail = getPurchaseRefundDetail(purchaseRefundVO.getId()).getData().getTableData();
                    if (!detail.isEmpty()) {
                        detail.forEach(item -> {
                            var purchaseDataBo = PurchaseDataExportBO.builder()
                                    .supplierName(purchaseRefundVO.getSupplierName())
                                    .receiptNumber(purchaseRefundVO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .stock(item.getStock())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .taxRate(item.getTaxRate())
                                    .taxAmount(item.getTaxAmount())
                                    .taxTotalPrice(item.getTaxTotalPrice())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(purchaseDataBo);
                        });
                    }
                }
                exportMap.put("采购退货明细", ExcelUtils.getSheetData(subData));
            }
            exportMap.put("采购退货", ExcelUtils.getSheetData(mainData));
            ExcelUtils.exportManySheet(response, "采购退货", exportMap);
        }
    }

    @Override
    public void exportPurchaseRefundDetailExcel(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(ReceiptPurchaseMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptPurchaseMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one()
                .getId();
        var detail = getPurchaseRefundDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var exportData = new ArrayList<PurchaseDataExportBO>();
            tableData.forEach(item -> {
                var purchaseBo = new PurchaseDataExportBO();
                purchaseBo.setSupplierName(data.getSupplierName());
                purchaseBo.setReceiptNumber(data.getReceiptNumber());
                BeanUtils.copyProperties(item, purchaseBo);
                exportData.add(purchaseBo);
            });
            var fileName = data.getReceiptNumber() + "-采购退货单明细";
            ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
        }
    }
}
