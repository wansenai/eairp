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
import com.wansenai.bo.CollectionBO;
import com.wansenai.bo.FileDataBO;
import com.wansenai.dto.financial.AddOrUpdateCollectionDTO;
import com.wansenai.dto.financial.QueryCollectionDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.financial.FinancialMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.CollectionReceiptService;
import com.wansenai.service.financial.FinancialSubService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.CollectionPaymentCodeEnum;
import com.wansenai.utils.enums.IncomeExpenseCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.CollectionDetailVO;
import com.wansenai.vo.financial.CollectionVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionReceiptServiceImpl extends ServiceImpl<FinancialMainMapper, FinancialMain> implements CollectionReceiptService {

    private final FinancialSubService financialSubService;

    private final CommonService commonService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    private final IFinancialAccountService accountService;

    public CollectionReceiptServiceImpl(FinancialSubService financialSubService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, IFinancialAccountService accountService) {
        this.financialSubService = financialSubService;
        this.commonService = commonService;
        this.userService = userService;
        this.fileMapper = fileMapper;
        this.accountService = accountService;
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
    public Response<Page<CollectionVO>> getCollectionReceiptPageList(QueryCollectionDTO queryCollectionDTO) {
        var result = new Page<CollectionVO>();
        var page = new Page<FinancialMain>(queryCollectionDTO.getPage(), queryCollectionDTO.getPageSize());

        var financialMainPage = lambdaQuery()
                .eq(queryCollectionDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, queryCollectionDTO.getFinancialPersonId())
                .eq(queryCollectionDTO.getAccountId() != null, FinancialMain::getAccountId, queryCollectionDTO.getAccountId())
                .eq(queryCollectionDTO.getStatus() != null, FinancialMain::getStatus, queryCollectionDTO.getStatus())
                .eq(queryCollectionDTO.getCustomerId() != null, FinancialMain::getRelatedPersonId, queryCollectionDTO.getCustomerId())
                .eq(StringUtils.hasLength(queryCollectionDTO.getReceiptNumber()), FinancialMain::getReceiptNumber, queryCollectionDTO.getReceiptNumber())
                .like(StringUtils.hasLength(queryCollectionDTO.getRemark()), FinancialMain::getRemark, queryCollectionDTO.getRemark())
                .ge(StringUtils.hasLength(queryCollectionDTO.getStartDate()), FinancialMain::getReceiptDate, queryCollectionDTO.getStartDate())
                .le(StringUtils.hasLength(queryCollectionDTO.getEndDate()), FinancialMain::getReceiptDate, queryCollectionDTO.getEndDate())
                .eq(FinancialMain::getType, "收款")
                .page(page);

        var collectionVOList = new ArrayList<CollectionVO>(financialMainPage.getRecords().size() + 1);
        financialMainPage.getRecords().forEach(item -> {
            var collectionVo = CollectionVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .customerName(commonService.getCustomerName(item.getRelatedPersonId()))
                    .receiptDate(item.getReceiptDate())
                    .financialPerson(commonService.getOperatorName(item.getOperatorId()))
                    .collectionAccountName(commonService.getAccountName(item.getAccountId()))
                    .totalCollectionAmount(item.getTotalAmount())
                    .discountAmount(item.getDiscountAmount())
                    .actualCollectionAmount(item.getChangeAmount())
                    .status(item.getStatus())
                    .remark(item.getRemark())
                    .build();

            collectionVOList.add(collectionVo);
        });
        result.setRecords(collectionVOList);
        result.setTotal(financialMainPage.getTotal());
        return Response.responseData(result);
    }

    @Override
    public Response<CollectionDetailVO> getCollectionReceiptDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var financialMain = getById(id);
        if(financialMain != null) {
            var collectionDetailVO = CollectionDetailVO.builder()
                    .receiptDate(financialMain.getReceiptDate())
                    .receiptNumber(financialMain.getReceiptNumber())
                    .financialPersonId(financialMain.getOperatorId())
                    .financialPersonName(commonService.getOperatorName(financialMain.getOperatorId()))
                    .collectionAccountId(financialMain.getAccountId())
                    .collectionAccountName(commonService.getAccountName(financialMain.getAccountId()))
                    .totalCollectionAmount(financialMain.getTotalAmount())
                    .discountAmount(financialMain.getDiscountAmount())
                    .actualCollectionAmount(financialMain.getChangeAmount())
                    .remark(financialMain.getRemark())
                    .status(financialMain.getStatus())
                    .build();

            var financialSubs = financialSubService.lambdaQuery()
                    .eq(FinancialSub::getFinancialMainId, id)
                    .list();

            if (financialSubs != null) {
                var CollectionBOList = new ArrayList<CollectionBO>(financialSubs.size() + 1);
                financialSubs.forEach(sub -> {
                    var collectionBO = CollectionBO.builder()
                            .collectionId(sub.getId())
                            .saleReceiptNumber(sub.getOtherReceipt())
                            .receivableArrears(sub.getReceivablePaymentArrears())
                            .receivedArrears(sub.getReceivedPrepaidArrears())
                            .thisCollectionAmount(sub.getSingleAmount())
                            .remark(sub.getRemark())
                            .build();
                    CollectionBOList.add(collectionBO);
                });
                collectionDetailVO.setTableData(CollectionBOList);
            }
            var fileList = commonService.getFileList(financialMain.getFileId());
            collectionDetailVO.setFiles(fileList);
            return Response.responseData(collectionDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    public Response<String> addOrUpdateCollectionReceipt(AddOrUpdateCollectionDTO addOrUpdateCollectionDTO) {
        var userId = userService.getCurrentUserId();
        var fid = processFiles(addOrUpdateCollectionDTO.getFiles(), addOrUpdateCollectionDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = addOrUpdateCollectionDTO.getId() != null;

        if (isUpdate) {
            var beforeReceipt = financialSubService.lambdaQuery()
                    .eq(FinancialSub::getFinancialMainId, addOrUpdateCollectionDTO.getId())
                    .list();

            financialSubService.lambdaUpdate()
                    .eq(FinancialSub::getFinancialMainId, addOrUpdateCollectionDTO.getId())
                    .remove();

            var financialSubList = addOrUpdateCollectionDTO.getTableData();
            var financialSub = financialSubList.stream()
                    .map(item -> FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(addOrUpdateCollectionDTO.getId())
                            .accountId(addOrUpdateCollectionDTO.getCollectionAccountId())
                            .otherReceipt(item.getSaleReceiptNumber())
                            .ReceivablePaymentArrears(item.getReceivableArrears())
                            .ReceivedPrepaidArrears(item.getReceivedArrears())
                            .singleAmount(item.getThisCollectionAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var updateSubResult = financialSubService.saveBatch(financialSub);

            var updateFinancialMain = lambdaUpdate()
                    .eq(FinancialMain::getId, addOrUpdateCollectionDTO.getId())
                    .set(addOrUpdateCollectionDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, addOrUpdateCollectionDTO.getFinancialPersonId())
                    .set(addOrUpdateCollectionDTO.getCollectionAccountId() != null, FinancialMain::getAccountId, addOrUpdateCollectionDTO.getCollectionAccountId())
                    .set(addOrUpdateCollectionDTO.getTotalCollectionAmount() != null, FinancialMain::getTotalAmount, addOrUpdateCollectionDTO.getTotalCollectionAmount())
                    .set(addOrUpdateCollectionDTO.getDiscountAmount() != null, FinancialMain::getDiscountAmount, addOrUpdateCollectionDTO.getDiscountAmount())
                    .set(addOrUpdateCollectionDTO.getActualCollectionAmount() != null, FinancialMain::getChangeAmount, addOrUpdateCollectionDTO.getActualCollectionAmount())
                    .set(addOrUpdateCollectionDTO.getStatus() != null, FinancialMain::getStatus, addOrUpdateCollectionDTO.getStatus())
                    .set(StringUtils.hasLength(addOrUpdateCollectionDTO.getRemark()), FinancialMain::getRemark, addOrUpdateCollectionDTO.getRemark())
                    .set(StringUtils.hasLength(addOrUpdateCollectionDTO.getReceiptDate()), FinancialMain::getReceiptDate, addOrUpdateCollectionDTO.getReceiptDate())
                    .set(StringUtils.hasLength(fileIds), FinancialMain::getFileId, fileIds)
                    .set(FinancialMain::getUpdateBy, userId)
                    .set(FinancialMain::getUpdateTime, LocalDateTime.now())
                    .update();

            var account = accountService.getById(addOrUpdateCollectionDTO.getCollectionAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var changeAmount = addOrUpdateCollectionDTO.getActualCollectionAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(FinancialSub::getSingleAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.subtract(beforeChangeAmount);
                if (changeAmount != null) {
                    accountBalance = accountBalance.add(changeAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);
            }

            if (!updateSubResult || !updateFinancialMain) {
                return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_COLLECTION_RECEIPT_ERROR);
            }
            return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_COLLECTION_RECEIPT_SUCCESS);

        } else {
            var id = SnowflakeIdUtil.nextId();
            var financialMain = FinancialMain.builder()
                    .id(id)
                    .receiptDate(TimeUtil.parse(addOrUpdateCollectionDTO.getReceiptDate()))
                    .receiptNumber(addOrUpdateCollectionDTO.getReceiptNumber())
                    .operatorId(addOrUpdateCollectionDTO.getFinancialPersonId())
                    .accountId(addOrUpdateCollectionDTO.getCollectionAccountId())
                    .relatedPersonId(addOrUpdateCollectionDTO.getCustomerId())
                    .totalAmount(addOrUpdateCollectionDTO.getTotalCollectionAmount())
                    .discountAmount(addOrUpdateCollectionDTO.getDiscountAmount())
                    .changeAmount(addOrUpdateCollectionDTO.getActualCollectionAmount())
                    .remark(addOrUpdateCollectionDTO.getRemark())
                    .status(addOrUpdateCollectionDTO.getStatus())
                    .type("收款")
                    .fileId(fileIds)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .status(addOrUpdateCollectionDTO.getStatus())
                    .build();

            var saveResult = save(financialMain);
            var financialSubList = addOrUpdateCollectionDTO.getTableData();
            var financialSub = financialSubList.stream()
                    .map(item -> FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(id)
                            .accountId(addOrUpdateCollectionDTO.getCollectionAccountId())
                            .otherReceipt(item.getSaleReceiptNumber())
                            .ReceivablePaymentArrears(item.getReceivableArrears())
                            .ReceivedPrepaidArrears(item.getReceivedArrears())
                            .singleAmount(item.getThisCollectionAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = financialSubService.saveBatch(financialSub);

            var account = accountService.getById(addOrUpdateCollectionDTO.getCollectionAccountId());
            if (account != null) {
                // 更新余额 收款账户增加金额
                var accountBalance = account.getCurrentAmount();
                var changeAmount = addOrUpdateCollectionDTO.getActualCollectionAmount();
                if (changeAmount != null) {
                    accountBalance = accountBalance.add(changeAmount);
                    account.setId(addOrUpdateCollectionDTO.getCollectionAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
            }

            if (!saveResult || !saveSubResult) {
                return Response.responseMsg(CollectionPaymentCodeEnum.ADD_COLLECTION_RECEIPT_ERROR);
            }
            return Response.responseMsg(CollectionPaymentCodeEnum.ADD_COLLECTION_RECEIPT_SUCCESS);
        }
    }

    @Override
    public Response<String> deleteBatchCollectionReceipt(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var deleteResult = lambdaUpdate()
                .set(FinancialMain::getDeleteFlag, CommonConstants.DELETED)
                .in(FinancialMain::getId, ids)
                .update();
        if(!deleteResult) {
            return Response.responseMsg(CollectionPaymentCodeEnum.DELETE_COLLECTION_RECEIPT_ERROR);
        }
        return Response.responseMsg(CollectionPaymentCodeEnum.DELETE_COLLECTION_RECEIPT_SUCCESS);
    }

    @Override
    public Response<String> updateCollectionReceiptStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(FinancialMain::getStatus, status)
                .in(FinancialMain::getId, ids)
                .update();
        if(!updateResult) {
            return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_COLLECTION_RECEIPT_ERROR);
        }
        return Response.responseMsg(CollectionPaymentCodeEnum.UPDATE_COLLECTION_RECEIPT_SUCCESS);
    }
}
