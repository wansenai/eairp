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
package com.wansenai.service.financial.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.PaymentBO;
import com.wansenai.bo.financial.PaymentDataExportBO;
import com.wansenai.bo.financial.PaymentDataExportEnBO;
import com.wansenai.bo.financial.PaymentExportBO;
import com.wansenai.bo.financial.PaymentExportEnBO;
import com.wansenai.dto.financial.AddOrUpdatePaymentDTO;
import com.wansenai.dto.financial.QueryPaymentDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.financial.FinancialMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.FinancialSubService;
import com.wansenai.service.financial.PaymentReceiptService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.CollectionPaymentCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.PaymentDetailVO;
import com.wansenai.vo.financial.PaymentVO;
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
public class PaymentReceiptServiceImpl extends ServiceImpl<FinancialMainMapper, FinancialMain> implements PaymentReceiptService {

    private final FinancialSubService financialSubService;

    private final CommonService commonService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    public PaymentReceiptServiceImpl(FinancialSubService financialSubService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper) {
        this.financialSubService = financialSubService;
        this.commonService = commonService;
        this.userService = userService;
        this.fileMapper = fileMapper;
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
    public Response<Page<PaymentVO>> getPaymentReceiptPageList(QueryPaymentDTO queryPaymentDTO) {
        var result = new Page<PaymentVO>();
        var page = new Page<FinancialMain>(queryPaymentDTO.getPage(), queryPaymentDTO.getPageSize());

        var financialMainPage = lambdaQuery()
                .eq(queryPaymentDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, queryPaymentDTO.getFinancialPersonId())
                .eq(queryPaymentDTO.getAccountId() != null, FinancialMain::getAccountId, queryPaymentDTO.getAccountId())
                .eq(queryPaymentDTO.getStatus() != null, FinancialMain::getStatus, queryPaymentDTO.getStatus())
                .eq(queryPaymentDTO.getSupplierId() != null, FinancialMain::getRelatedPersonId, queryPaymentDTO.getSupplierId())
                .eq(StringUtils.hasLength(queryPaymentDTO.getReceiptNumber()), FinancialMain::getReceiptNumber, queryPaymentDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryPaymentDTO.getRemark()), FinancialMain::getRemark, queryPaymentDTO.getRemark())
                .ge(StringUtils.hasLength(queryPaymentDTO.getStartDate()), FinancialMain::getReceiptDate, queryPaymentDTO.getStartDate())
                .le(StringUtils.hasLength(queryPaymentDTO.getEndDate()), FinancialMain::getReceiptDate, queryPaymentDTO.getEndDate())
                .eq(FinancialMain::getType, "付款")
                .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .orderByDesc(FinancialMain::getCreateTime)
                .page(page);

        var paymentVOList = new ArrayList<PaymentVO>(financialMainPage.getRecords().size() + 1);
        financialMainPage.getRecords().forEach(item -> {
            var paymentVo = PaymentVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .supplierName(commonService.getSupplierName(item.getRelatedPersonId()))
                    .receiptDate(item.getReceiptDate())
                    .financialPerson(commonService.getOperatorName(item.getOperatorId()))
                    .paymentAccountName(commonService.getAccountName(item.getAccountId()))
                    .totalPaymentAmount(item.getTotalAmount())
                    .discountAmount(item.getDiscountAmount())
                    .actualPaymentAmount(item.getChangeAmount())
                    .status(item.getStatus())
                    .remark(item.getRemark())
                    .build();

            paymentVOList.add(paymentVo);
        });
        result.setRecords(paymentVOList);
        result.setTotal(financialMainPage.getTotal());
        return Response.responseData(result);
    }

