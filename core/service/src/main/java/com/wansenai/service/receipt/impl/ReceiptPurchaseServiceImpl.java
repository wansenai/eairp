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
import com.wansenai.bo.PurchaseDataBO;
import com.wansenai.dto.receipt.purchase.PurchaseOrderDTO;
import com.wansenai.dto.receipt.purchase.QueryPurchaseOrderDTO;
import com.wansenai.entities.receipt.ReceiptPurchaseMain;
import com.wansenai.entities.receipt.ReceiptPurchaseSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.product.ProductStockKeepUnitMapper;
import com.wansenai.mappers.receipt.ReceiptPurchaseMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.basic.SupplierService;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.receipt.ReceiptPurchaseService;
import com.wansenai.service.receipt.ReceiptPurchaseSubService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.PurchaseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.purchase.PurchaseOrderDetailVO;
import com.wansenai.vo.receipt.purchase.PurchaseOrderVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReceiptPurchaseServiceImpl extends ServiceImpl<ReceiptPurchaseMainMapper, ReceiptPurchaseMain> implements ReceiptPurchaseService {

    private final SysFileMapper fileMapper;
    private final ProductStockKeepUnitMapper productStockKeepUnitMapper;
    private final CommonService commonService;
    private final ISysUserService userService;
    private final SupplierService supplierService;
    private final ReceiptPurchaseMainMapper receiptPurchaseMainMapper;
    private final ReceiptPurchaseSubService receiptPurchaseSubService;

    public ReceiptPurchaseServiceImpl(SysFileMapper fileMapper, ProductStockKeepUnitMapper productStockKeepUnitMapper, CommonService commonService, ISysUserService userService, SupplierService supplierService, ReceiptPurchaseMainMapper receiptPurchaseMainMapper, ReceiptPurchaseSubService receiptPurchaseSubService) {
        this.fileMapper = fileMapper;
        this.productStockKeepUnitMapper = productStockKeepUnitMapper;
        this.commonService = commonService;
        this.userService = userService;
        this.supplierService = supplierService;
        this.receiptPurchaseMainMapper = receiptPurchaseMainMapper;
        this.receiptPurchaseSubService = receiptPurchaseSubService;
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

    private String getSupplierName(Long supplierId) {
        return (supplierId != null) ? supplierService.getById(supplierId).getSupplierName() : null;
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

        var data = productStockKeepUnitMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
        if (data != null) {
            purchaseData.setProductName(data.getProductName());
            purchaseData.setProductStandard(data.getProductStandard());
            purchaseData.setProductUnit(data.getProductUnit());
            purchaseData.setStock(data.getStock());
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
                .le(StringUtils.hasText(queryPurchaseOrderDTO.getEndDate()), ReceiptPurchaseMain::getCreateTime, queryPurchaseOrderDTO.getEndDate());

        var queryResult = receiptPurchaseMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = getReceiptSubList(item.getId());
            var productNumber = calculateProductNumber(receiptSubList);
            var supplierName = getSupplierName(item.getSupplierId());
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

    @Override
    public Response<PurchaseOrderDetailVO> getPurchaseOrderDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var purchaseMain = getById(id);
        List<FileDataBO> fileList = commonService.getFileList(purchaseMain.getFileId());

        var receiptSubList = receiptPurchaseSubService.lambdaQuery()
                .eq(ReceiptPurchaseSub::getReceiptPurchaseMainId, id)
                .list();

        var tableData = new ArrayList<PurchaseDataBO>(receiptSubList.size() + 1);
        for (ReceiptPurchaseSub item : receiptSubList) {
            var purchaseData = createPurchaseDataFromReceiptSub(item);
            tableData.add(purchaseData);
        }

        var multipleAccountIds = parseAndCollectLongList(purchaseMain.getMultipleAccount());
        var multipleAccountAmounts = parseAndCollectLongList(purchaseMain.getMultipleAccountAmount());

        var purchaseOrderDetailVO = PurchaseOrderDetailVO.builder()
                .receiptNumber(purchaseMain.getReceiptNumber())
                .receiptDate(purchaseMain.getReceiptDate())
                .supplierId(purchaseMain.getSupplierId())
                .accountId(purchaseMain.getAccountId())
                .discountRate(purchaseMain.getDiscountRate())
                .discountAmount(purchaseMain.getDiscountAmount())
                .discountLastAmount(purchaseMain.getDiscountLastAmount())
                .multipleAccountIds(multipleAccountIds)
                .multipleAccountAmounts(multipleAccountAmounts)
                .deposit(purchaseMain.getDeposit())
                .remark(purchaseMain.getRemark())
                .tableData(tableData)
                .files(fileList)
                .build();

        return Response.responseData(purchaseOrderDetailVO);
    }

    @Override
    public Response<String> addOrUpdatePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = purchaseOrderDTO.getId() != null;

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
                    .discountAmount(purchaseOrderDTO.getDiscountAmount())
                    .discountLastAmount(purchaseOrderDTO.getDiscountLastAmount())
                    .deposit(purchaseOrderDTO.getDeposit())
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
}
