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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.*;
import com.wansenai.bo.retail.RetailReturnExportBO;
import com.wansenai.bo.retail.RetailReturnExportEnBO;
import com.wansenai.bo.retail.RetailShipmentsExportBO;
import com.wansenai.bo.retail.RetailShipmentsExportEnBO;
import com.wansenai.dto.receipt.retail.QueryRetailRefundDTO;
import com.wansenai.dto.receipt.retail.QueryShipmentsDTO;
import com.wansenai.dto.receipt.retail.RetailRefundDTO;
import com.wansenai.dto.receipt.retail.RetailShipmentsDTO;
import com.wansenai.dto.system.SystemMessageDTO;
import com.wansenai.entities.financial.FinancialAccount;
import com.wansenai.entities.product.ProductStock;
import com.wansenai.entities.product.ProductStockKeepUnit;
import com.wansenai.entities.receipt.ReceiptRetailMain;
import com.wansenai.entities.receipt.ReceiptRetailSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.entities.system.SysMsg;
import com.wansenai.mappers.product.ProductStockKeepUnitMapper;
import com.wansenai.mappers.product.ProductStockMapper;
import com.wansenai.mappers.receipt.ReceiptRetailMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.product.ProductService;
import com.wansenai.service.receipt.ReceiptRetailService;
import com.wansenai.service.receipt.ReceiptRetailSubService;
import com.wansenai.service.system.ISysMsgService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.MessageUtil;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.MessageConstants;
import com.wansenai.utils.constants.ReceiptConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.RetailCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.redis.RedisUtil;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.receipt.ReceiptRetailDetailVO;
import com.wansenai.vo.receipt.retail.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReceiptRetailServiceImpl extends ServiceImpl<ReceiptRetailMainMapper, ReceiptRetailMain> implements ReceiptRetailService {

    private final ReceiptRetailMainMapper receiptRetailMainMapper;

    private final ReceiptRetailSubService receiptRetailSubService;

    private final IFinancialAccountService accountService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    private final ProductStockMapper productStockMapper;

    private final ProductStockKeepUnitMapper productStockKeepUnitMapper;

    private final ProductService productService;

    private final CommonService commonService;

    private final ISysMsgService messageService;

    private final RedisUtil redisUtil;

    public ReceiptRetailServiceImpl(ReceiptRetailMainMapper receiptRetailMainMapper, ReceiptRetailSubService receiptRetailSubService, IFinancialAccountService accountService, ISysUserService userService, SysFileMapper fileMapper, ProductStockMapper productStockMapper, ProductStockKeepUnitMapper productStockKeepUnitMapper, ProductService productService, CommonService commonService, ISysMsgService messageService, RedisUtil redisUtil) {
        this.receiptRetailMainMapper = receiptRetailMainMapper;
        this.receiptRetailSubService = receiptRetailSubService;
        this.accountService = accountService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.productStockMapper = productStockMapper;
        this.productStockKeepUnitMapper = productStockKeepUnitMapper;
        this.productService = productService;
        this.commonService = commonService;
        this.messageService = messageService;
        this.redisUtil = redisUtil;
    }

    private String getAccountName(Long accountId) {
        return Optional.ofNullable(accountId)
                .map(accountService::getById)
                .map(FinancialAccount::getAccountName)
                .orElse(null);
    }

    private String getUserName(Long userId) {
        return (userId != null) ? userService.getById(userId).getName() : null;
    }

    private int calculateProductNumber(List<ReceiptRetailSub> subList) {
        return subList.stream()
                .mapToInt(sub -> sub.getProductNumber() != null ? sub.getProductNumber() : 0)
                .sum();
    }

    private final Map<Long, List<ReceiptRetailSub>> receiptSubListCache = new ConcurrentHashMap<>();

    private List<ReceiptRetailSub> getReceiptRetailList(Long receiptRetailMainId) {
        return receiptSubListCache.computeIfAbsent(receiptRetailMainId, id ->
                receiptRetailSubService.lambdaQuery()
                        .eq(ReceiptRetailSub::getReceiptMainId, id)
                        .list()
        );
    }

    private Response<String> deleteRetail(List<Long> ids, RetailCodeEnum successEnum, RetailCodeEnum errorEnum) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateStatusResult = lambdaUpdate()
                .in(ReceiptRetailMain::getId, ids)
                .set(ReceiptRetailMain::getDeleteFlag, CommonConstants.DELETED)
                .update();

        var updateSubResult = receiptRetailSubService.lambdaUpdate()
                .in(ReceiptRetailSub::getReceiptMainId, ids)
                .set(ReceiptRetailSub::getDeleteFlag, CommonConstants.DELETED)
                .update();

        if (updateStatusResult &&updateSubResult) {
            return Response.responseMsg(successEnum);
        } else {
            return Response.responseMsg(errorEnum);
        }
    }

    private Response<String> updateRetailStatus(List<Long> ids, Integer status, RetailCodeEnum successEnum, RetailCodeEnum errorEnum) {
        if (ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .in(ReceiptRetailMain::getId, ids)
                .set(ReceiptRetailMain::getStatus, status)
                .update();
        if (updateResult) {
            return Response.responseMsg(successEnum);
        } else {
            return Response.responseMsg(errorEnum);
        }
    }

    private void updateProductStock(List<ReceiptRetailSub> receiptSubList, int stockType) {
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

    private ShipmentsDataBO createShipmentsDataFromReceiptSub(ReceiptRetailSub item) {
        var shipmentBo = ShipmentsDataBO.builder()
                .productId(item.getProductId())
                .barCode(item.getProductBarcode())
                .productNumber(item.getProductNumber())
                .unitPrice(item.getUnitPrice())
                .amount(item.getTotalAmount())
                .warehouseId(item.getWarehouseId())
                .build();
        var data = productStockMapper.getProductSkuByBarCode(item.getProductBarcode(), item.getWarehouseId());
        if (data != null) {
            shipmentBo.setWarehouseId(data.getWarehouseId());
            shipmentBo.setProductName(data.getProductName());
            shipmentBo.setProductStandard(data.getProductStandard());
            shipmentBo.setProductUnit(data.getProductUnit());
            shipmentBo.setProductModel(data.getProductModel());
            shipmentBo.setProductColor(data.getProductColor());
            shipmentBo.setStock(data.getStock());

            if (data.getWarehouseId() != null) {
                shipmentBo.setWarehouseName(commonService.getWarehouseName(data.getWarehouseId()));
            }
        }
        return shipmentBo;
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
                .le(StringUtils.hasText(shipmentsDTO.getEndDate()), ReceiptRetailMain::getCreateTime, shipmentsDTO.getEndDate())
                .orderByDesc(ReceiptRetailMain::getCreateTime);

        var queryResult = receiptRetailMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);

            var memberName = commonService.getMemberName(item.getMemberId());
            var crateBy = getUserName(item.getCreateBy());

            var retailShipmentsVO = RetailShipmentsVO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalAmount())
                    .collectionAmount(item.getChangeAmount())
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
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, receiptRetailMain.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var memberName = commonService.getMemberName(receiptRetailMain.getMemberId());
            var crateBy = getUserName(receiptRetailMain.getCreateBy());

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

    private List<RetailShipmentsExportBO> getRetailShipmentsBOList(QueryShipmentsDTO shipmentsDTO) {
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

        var result = new ArrayList<RetailShipmentsExportBO>(query.size() + 2);
        for (ReceiptRetailMain receiptRetailMain : query) {
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, receiptRetailMain.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var memberName = commonService.getMemberName(receiptRetailMain.getMemberId());
            var crateBy = getUserName(receiptRetailMain.getCreateBy());

            var retailShipmentsExportBO = RetailShipmentsExportBO.builder()
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
            result.add(retailShipmentsExportBO);
        }
        return result;
    }

    private List<RetailShipmentsExportEnBO> getRetailShipmentsBOEnList(QueryShipmentsDTO shipmentsDTO) {
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

        var result = new ArrayList<RetailShipmentsExportEnBO>(query.size() + 2);
        for (ReceiptRetailMain receiptRetailMain : query) {
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, receiptRetailMain.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var memberName = commonService.getMemberName(receiptRetailMain.getMemberId());
            var crateBy = getUserName(receiptRetailMain.getCreateBy());

            var retailShipmentsExportEnBO = RetailShipmentsExportEnBO.builder()
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
            result.add(retailShipmentsExportEnBO);
        }
        return result;
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateRetailShipments(RetailShipmentsDTO shipmentsDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = shipmentsDTO.getId() != null;

        var fid = processFiles(shipmentsDTO.getFiles(), shipmentsDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

        if (isUpdate) {
            var beforeReceipt = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, shipmentsDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 1);
            }

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
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var updateSubResult = receiptRetailSubService.saveBatch(receiptList);
            updateProductStock(receiptList, 2);

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

            // 更新余额 如果之前已经修改过那么就需要减去之前的金额 再加上现在的金额 如果之前没有修改过那么就直接加上现在的金额

            var account = accountService.getById(shipmentsDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var changeAmount = shipmentsDTO.getCollectAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(ReceiptRetailSub::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.subtract(beforeChangeAmount);
                if (changeAmount != null) {
                    accountBalance = accountBalance.add(changeAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }
            var systemLanguage = userService.getUserSystemLanguage(userId);

            if (updateMainResult && updateSubResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS);
                }
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
                }
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR_EN);
            }
        } else {

            for (ShipmentsDataBO check : shipmentsDTO.getTableData()) {
                if (check.getProductId() == null) {
                    return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY.getCode(), "条码没有找到对应的商品,请检查条码是否正确");
                }
            }
            var id = SnowflakeIdUtil.nextId();
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
            updateProductStock(receiptList, 2);

            var account = accountService.getById(shipmentsDTO.getAccountId());
            if (account != null) {
                // 更新余额
                var accountBalance = Optional.ofNullable(account.getCurrentAmount()).orElse(BigDecimal.ZERO);
                var changeAmount = shipmentsDTO.getReceiptAmount();
                if (changeAmount != null) {
                    accountBalance = accountBalance.add(changeAmount);
                    account.setId(shipmentsDTO.getAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }
            // send System Message
            var systemLanguage = userService.getUserSystemLanguage(userId);
            var title = "";
            var message = "";
            var description = "";
            if ("zh_CN".equals(systemLanguage)) {
                title = MessageUtil.RetailShipmentsZhCnSubject();
                message = MessageUtil.RetailShipmentsZhCnTemplate(receiptMain.getReceiptNumber());
                description = MessageUtil.RetailShipmentsZhCnDescription(receiptMain.getReceiptNumber());
            } else if ("en_US".equals(systemLanguage)) {
                title = MessageUtil.RetailShipmentsEnUsSubject();
                message = MessageUtil.RetailShipmentsEnUsTemplate(receiptMain.getReceiptNumber());
                description = MessageUtil.RetailShipmentsEnUsDescription(receiptMain.getReceiptNumber());
            }
            var messageDTO = SystemMessageDTO.builder()
                    .userId(userId)
                    .type("todo")
                    .msgTitle(title)
                    .msgContent(message)
                    .description(description)
                    .build();
            messageService.insertMessage(messageDTO);

            if (saveMainResult && saveSubResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_SUCCESS);
                }
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_ERROR);
                }
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_SHIPMENTS_ERROR_EN);
            }
        }
    }

    @Override
    public Response<String> deleteRetailShipments(List<Long> ids) {
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            return deleteRetail(ids, RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_SUCCESS, RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_ERROR);
        }
        return deleteRetail(ids, RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_SUCCESS_EN, RetailCodeEnum.DELETE_RETAIL_SHIPMENTS_ERROR_EN);
    }

    private RetailShipmentsDetailVO createRetailShipmentsDetail(ReceiptRetailMain shipment) {
        var fileList = commonService.getFileList(shipment.getFileId());
        var receiptSubList = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getReceiptMainId, shipment.getId())
                .list();

        var tableData = receiptSubList.stream()
                .map(this::createShipmentsDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));

        return RetailShipmentsDetailVO.builder()
                .receiptNumber(shipment.getReceiptNumber())
                .receiptDate(shipment.getReceiptDate())
                .memberId(shipment.getMemberId())
                .memberName(commonService.getMemberName(shipment.getMemberId()))
                .accountName(getAccountName(shipment.getAccountId()))
                .accountId(shipment.getAccountId())
                .paymentType(shipment.getPaymentType())
                .collectAmount(shipment.getChangeAmount())
                .receiptAmount(shipment.getTotalAmount())
                .backAmount(shipment.getBackAmount())
                .remark(shipment.getRemark())
                .tableData(tableData)
                .files(fileList)
                .status(shipment.getStatus())
                .build();
    }

    @Override
    public Response<RetailShipmentsDetailVO> getRetailShipmentsDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = getById(id);
        var retailShipmentsDetailVO = createRetailShipmentsDetail(shipment);
        return Response.responseData(retailShipmentsDetailVO);
    }

    @Override
    public Response<RetailShipmentsDetailVO> getLinkRetailShipmentsDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var shipment = lambdaQuery()
                .eq(ReceiptRetailMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        var retailShipmentsDetailVO = createRetailShipmentsDetail(shipment);
        return Response.responseData(retailShipmentsDetailVO);
    }

    @Override
    public Response<String> updateRetailShipmentsStatus(List<Long> ids, Integer status) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var receiptList = lambdaQuery()
                .in(ReceiptRetailMain::getId, ids)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        List<SystemMessageDTO> messageDTO = new ArrayList<>();
        if (status == CommonConstants.REVIEWED) {
            for (ReceiptRetailMain receiptRetailMain : receiptList) {
                // send System Message
                var userId = receiptRetailMain.getCreateBy();
                // 检查是否存在该id的消息
                var dataList = redisUtil.lGet(MessageConstants.SYSTEM_MESSAGE_PREFIX + userId, 0, -1);
                if (!dataList.isEmpty()) {
                    var deleteMessageIds = new ArrayList<Long>();
                    dataList.forEach(item -> {
                        var msg = JSONObject.parseObject(item.toString(), SysMsg.class);
                        if(Objects.nonNull(msg) && msg.getDescription().contains(receiptRetailMain.getReceiptNumber())) {
                            redisUtil.lRemove(MessageConstants.SYSTEM_MESSAGE_PREFIX + userId, 1, item);
                            deleteMessageIds.add(msg.getId());
                        }
                    });
                    if (!deleteMessageIds.isEmpty()) {
                        messageService.removeByIds(deleteMessageIds);
                    }
                    var systemLanguage = userService.getUserSystemLanguage(userId);
                    var title = "";
                    var message = "";
                    var description = "";
                    if ("zh_CN".equals(systemLanguage)) {
                        title = MessageUtil.RetailShipmentsAuditedZhCnSubject();
                        message = MessageUtil.RetailShipmentsAuditedZhCnTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailShipmentsZhCnDescription(receiptRetailMain.getReceiptNumber());
                    } else if ("en_US".equals(systemLanguage)) {
                        title = MessageUtil.RetailShipmentsAuditedEnUsSubject();
                        message = MessageUtil.RetailShipmentsAuditedEnUsTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailShipmentsEnUsDescription(receiptRetailMain.getReceiptNumber());
                    }
                    var msgDTO = SystemMessageDTO.builder()
                            .userId(userId)
                            .type("notice")
                            .msgTitle(title)
                            .msgContent(message)
                            .description(description)
                            .status(MessageConstants.SYSTEM_MESSAGE_UNREAD)
                            .build();
                    messageDTO.add(msgDTO);
                } else {
                    var systemLanguage = userService.getUserSystemLanguage(userId);
                    var title = "";
                    var message = "";
                    var description = "";
                    if ("zh_CN".equals(systemLanguage)) {
                        title = MessageUtil.RetailShipmentsAuditedZhCnSubject();
                        message = MessageUtil.RetailShipmentsAuditedZhCnTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailShipmentsZhCnDescription(receiptRetailMain.getReceiptNumber());
                    } else if ("en_US".equals(systemLanguage)) {
                        title = MessageUtil.RetailShipmentsAuditedEnUsSubject();
                        message = MessageUtil.RetailShipmentsAuditedEnUsTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailShipmentsEnUsDescription(receiptRetailMain.getReceiptNumber());
                    }
                    var msgDTO = SystemMessageDTO.builder()
                            .userId(userId)
                            .type("notice")
                            .msgTitle(title)
                            .msgContent(message)
                            .description(description)
                            .status(MessageConstants.SYSTEM_MESSAGE_UNREAD)
                            .build();
                    messageDTO.add(msgDTO);
                }
            }
            messageService.insertBatchMessage(messageDTO);
        }

        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            return updateRetailStatus(ids, status, RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS, RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR);
        }
        return updateRetailStatus(ids, status, RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_SUCCESS_EN, RetailCodeEnum.UPDATE_RETAIL_SHIPMENTS_ERROR_EN);
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
                .le(StringUtils.hasText(refundDTO.getEndDate()), ReceiptRetailMain::getCreateTime, refundDTO.getEndDate())
                .orderByDesc(ReceiptRetailMain::getCreateTime);

        var queryResult = receiptRetailMainMapper.selectPage(page, queryWrapper);

        queryResult.getRecords().forEach(item -> {
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var memberName = commonService.getMemberName(item.getMemberId());
            var crateBy = getUserName(item.getCreateBy());

            var retailRefundVO = RetailRefundVO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalAmount())
                    .paymentAmount(item.getChangeAmount())
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

    private List<RetailReturnExportBO> getRetailRefundList(QueryRetailRefundDTO refundDTO) {
        List<RetailReturnExportBO> result = new ArrayList<>();
        var retailRefundList = lambdaQuery()
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
                .le(StringUtils.hasText(refundDTO.getEndDate()), ReceiptRetailMain::getCreateTime, refundDTO.getEndDate())
                .list();

        retailRefundList.forEach(item -> {
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var memberName = commonService.getMemberName(item.getMemberId());
            var crateBy = getUserName(item.getCreateBy());

            var retailReturnExportBO = RetailReturnExportBO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalAmount())
                    .paymentAmount(item.getChangeAmount())
                    .backAmount(item.getBackAmount())
                    .status(item.getStatus())
                    .build();
            result.add(retailReturnExportBO);
        });

        return result;
    }

    private List<RetailReturnExportEnBO> getRetailRefundEnList(QueryRetailRefundDTO refundDTO) {
        List<RetailReturnExportEnBO> result = new ArrayList<>();
        var retailRefundList = lambdaQuery()
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
                .le(StringUtils.hasText(refundDTO.getEndDate()), ReceiptRetailMain::getCreateTime, refundDTO.getEndDate())
                .list();

        retailRefundList.forEach(item -> {
            var receiptSubList = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, item.getId())
                    .list();
            var productNumber = calculateProductNumber(receiptSubList);
            var memberName = commonService.getMemberName(item.getMemberId());
            var crateBy = getUserName(item.getCreateBy());

            var retailReturnExportEnBO = RetailReturnExportEnBO.builder()
                    .id(item.getId())
                    .memberName(memberName)
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .productInfo(item.getRemark())
                    .operator(crateBy)
                    .productNumber(productNumber)
                    .totalPrice(item.getTotalAmount())
                    .paymentAmount(item.getChangeAmount())
                    .backAmount(item.getBackAmount())
                    .status(item.getStatus())
                    .build();
            result.add(retailReturnExportEnBO);
        });

        return result;
    }

    @Override
    @Transactional
    public Response<String> addOrUpdateRetailRefund(RetailRefundDTO refundDTO) {
        var userId = userService.getCurrentUserId();
        var isUpdate = refundDTO.getId() != null;

        var fid = processFiles(refundDTO.getFiles(), refundDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);

        if (isUpdate) {
            var beforeReceipt = receiptRetailSubService.lambdaQuery()
                    .eq(ReceiptRetailSub::getReceiptMainId, refundDTO.getId())
                    .list();
            if (!beforeReceipt.isEmpty()) {
                updateProductStock(beforeReceipt, 2);
            }

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
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            var updateSubResult = receiptRetailSubService.saveBatch(receiptList);
            updateProductStock(receiptList, 1);

            // 更新余额 如果之前已经修改过那么就需要加上之前的金额 然后再减去现在的金额 如果之前没有修改过那么就直接加上现在的金额 因为这个是退货 所以是负数
            var account = accountService.getById(refundDTO.getAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var changeAmount = refundDTO.getReceiptAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(ReceiptRetailSub::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.add(beforeChangeAmount);
                if (changeAmount != null) {
                    accountBalance = accountBalance.subtract(changeAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }

            var systemLanguage = userService.getUserSystemLanguage(userId);
            if (updateMainResult && updateSubResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS);
                }
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR);
                }
                return Response.responseMsg(RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR_EN);
            }
        } else {
            for (ShipmentsDataBO check : refundDTO.getTableData()) {
                if (check.getProductId() == null) {
                    return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY.getCode(), "条码没有找到对应的商品,请检查条码是否正确");
                }
            }
            var id = SnowflakeIdUtil.nextId();
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
            updateProductStock(receiptList, 1);

            var account = accountService.getById(refundDTO.getAccountId());
            if (account != null) {
                // 更新余额
                var accountBalance = account.getCurrentAmount();
                var changeAmount = refundDTO.getReceiptAmount();
                if (changeAmount != null) {
                    accountBalance = accountBalance.subtract(changeAmount);
                    account.setId(refundDTO.getAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }
            // send System Message
            var systemLanguage = userService.getUserSystemLanguage(userId);
            var title = "";
            var message = "";
            var description = "";
            if ("zh_CN".equals(systemLanguage)) {
                title = MessageUtil.RetailRefundZhCnSubject();
                message = MessageUtil.RetailRefundZhCnTemplate(receiptMain.getReceiptNumber());
                description = MessageUtil.RetailRefundZhCnDescription(receiptMain.getReceiptNumber());
            } else if ("en_US".equals(systemLanguage)) {
                title = MessageUtil.RetailRefundEnUsSubject();
                message = MessageUtil.RetailRefundEnUsTemplate(receiptMain.getReceiptNumber());
                description = MessageUtil.RetailRefundEnUsDescription(receiptMain.getReceiptNumber());
            }
            var messageDTO = SystemMessageDTO.builder()
                    .userId(userId)
                    .type("todo")
                    .msgTitle(title)
                    .msgContent(message)
                    .description(description)
                    .build();
            messageService.insertMessage(messageDTO);

            if (saveMainResult && saveSubResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_SUCCESS);
                }
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_SUCCESS_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_ERROR);
                }
                return Response.responseMsg(RetailCodeEnum.ADD_RETAIL_REFUND_ERROR_EN);
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

    private RetailRefundDetailVO createRetailRefundDetail(ReceiptRetailMain refund) {
        var fileList = commonService.getFileList(refund.getFileId());
        var receiptSubList = receiptRetailSubService.lambdaQuery()
                .eq(ReceiptRetailSub::getReceiptMainId, refund.getId())
                .eq(ReceiptRetailSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var tableData = receiptSubList.stream()
                .map(this::createShipmentsDataFromReceiptSub)
                .collect(Collectors.toCollection(ArrayList::new));

        return RetailRefundDetailVO.builder()
                .receiptNumber(refund.getReceiptNumber())
                .receiptDate(refund.getReceiptDate())
                .memberId(refund.getMemberId())
                .memberName(commonService.getMemberName(refund.getMemberId()))
                .accountId(refund.getAccountId())
                .accountName(getAccountName(refund.getAccountId()))
                .otherReceipt(refund.getOtherReceipt())
                .paymentAmount(refund.getChangeAmount())
                .paymentType(refund.getPaymentType())
                .receiptAmount(refund.getTotalAmount())
                .backAmount(refund.getBackAmount())
                .remark(refund.getRemark())
                .status(refund.getStatus())
                .tableData(tableData)
                .files(fileList)
                .status(refund.getStatus())
                .build();
    }

    @Override
    public Response<RetailRefundDetailVO> getRetailRefundDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var refund = getById(id);
        var retailRefundDetailVO = createRetailRefundDetail(refund);
        return Response.responseData(retailRefundDetailVO);
    }

    @Override
    public Response<RetailRefundDetailVO> getLinkRetailRefundDetail(String receiptNumber) {
        if (!StringUtils.hasLength(receiptNumber)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var refund = lambdaQuery()
                .eq(ReceiptRetailMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one();
        var retailRefundDetailVO = createRetailRefundDetail(refund);
        return Response.responseData(retailRefundDetailVO);
    }

    @Override
    public Response<String> deleteRetailRefund(List<Long> ids) {
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            return deleteRetail(ids, RetailCodeEnum.DELETE_RETAIL_REFUND_SUCCESS, RetailCodeEnum.DELETE_RETAIL_REFUND_ERROR);
        }
        return deleteRetail(ids, RetailCodeEnum.DELETE_RETAIL_REFUND_SUCCESS_EN, RetailCodeEnum.DELETE_RETAIL_REFUND_ERROR_EN);
    }

    @Override
    public Response<String> updateRetailRefundStatus(List<Long> ids, Integer status) {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var receiptList = lambdaQuery()
                .in(ReceiptRetailMain::getId, ids)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        List<SystemMessageDTO> messageDTO = new ArrayList<>();
        if (status == CommonConstants.REVIEWED) {
            for (ReceiptRetailMain receiptRetailMain : receiptList) {
                // send System Message
                var userId = receiptRetailMain.getCreateBy();
                // 检查是否存在该id的消息
                var dataList = redisUtil.lGet(MessageConstants.SYSTEM_MESSAGE_PREFIX + userId, 0, -1);
                if (!dataList.isEmpty()) {
                    var deleteMessageIds = new ArrayList<Long>();
                    dataList.forEach(item -> {
                        var msg = JSONObject.parseObject(item.toString(), SysMsg.class);
                        if(Objects.nonNull(msg) && msg.getDescription().contains(receiptRetailMain.getReceiptNumber())) {
                            redisUtil.lRemove(MessageConstants.SYSTEM_MESSAGE_PREFIX + userId, 1, item);
                            deleteMessageIds.add(msg.getId());
                        }
                    });
                    if (!deleteMessageIds.isEmpty()) {
                        messageService.removeByIds(deleteMessageIds);
                    }
                    var systemLanguage = userService.getUserSystemLanguage(userId);
                    var title = "";
                    var message = "";
                    var description = "";
                    if ("zh_CN".equals(systemLanguage)) {
                        title = MessageUtil.RetailRefundAuditedZhCnSubject();
                        message = MessageUtil.RetailRefundAuditedZhCnTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailRefundZhCnDescription(receiptRetailMain.getReceiptNumber());
                    } else if ("en_US".equals(systemLanguage)) {
                        title = MessageUtil.RetailRefundAuditedEnUsSubject();
                        message = MessageUtil.RetailRefundAuditedEnUsTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailRefundEnUsDescription(receiptRetailMain.getReceiptNumber());
                    }
                    var msgDTO = SystemMessageDTO.builder()
                            .userId(userId)
                            .type("notice")
                            .msgTitle(title)
                            .msgContent(message)
                            .description(description)
                            .status(MessageConstants.SYSTEM_MESSAGE_UNREAD)
                            .build();
                    messageDTO.add(msgDTO);
                } else {
                    var systemLanguage = userService.getUserSystemLanguage(userId);
                    var title = "";
                    var message = "";
                    var description = "";
                    if ("zh_CN".equals(systemLanguage)) {
                        title = MessageUtil.RetailRefundAuditedZhCnSubject();
                        message = MessageUtil.RetailRefundAuditedZhCnTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailRefundZhCnDescription(receiptRetailMain.getReceiptNumber());
                    } else if ("en_US".equals(systemLanguage)) {
                        title = MessageUtil.RetailRefundAuditedEnUsSubject();
                        message = MessageUtil.RetailRefundAuditedEnUsTemplate(receiptRetailMain.getReceiptNumber());
                        description = MessageUtil.RetailRefundEnUsDescription(receiptRetailMain.getReceiptNumber());
                    }
                    var msgDTO = SystemMessageDTO.builder()
                            .userId(userId)
                            .type("notice")
                            .msgTitle(title)
                            .msgContent(message)
                            .description(description)
                            .status(MessageConstants.SYSTEM_MESSAGE_UNREAD)
                            .build();
                    messageDTO.add(msgDTO);
                }
            }
            messageService.insertBatchMessage(messageDTO);
        }
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            return updateRetailStatus(ids, status, RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS, RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR);
        }
       return updateRetailStatus(ids, status, RetailCodeEnum.UPDATE_RETAIL_REFUND_SUCCESS_EN, RetailCodeEnum.UPDATE_RETAIL_REFUND_ERROR_EN);
    }

    @Override
    public void exportRetailShipmentsExcel(QueryShipmentsDTO queryShipmentsDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            var mainData = getRetailShipmentsBOList(queryShipmentsDTO);
            if (!mainData.isEmpty()) {
                if (queryShipmentsDTO.getIsExportDetail()) {
                    var subData = new ArrayList<ShipmentsDataExportBO>();
                    for (RetailShipmentsExportBO shipmentsExportBO : mainData) {
                        var detail = getRetailShipmentsDetail(shipmentsExportBO.getId()).getData().getTableData();
                        detail.forEach(item -> {
                            var shipmentBo = ShipmentsDataExportBO.builder()
                                    .memberName(shipmentsExportBO.getMemberName())
                                    .receiptNumber(shipmentsExportBO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .stock(item.getStock())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(shipmentBo);
                        });
                    }
                    exportMap.put("零售出库单明细", ExcelUtils.getSheetData(subData));
                }
                exportMap.put("零售出库单", ExcelUtils.getSheetData(mainData));
                ExcelUtils.exportManySheet(response, "零售出库单", exportMap);
            }
        } else {
            var mainEnData = getRetailShipmentsBOEnList(queryShipmentsDTO);
            if (!mainEnData.isEmpty()) {
                if (queryShipmentsDTO.getIsExportDetail()) {
                    var subEnData = new ArrayList<ShipmentsDataExportEnBO>();
                    for (RetailShipmentsExportEnBO shipmentsExportEnBO : mainEnData) {
                        var detail = getRetailShipmentsDetail(shipmentsExportEnBO.getId()).getData().getTableData();
                        detail.forEach(item -> {
                            var shipmentBo = ShipmentsDataExportEnBO.builder()
                                    .memberName(shipmentsExportEnBO.getMemberName())
                                    .receiptNumber(shipmentsExportEnBO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .stock(item.getStock())
                                    .remark(item.getRemark())
                                    .build();
                            subEnData.add(shipmentBo);
                        });
                    }
                    exportMap.put("Retail Outbound Document Details", ExcelUtils.getSheetData(subEnData));
                }
                exportMap.put("Retail Outbound Document", ExcelUtils.getSheetData(mainEnData));
                ExcelUtils.exportManySheet(response, "Retail Outbound Document", exportMap);
            }
        }
    }

    @Override
    public void exportShipmentsDetailExcel(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(ReceiptRetailMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one()
                .getId();
        var detail = getRetailShipmentsDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
            var fileName = "";
            if ("zh_CN".equals(systemLanguage)) {
                var exportData = new ArrayList<ShipmentsDataExportBO>();
                tableData.forEach(item -> {
                    var shipmentBo = new ShipmentsDataExportBO();
                    shipmentBo.setMemberName(data.getMemberName());
                    shipmentBo.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, shipmentBo);
                    exportData.add(shipmentBo);
                });
                fileName = data.getReceiptNumber() + "-零售出库单明细";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
            } else {
                var exportData = new ArrayList<ShipmentsDataExportEnBO>();
                tableData.forEach(item -> {
                    var shipmentEnBo = new ShipmentsDataExportEnBO();
                    shipmentEnBo.setMemberName(data.getMemberName());
                    shipmentEnBo.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, shipmentEnBo);
                    exportData.add(shipmentEnBo);
                });
                fileName = data.getReceiptNumber() + "- Retail Outbound Document Details";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
            }
        }
    }

    @Override
    public void exportRetailRefundExcel(QueryRetailRefundDTO queryRetailRefundDTO, HttpServletResponse response) throws Exception {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            var mainData = getRetailRefundList(queryRetailRefundDTO);
            if (!mainData.isEmpty()) {
                if (queryRetailRefundDTO.getIsExportDetail()) {
                    var subData = new ArrayList<ShipmentsDataExportBO>();
                    for (RetailReturnExportBO returnExportBO : mainData) {
                        var detail = getRetailRefundDetail(returnExportBO.getId()).getData().getTableData();
                        detail.forEach(item -> {
                            var shipmentBo = ShipmentsDataExportBO.builder()
                                    .memberName(returnExportBO.getMemberName())
                                    .receiptNumber(returnExportBO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .stock(item.getStock())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(shipmentBo);
                        });
                    }
                    exportMap.put("零售退货单明细", ExcelUtils.getSheetData(subData));
                }
                exportMap.put("零售退货单", ExcelUtils.getSheetData(mainData));
                ExcelUtils.exportManySheet(response, "零售退货单", exportMap);
            }
        } else {
            var mainEnData = getRetailRefundEnList(queryRetailRefundDTO);
            if (!mainEnData.isEmpty()) {
                if (queryRetailRefundDTO.getIsExportDetail()) {
                    var subEnData = new ArrayList<ShipmentsDataExportBO>();
                    for (RetailReturnExportEnBO returnExportEnBO : mainEnData) {
                        var detail = getRetailRefundDetail(returnExportEnBO.getId()).getData().getTableData();
                        detail.forEach(item -> {
                            var shipmentBo = ShipmentsDataExportBO.builder()
                                    .memberName(returnExportEnBO.getMemberName())
                                    .receiptNumber(returnExportEnBO.getReceiptNumber())
                                    .warehouseName(item.getWarehouseName())
                                    .barCode(item.getBarCode())
                                    .productName(item.getProductName())
                                    .productStandard(item.getProductStandard())
                                    .productModel(item.getProductModel())
                                    .productColor(item.getProductColor())
                                    .productUnit(item.getProductUnit())
                                    .productNumber(item.getProductNumber())
                                    .unitPrice(item.getUnitPrice())
                                    .amount(item.getAmount())
                                    .stock(item.getStock())
                                    .remark(item.getRemark())
                                    .build();
                            subEnData.add(shipmentBo);
                        });
                    }
                    exportMap.put("Retail Return Document Details", ExcelUtils.getSheetData(subEnData));
                }
                exportMap.put("Retail Return Document", ExcelUtils.getSheetData(mainEnData));
                ExcelUtils.exportManySheet(response, "Retail Return Document", exportMap);
            }
        }
    }

    @Override
    public void exportRefundDetailExcel(String receiptNumber, HttpServletResponse response) throws IOException {
        var id = lambdaQuery()
                .eq(ReceiptRetailMain::getReceiptNumber, receiptNumber)
                .eq(ReceiptRetailMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .one()
                .getId();
        var detail = getRetailRefundDetail(id);
        if (detail != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
            if ("zh_CN".equals(systemLanguage)) {
                var exportData = new ArrayList<ShipmentsDataExportBO>();
                tableData.forEach(item -> {
                    var shipmentBo = new ShipmentsDataExportBO();
                    shipmentBo.setMemberName(data.getMemberName());
                    shipmentBo.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, shipmentBo);
                    exportData.add(shipmentBo);
                });
                var fileName = data.getReceiptNumber() + "-零售退货单明细";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
            } else {
                var exportData = new ArrayList<ShipmentsDataExportEnBO>();
                tableData.forEach(item -> {
                    var shipmentEnBo = new ShipmentsDataExportEnBO();
                    shipmentEnBo.setMemberName(data.getMemberName());
                    shipmentEnBo.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, shipmentEnBo);
                    exportData.add(shipmentEnBo);
                });
                var fileName = data.getReceiptNumber() + "- Retail Return Document Details";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
            }
        }
    }

}