    private List<PaymentExportBO> getPaymentReceiptList(QueryPaymentDTO queryPaymentDTO) {
        var financialMainList = lambdaQuery()
                .eq(queryPaymentDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, queryPaymentDTO.getFinancialPersonId())
                .eq(queryPaymentDTO.getAccountId() != null, FinancialMain::getAccountId, queryPaymentDTO.getAccountId())
                .eq(queryPaymentDTO.getStatus() != null, FinancialMain::getStatus, queryPaymentDTO.getStatus())
                .eq(queryPaymentDTO.getSupplierId() != null, FinancialMain::getRelatedPersonId, queryPaymentDTO.getSupplierId())
                .eq(StringUtils.hasLength(queryPaymentDTO.getReceiptNumber()), FinancialMain::getReceiptNumber, queryPaymentDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryPaymentDTO.getRemark()), FinancialMain::getRemark, queryPaymentDTO.getRemark())
                .ge(StringUtils.hasLength(queryPaymentDTO.getStartDate()), FinancialMain::getReceiptDate, queryPaymentDTO.getStartDate())
                .le(StringUtils.hasLength(queryPaymentDTO.getEndDate()), FinancialMain::getReceiptDate, queryPaymentDTO.getEndDate())
                .eq(FinancialMain::getType, "付款")
                .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var paymentExportBOList = new ArrayList<PaymentExportBO>(financialMainList.size() + 1);
        financialMainList.forEach(item -> {
            var paymentExportBO = PaymentExportBO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .supplierName(commonService.getSupplierName(item.getRelatedPersonId()))
                    .receiptDate(item.getReceiptDate())
                    .financialPerson(commonService.getOperatorName(item.getOperatorId()))
                    .paymentAccountName(commonService.getAccountName(item.getAccountId()))
                    .totalPaymentAmount(item.getTotalAmount())
                    .discountAmount(item.getDiscountAmount())
                    .actualPaymentAmount(item.getChangeAmount())
                    .status(item.getStatus())
                    .remark(item.getRemark())
                    .build();

            paymentExportBOList.add(paymentExportBO);
        });
        return paymentExportBOList;
    }

    private List<PaymentExportEnBO> getPaymentReceiptEnList(QueryPaymentDTO queryPaymentDTO) {
        var financialMainList = lambdaQuery()
                .eq(queryPaymentDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, queryPaymentDTO.getFinancialPersonId())
                .eq(queryPaymentDTO.getAccountId() != null, FinancialMain::getAccountId, queryPaymentDTO.getAccountId())
                .eq(queryPaymentDTO.getStatus() != null, FinancialMain::getStatus, queryPaymentDTO.getStatus())
                .eq(queryPaymentDTO.getSupplierId() != null, FinancialMain::getRelatedPersonId, queryPaymentDTO.getSupplierId())
                .eq(StringUtils.hasLength(queryPaymentDTO.getReceiptNumber()), FinancialMain::getReceiptNumber, queryPaymentDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryPaymentDTO.getRemark()), FinancialMain::getRemark, queryPaymentDTO.getRemark())
                .ge(StringUtils.hasLength(queryPaymentDTO.getStartDate()), FinancialMain::getReceiptDate, queryPaymentDTO.getStartDate())
                .le(StringUtils.hasLength(queryPaymentDTO.getEndDate()), FinancialMain::getReceiptDate, queryPaymentDTO.getEndDate())
                .eq(FinancialMain::getType, "付款")
                .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();

        var paymentExportEnBOList = new ArrayList<PaymentExportEnBO>(financialMainList.size() + 1);
        financialMainList.forEach(item -> {
            var paymentExportEnBO = PaymentExportEnBO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .supplierName(commonService.getSupplierName(item.getRelatedPersonId()))
                    .receiptDate(item.getReceiptDate())
                    .financialPerson(commonService.getOperatorName(item.getOperatorId()))
                    .paymentAccountName(commonService.getAccountName(item.getAccountId()))
                    .totalPaymentAmount(item.getTotalAmount())
                    .discountAmount(item.getDiscountAmount())
                    .actualPaymentAmount(item.getChangeAmount())
                    .status(item.getStatus())
                    .remark(item.getRemark())
                    .build();

            paymentExportEnBOList.add(paymentExportEnBO);
        });
        return paymentExportEnBOList;
    }

    @Override
    public Response<PaymentDetailVO> getPaymentReceiptDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var financialMain = getById(id);
        if(financialMain != null) {
            var paymentDetailVO = PaymentDetailVO.builder()
                    .id(financialMain.getId())
                    .supplierId(financialMain.getRelatedPersonId())
                    .supplierName(commonService.getSupplierName(financialMain.getRelatedPersonId()))
                    .receiptDate(financialMain.getReceiptDate())
                    .receiptNumber(financialMain.getReceiptNumber())
                    .financialPersonId(financialMain.getOperatorId())
                    .financialPersonName(commonService.getOperatorName(financialMain.getOperatorId()))
                    .paymentAccountId(financialMain.getAccountId())
                    .paymentAccountName(commonService.getAccountName(financialMain.getAccountId()))
                    .totalPaymentAmount(financialMain.getTotalAmount())
                    .discountAmount(financialMain.getDiscountAmount())
                    .actualPaymentAmount(financialMain.getChangeAmount())
                    .remark(financialMain.getRemark())
                    .status(financialMain.getStatus())
                    .build();

            var financialSubs = financialSubService.lambdaQuery()
                    .eq(FinancialSub::getFinancialMainId, id)
                    .list();

            if (financialSubs != null) {
                var paymentBOList = new ArrayList<PaymentBO>(financialSubs.size() + 1);
                financialSubs.forEach(sub -> {
                    var paymentBO = PaymentBO.builder()
                            .paymentId(sub.getId())
                            .purchaseReceiptNumber(sub.getOtherReceipt())
                            .paymentArrears(sub.getReceivablePaymentArrears())
                            .prepaidArrears(sub.getReceivedPrepaidArrears())
                            .thisPaymentAmount(sub.getSingleAmount())
                            .remark(sub.getRemark())
                            .build();
                    paymentBOList.add(paymentBO);
                });
                paymentDetailVO.setTableData(paymentBOList);
            }
            var fileList = commonService.getFileList(financialMain.getFileId());
            paymentDetailVO.setFiles(fileList);
            return Response.responseData(paymentDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    public Response<String> addOrUpdatePaymentReceipt(AddOrUpdatePaymentDTO addOrUpdatePaymentDTO) {
        var userId = userService.getCurrentUserId();
        var systemLanguage = userService.getUserSystemLanguage(userId);
        var fid = processFiles(addOrUpdatePaymentDTO.getFiles(), addOrUpdatePaymentDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = addOrUpdatePaymentDTO.getId() != null;

        if (isUpdate) {
//            var beforeReceipt = financialSubService.lambdaQuery()
//                    .eq(FinancialSub::getFinancialMainId, addOrUpdatePaymentDTO.getId())
//                    .list();

            financialSubService.lambdaUpdate()
                    .eq(FinancialSub::getFinancialMainId, addOrUpdatePaymentDTO.getId())
                    .remove();

            var financialSubList = addOrUpdatePaymentDTO.getTableData();
            var financialSub = financialSubList.stream()
                    .map(item -> FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(addOrUpdatePaymentDTO.getId())
                            .accountId(addOrUpdatePaymentDTO.getPaymentAccountId())
                            .otherReceipt(item.getPurchaseReceiptNumber())
                            .ReceivablePaymentArrears(item.getPaymentArrears())
                            .ReceivedPrepaidArrears(Optional.ofNullable(item.getPrepaidArrears()).orElse(BigDecimal.ZERO).add(item.getPrepaidArrears()))
                            .singleAmount(item.getThisPaymentAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var updateSubResult = financialSubService.saveBatch(financialSub);

            var updateFinancialMain = lambdaUpdate()
                    .eq(FinancialMain::getId, addOrUpdatePaymentDTO.getId())
                    .set(addOrUpdatePaymentDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, addOrUpdatePaymentDTO.getFinancialPersonId())
                    .set(addOrUpdatePaymentDTO.getPaymentAccountId() != null, FinancialMain::getAccountId, addOrUpdatePaymentDTO.getPaymentAccountId())
                    .set(addOrUpdatePaymentDTO.getTotalPaymentAmount() != null, FinancialMain::getTotalAmount, addOrUpdatePaymentDTO.getTotalPaymentAmount())
                    .set(addOrUpdatePaymentDTO.getDiscountAmount() != null, FinancialMain::getDiscountAmount, addOrUpdatePaymentDTO.getDiscountAmount())
                    .set(addOrUpdatePaymentDTO.getActualPaymentAmount() != null, FinancialMain::getChangeAmount, addOrUpdatePaymentDTO.getActualPaymentAmount())
                    .set(addOrUpdatePaymentDTO.getStatus() != null, FinancialMain::getStatus, addOrUpdatePaymentDTO.getStatus())
                    .set(StringUtils.hasLength(addOrUpdatePaymentDTO.getRemark()), FinancialMain::getRemark, addOrUpdatePaymentDTO.getRemark())
                    .set(StringUtils.hasLength(addOrUpdatePaymentDTO.getReceiptDate()), FinancialMain::getReceiptDate, addOrUpdatePaymentDTO.getReceiptDate())
                    .set(StringUtils.hasLength(fileIds), FinancialMain::getFileId, fileIds)
                    .set(FinancialMain::getUpdateBy, userId)
                    .set(FinancialMain::getUpdateTime, LocalDateTime.now())
                    .update();

            // 这段代码需要再次检查具体业务是要做什么 2024-08-01 (James Zow <Jameszow@163.com>)
//            var account = accountService.getById(addOrUpdatePaymentDTO.getPaymentAccountId());
//            if (account != null) {
//                var accountBalance = account.getCurrentAmount();
//                var changeAmount = addOrUpdatePaymentDTO.getActualPaymentAmount();
//                var beforeChangeAmount = beforeReceipt.stream()
//                        .map(FinancialSub::getSingleAmount)
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                accountBalance = accountBalance.add(beforeChangeAmount);
//                if (changeAmount != null) {
//                    accountBalance = accountBalance.subtract(changeAmount);
//                }
//                account.setCurrentAmount(accountBalance);
//                accountService.updateById(account);
//            }

            if (!updateSubResult || !updateFinancialMain) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_ERROR);
                }
                return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_ERROR_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_SUCCESS);
                }
                return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_SUCCESS_EN);
            }

        } else {
            var id = SnowflakeIdUtil.nextId();
            var financialMain = FinancialMain.builder()
                    .id(id)
                    .receiptDate(TimeUtil.parse(addOrUpdatePaymentDTO.getReceiptDate()))
                    .receiptNumber(addOrUpdatePaymentDTO.getReceiptNumber())
                    .operatorId(addOrUpdatePaymentDTO.getFinancialPersonId())
                    .accountId(addOrUpdatePaymentDTO.getPaymentAccountId())
                    .relatedPersonId(addOrUpdatePaymentDTO.getSupplierId())
                    .totalAmount(addOrUpdatePaymentDTO.getTotalPaymentAmount())
                    .discountAmount(addOrUpdatePaymentDTO.getDiscountAmount())
                    .changeAmount(addOrUpdatePaymentDTO.getActualPaymentAmount())
                    .remark(addOrUpdatePaymentDTO.getRemark())
                    .status(addOrUpdatePaymentDTO.getStatus())
                    .type("付款")
                    .fileId(fileIds)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .status(addOrUpdatePaymentDTO.getStatus())
                    .build();

            var saveResult = save(financialMain);
            var financialSubList = addOrUpdatePaymentDTO.getTableData();
            var financialSub = financialSubList.stream()
                    .map(item -> FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(id)
                            .accountId(addOrUpdatePaymentDTO.getPaymentAccountId())
                            .otherReceipt(item.getPurchaseReceiptNumber())
                            .ReceivablePaymentArrears(item.getPaymentArrears())
                            .ReceivedPrepaidArrears(Optional.ofNullable(item.getPrepaidArrears()).orElse(BigDecimal.ZERO).add(item.getThisPaymentAmount()))
                            .singleAmount(item.getThisPaymentAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = financialSubService.saveBatch(financialSub);

//            var account = accountService.getById(addOrUpdatePaymentDTO.getPaymentAccountId());
//            if (account != null) {
//                // 更新余额 采购划扣金额
//                var accountBalance = account.getCurrentAmount();
//                var changeAmount = addOrUpdatePaymentDTO.getActualPaymentAmount();
//                if (changeAmount != null) {
//                    accountBalance = accountBalance.subtract(changeAmount);
//                    account.setId(addOrUpdatePaymentDTO.getPaymentAccountId());
//                    account.setCurrentAmount(accountBalance);
//                    accountService.updateById(account);
//                }
//            }

            if (!saveResult || !saveSubResult) {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(CollectionPaymentCodeEnum.ADD_PAYMENT_RECEIPT_ERROR);
                }
                return Response.responseMsg(CollectionPaymentCodeEnum.ADD_PAYMENT_RECEIPT_ERROR_EN);
            } else {
                if ("zh_CN".equals(systemLanguage)) {
                    return Response.responseMsg(CollectionPaymentCodeEnum.ADD_PAYMENT_RECEIPT_SUCCESS);
                }
                return Response.responseMsg(CollectionPaymentCodeEnum.ADD_PAYMENT_RECEIPT_SUCCESS_EN);
            }
        }
    }

    @Override
    public Response<String> deleteBatchPaymentReceipt(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var deleteResult = lambdaUpdate()
                .set(FinancialMain::getDeleteFlag, CommonConstants.DELETED)
                .in(FinancialMain::getId, ids)
                .update();

        financialSubService.lambdaUpdate()
                .set(FinancialSub::getDeleteFlag, CommonConstants.DELETED)
                .in(FinancialSub::getFinancialMainId, ids)
                .update();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());

        if(!deleteResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(CollectionPaymentCodeEnum.DELETE_PAYMENT_RECEIPT_ERROR);
            }
            return Response.responseMsg(CollectionPaymentCodeEnum.DELETE_PAYMENT_RECEIPT_ERROR_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(CollectionPaymentCodeEnum.DELETE_PAYMENT_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(CollectionPaymentCodeEnum.DELETE_PAYMENT_RECEIPT_SUCCESS_EN);
        }
    }

    @Override
    public Response<String> updatePaymentReceiptStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(FinancialMain::getStatus, status)
                .in(FinancialMain::getId, ids)
                .update();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if(!updateResult) {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_ERROR);
            }
            return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_ERROR_EN);
        } else {
            if ("zh_CN".equals(systemLanguage)) {
                return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_SUCCESS);
            }
            return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_PAYMENT_RECEIPT_SUCCESS_EN);
        }
    }

    @Override
    public void exportPaymentReceipt(QueryPaymentDTO queryPaymentDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
        if ("zh_CN".equals(systemLanguage)) {
            var mainData = getPaymentReceiptList(queryPaymentDTO);
            if (!mainData.isEmpty()) {
                exportMap.put("付款单", ExcelUtils.getSheetData(mainData));
                if (queryPaymentDTO.getIsExportDetail()) {
                    var subData = new ArrayList<PaymentDataExportBO>();
                    for (PaymentExportBO paymentExportBO : mainData) {
                        var detail = getPaymentReceiptDetail(paymentExportBO.getId()).getData().getTableData();
                        if (!detail.isEmpty()) {
                            detail.forEach(item -> {
                                var data = PaymentDataExportBO.builder()
                                        .supplierName(paymentExportBO.getSupplierName())
                                        .receiptNumber(paymentExportBO.getReceiptNumber())
                                        .purchaseReceiptNumber(item.getPurchaseReceiptNumber())
                                        .paymentArrears(item.getPaymentArrears())
                                        .prepaidArrears(item.getPrepaidArrears())
                                        .thisPaymentAmount(item.getThisPaymentAmount())
                                        .remark(item.getRemark())
                                        .build();
                                subData.add(data);
                            });
                        }
                    }
                    exportMap.put("付款单明细", ExcelUtils.getSheetData(subData));
                }
                ExcelUtils.exportManySheet(response, "付款单", exportMap);
            }
        } else {
            var mainEnData = getPaymentReceiptEnList(queryPaymentDTO);
            if (!mainEnData.isEmpty()) {
                exportMap.put("Payment Document", ExcelUtils.getSheetData(mainEnData));
                if (queryPaymentDTO.getIsExportDetail()) {
                    var subEnData = new ArrayList<PaymentDataExportEnBO>();
                    for (PaymentExportEnBO paymentExportEnBO : mainEnData) {
                        var detail = getPaymentReceiptDetail(paymentExportEnBO.getId()).getData().getTableData();
                        if (!detail.isEmpty()) {
                            detail.forEach(item -> {
                                var data = PaymentDataExportEnBO.builder()
                                        .supplierName(paymentExportEnBO.getSupplierName())
                                        .receiptNumber(paymentExportEnBO.getReceiptNumber())
                                        .purchaseReceiptNumber(item.getPurchaseReceiptNumber())
                                        .paymentArrears(item.getPaymentArrears())
                                        .prepaidArrears(item.getPrepaidArrears())
                                        .thisPaymentAmount(item.getThisPaymentAmount())
                                        .remark(item.getRemark())
                                        .build();
                                subEnData.add(data);
                            });
                        }
                    }
                    exportMap.put("Payment Document Detail", ExcelUtils.getSheetData(subEnData));
                }
                ExcelUtils.exportManySheet(response, "Payment Document", exportMap);
            }
        }
    }

    @Override
    public void exportPaymentReceiptDetail(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(FinancialMain::getReceiptNumber, receiptNumber)
                .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(FinancialMain::getType, "付款")
                .one()
                .getId();

        var detail = getPaymentReceiptDetail(id);
        if (detail.getData() != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var systemLanguage = userService.getUserSystemLanguage(userService.getCurrentUserId());
            if ("zh_CN".equals(systemLanguage)) {
                var exportData = new ArrayList<PaymentDataExportBO>();
                tableData.forEach(item -> {
                    var paymentDataBO = new PaymentDataExportBO();
                    paymentDataBO.setSupplierName(data.getSupplierName());
                    paymentDataBO.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, paymentDataBO);
                    exportData.add(paymentDataBO);
                });
                var fileName = data.getReceiptNumber() + "-付款单明细";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
            } else {
                var exportEnData = new ArrayList<PaymentDataExportEnBO>();
                tableData.forEach(item -> {
                    var paymentDataEnBO = new PaymentDataExportEnBO();
                    paymentDataEnBO.setSupplierName(data.getSupplierName());
                    paymentDataEnBO.setReceiptNumber(data.getReceiptNumber());
                    BeanUtils.copyProperties(item, paymentDataEnBO);
                    exportEnData.add(paymentDataEnBO);
                });
                var fileName = data.getReceiptNumber() + "- Payment Document Detail";
                ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportEnData));
            }
        }
    }
}
