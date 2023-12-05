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
import com.wansenai.bo.IncomeExpenseDataExportBO;
import com.wansenai.bo.TransferAccountBO;
import com.wansenai.bo.TransferAccountDataExportBO;
import com.wansenai.dto.financial.AddOrUpdateTransferDTO;
import com.wansenai.dto.financial.QueryTransferDTO;
import com.wansenai.entities.financial.FinancialMain;
import com.wansenai.entities.financial.FinancialSub;
import com.wansenai.entities.system.SysFile;
import com.wansenai.mappers.financial.FinancialMainMapper;
import com.wansenai.mappers.system.SysFileMapper;
import com.wansenai.service.common.CommonService;
import com.wansenai.service.financial.FinancialSubService;
import com.wansenai.service.financial.IFinancialAccountService;
import com.wansenai.service.financial.TransferReceiptService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.TransferAccountCodeEnum;
import com.wansenai.utils.excel.ExcelUtils;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.financial.TransferDetailVO;
import com.wansenai.vo.financial.TransferVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TransferReceiptServiceImpl extends ServiceImpl<FinancialMainMapper, FinancialMain> implements TransferReceiptService {

    private final FinancialSubService financialSubService;

    private final CommonService commonService;

    private final ISysUserService userService;

    private final SysFileMapper fileMapper;

    private final IFinancialAccountService accountService;

    public TransferReceiptServiceImpl(FinancialSubService financialSubService, CommonService commonService, ISysUserService userService, SysFileMapper fileMapper, IFinancialAccountService accountService) {
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
    public Response<Page<TransferVO>> getTransferReceiptPageList(QueryTransferDTO queryTransferDTO) {
        var result = new Page<TransferVO>();
        var page = new Page<FinancialMain>(queryTransferDTO.getPage(), queryTransferDTO.getPageSize());

        var financialMainPage = lambdaQuery()
                .eq(queryTransferDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, queryTransferDTO.getFinancialPersonId())
                .eq(queryTransferDTO.getAccountId() != null, FinancialMain::getAccountId, queryTransferDTO.getAccountId())
                .eq(StringUtils.hasLength(queryTransferDTO.getReceiptNumber()), FinancialMain::getReceiptNumber, queryTransferDTO.getReceiptNumber())
                .eq(queryTransferDTO.getStatus() != null, FinancialMain::getStatus, queryTransferDTO.getStatus())
                .like(StringUtils.hasLength(queryTransferDTO.getRemark()), FinancialMain::getRemark, queryTransferDTO.getRemark())
                .ge(StringUtils.hasLength(queryTransferDTO.getStartDate()), FinancialMain::getReceiptDate, queryTransferDTO.getStartDate())
                .le(StringUtils.hasLength(queryTransferDTO.getEndDate()), FinancialMain::getReceiptDate, queryTransferDTO.getEndDate())
                .eq(FinancialMain::getType, "转账")
                .page(page);

        var transferVOList = new ArrayList<TransferVO>(financialMainPage.getRecords().size() + 1);
        financialMainPage.getRecords().forEach(item -> {
            var transferVO = TransferVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .financialPerson(commonService.getOperatorName(item.getOperatorId()))
                    .paymentAmount(item.getTotalAmount())
                    .paymentAccountName(commonService.getAccountName(item.getAccountId()))
                    .status(item.getStatus())
                    .remark(item.getRemark())
                    .build();

            transferVOList.add(transferVO);
        });
        result.setRecords(transferVOList);
        result.setTotal(financialMainPage.getTotal());
        return Response.responseData(result);
    }

    private List<TransferVO> getTransferReceiptList(QueryTransferDTO queryTransferDTO) {
        var financialMainList = lambdaQuery()
                .eq(queryTransferDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, queryTransferDTO.getFinancialPersonId())
                .eq(queryTransferDTO.getAccountId() != null, FinancialMain::getAccountId, queryTransferDTO.getAccountId())
                .eq(StringUtils.hasLength(queryTransferDTO.getReceiptNumber()), FinancialMain::getReceiptNumber, queryTransferDTO.getReceiptNumber())
                .eq(queryTransferDTO.getStatus() != null, FinancialMain::getStatus, queryTransferDTO.getStatus())
                .like(StringUtils.hasLength(queryTransferDTO.getRemark()), FinancialMain::getRemark, queryTransferDTO.getRemark())
                .ge(StringUtils.hasLength(queryTransferDTO.getStartDate()), FinancialMain::getReceiptDate, queryTransferDTO.getStartDate())
                .le(StringUtils.hasLength(queryTransferDTO.getEndDate()), FinancialMain::getReceiptDate, queryTransferDTO.getEndDate())
                .eq(FinancialMain::getType, "转账")
                .list();

        var transferVOList = new ArrayList<TransferVO>(financialMainList.size() + 1);
        financialMainList.forEach(item -> {
            var transferVO = TransferVO.builder()
                    .id(item.getId())
                    .receiptNumber(item.getReceiptNumber())
                    .receiptDate(item.getReceiptDate())
                    .financialPerson(commonService.getOperatorName(item.getOperatorId()))
                    .paymentAmount(item.getTotalAmount())
                    .paymentAccountName(commonService.getAccountName(item.getAccountId()))
                    .status(item.getStatus())
                    .remark(item.getRemark())
                    .build();

            transferVOList.add(transferVO);
        });
        return transferVOList;
    }

    @Override
    public Response<TransferDetailVO> getTransferReceiptDetail(Long id) {
        if (id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var financialMain = getById(id);
        if(financialMain != null) {
            var transferDetailVO = TransferDetailVO.builder()
                    .receiptDate(financialMain.getReceiptDate())
                    .receiptNumber(financialMain.getReceiptNumber())
                    .financialPersonId(financialMain.getOperatorId())
                    .financialPersonName(commonService.getOperatorName(financialMain.getOperatorId()))
                    .paymentAccountId(financialMain.getAccountId())
                    .paymentAccountName(commonService.getAccountName(financialMain.getAccountId()))
                    .paymentAmount(financialMain.getTotalAmount())
                    .remark(financialMain.getRemark())
                    .status(financialMain.getStatus())
                    .build();

            var transferAccountBOList = financialSubService.lambdaQuery()
                    .eq(FinancialSub::getFinancialMainId, id)
                    .list();

            if (transferAccountBOList != null) {
                var transferAccountBOVOList = new ArrayList<TransferAccountBO>(transferAccountBOList.size() + 1);
                transferAccountBOList.forEach(sub -> {
                    var transferAccountVO = TransferAccountBO.builder()
                            .accountId(sub.getAccountId())
                            .accountName(commonService.getAccountName(sub.getAccountId()))
                            .transferAmount(sub.getSingleAmount())
                            .remark(sub.getRemark())
                            .build();
                    transferAccountBOVOList.add(transferAccountVO);
                });
                transferDetailVO.setTableData(transferAccountBOVOList);
            }
            var fileList = commonService.getFileList(financialMain.getFileId());
            transferDetailVO.setFiles(fileList);
            return Response.responseData(transferDetailVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
    }

    @Override
    public Response<String> addOrUpdateTransferReceipt(AddOrUpdateTransferDTO addOrUpdateTransferDTO) {
        var userId = userService.getCurrentUserId();
        var fid = processFiles(addOrUpdateTransferDTO.getFiles(), addOrUpdateTransferDTO.getId());
        var fileIds = StringUtils.collectionToCommaDelimitedString(fid);
        var isUpdate = addOrUpdateTransferDTO.getId() != null;

        if (isUpdate) {
            var beforeReceipt = financialSubService.lambdaQuery()
                    .eq(FinancialSub::getFinancialMainId, addOrUpdateTransferDTO.getId())
                    .list();

            financialSubService.lambdaUpdate()
                    .eq(FinancialSub::getFinancialMainId, addOrUpdateTransferDTO.getId())
                    .remove();

            var financialSubList = addOrUpdateTransferDTO.getTableData();
            var financialSub = financialSubList.stream()
                    .map(item -> FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(addOrUpdateTransferDTO.getId())
                            .accountId(item.getAccountId())
                            .singleAmount(item.getTransferAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var updateSubResult = financialSubService.saveBatch(financialSub);

            var updateFinancialMain = lambdaUpdate()
                    .eq(FinancialMain::getId, addOrUpdateTransferDTO.getId())
                    .set(StringUtils.hasLength(addOrUpdateTransferDTO.getReceiptDate()), FinancialMain::getReceiptDate, addOrUpdateTransferDTO.getReceiptDate())
                    .set(addOrUpdateTransferDTO.getFinancialPersonId() != null, FinancialMain::getOperatorId, addOrUpdateTransferDTO.getFinancialPersonId())
                    .set(addOrUpdateTransferDTO.getPaymentAccountId() != null, FinancialMain::getAccountId, addOrUpdateTransferDTO.getPaymentAccountId())
                    .set(addOrUpdateTransferDTO.getPaymentAmount() != null, FinancialMain::getTotalAmount, addOrUpdateTransferDTO.getPaymentAmount())
                    .set(StringUtils.hasLength(addOrUpdateTransferDTO.getRemark()), FinancialMain::getRemark, addOrUpdateTransferDTO.getRemark())
                    .set(StringUtils.hasLength(fileIds), FinancialMain::getFileId, fileIds)
                    .set(FinancialMain::getUpdateBy, userId)
                    .set(FinancialMain::getUpdateTime, LocalDateTime.now())
                    .set(addOrUpdateTransferDTO.getStatus() != null, FinancialMain::getStatus, addOrUpdateTransferDTO.getStatus())
                    .update();

            var account = accountService.getById(addOrUpdateTransferDTO.getPaymentAccountId());
            if (account != null) {
                var accountBalance = account.getCurrentAmount();
                var changeAmount = addOrUpdateTransferDTO.getPaymentAmount();
                var beforeChangeAmount = beforeReceipt.stream()
                        .map(FinancialSub::getSingleAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                accountBalance = accountBalance.add(beforeChangeAmount);
                if (changeAmount != null) {
                    accountBalance = accountBalance.subtract(changeAmount);
                }
                account.setCurrentAmount(accountBalance);
                accountService.updateById(account);

                // 子表的的账户也需要更新金额
                var subAccountIds = beforeReceipt.stream()
                        .map(FinancialSub::getAccountId)
                        .collect(Collectors.toList());
                var subAccountList = accountService.listByIds(subAccountIds);
                subAccountList.forEach(subAccount -> {
                    var subAccountBalance = subAccount.getCurrentAmount();
                    var subChangeAmount = beforeReceipt.stream()
                            .filter(sub -> sub.getAccountId().equals(subAccount.getId()))
                            .map(FinancialSub::getSingleAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    subAccountBalance = subAccountBalance.add(subChangeAmount);
                    subAccount.setCurrentAmount(subAccountBalance);
                });
                accountService.updateBatchById(subAccountList);
            }

            if (!updateSubResult || !updateFinancialMain) {
                return Response.responseMsg(TransferAccountCodeEnum.UPDATE_TRANSFER_ACCOUNT_RECEIPT_ERROR);
            }
            return Response.responseMsg(TransferAccountCodeEnum.UPDATE_TRANSFER_ACCOUNT_RECEIPT_SUCCESS);

        } else {
            var id = SnowflakeIdUtil.nextId();
            var financialMain = FinancialMain.builder()
                    .id(id)
                    .receiptDate(TimeUtil.parse(addOrUpdateTransferDTO.getReceiptDate()))
                    .receiptNumber(addOrUpdateTransferDTO.getReceiptNumber())
                    .operatorId(addOrUpdateTransferDTO.getFinancialPersonId())
                    .accountId(addOrUpdateTransferDTO.getPaymentAccountId())
                    .totalAmount(addOrUpdateTransferDTO.getPaymentAmount())
                    .remark(addOrUpdateTransferDTO.getRemark())
                    .type("转账")
                    .fileId(fileIds)
                    .createBy(userId)
                    .createTime(LocalDateTime.now())
                    .status(addOrUpdateTransferDTO.getStatus())
                    .build();

            var saveResult = save(financialMain);
            var financialSubList = addOrUpdateTransferDTO.getTableData();
            var financialSub = financialSubList.stream()
                    .map(item -> FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(id)
                            .accountId(item.getAccountId())
                            .singleAmount(item.getTransferAmount())
                            .remark(item.getRemark())
                            .createBy(userId)
                            .createTime(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            var saveSubResult = financialSubService.saveBatch(financialSub);

            var account = accountService.getById(addOrUpdateTransferDTO.getPaymentAccountId());
            if (account != null) {
                // 更新余额
                var accountBalance = account.getCurrentAmount();
                var changeAmount = addOrUpdateTransferDTO.getPaymentAmount();
                if (changeAmount != null) {
                    accountBalance = accountBalance.subtract(changeAmount);
                    account.setId(addOrUpdateTransferDTO.getPaymentAccountId());
                    account.setCurrentAmount(accountBalance);
                    accountService.updateById(account);
                }
                // 子表的的账户也需要更新金额
                var subAccountList = accountService.listByIds(financialSubList.stream()
                        .map(TransferAccountBO::getAccountId)
                        .collect(Collectors.toList()));
                subAccountList.forEach(subAccount -> {
                    var subAccountBalance = subAccount.getCurrentAmount();
                    var subChangeAmount = financialSubList.stream()
                            .filter(sub -> sub.getAccountId().equals(subAccount.getId()))
                            .map(TransferAccountBO::getTransferAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    subAccountBalance = subAccountBalance.add(subChangeAmount);
                    subAccount.setId(subAccount.getId());
                    subAccount.setCurrentAmount(subAccountBalance);
                });
                accountService.updateBatchById(subAccountList);
            }

            if (!saveResult || !saveSubResult) {
                return Response.responseMsg(TransferAccountCodeEnum.ADD_TRANSFER_ACCOUNT_RECEIPT_ERROR);
            }
            return Response.responseMsg(TransferAccountCodeEnum.ADD_TRANSFER_ACCOUNT_RECEIPT_SUCCESS);
        }
    }

    @Override
    public Response<String> deleteBatchTransferReceipt(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var deleteResult = lambdaUpdate()
                .set(FinancialMain::getDeleteFlag, CommonConstants.DELETED)
                .in(FinancialMain::getId, ids)
                .update();
        if(!deleteResult) {
            return Response.responseMsg(TransferAccountCodeEnum.DELETE_TRANSFER_ACCOUNT_RECEIPT_ERROR);
        }
        return Response.responseMsg(TransferAccountCodeEnum.DELETE_TRANSFER_ACCOUNT_RECEIPT_SUCCESS);
    }

    @Override
    public Response<String> updateTransferReceiptStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .set(FinancialMain::getStatus, status)
                .in(FinancialMain::getId, ids)
                .update();
        if(!updateResult) {
            return Response.responseMsg(TransferAccountCodeEnum.UPDATE_TRANSFER_ACCOUNT_RECEIPT_ERROR);
        }
        return Response.responseMsg(TransferAccountCodeEnum.UPDATE_TRANSFER_ACCOUNT_RECEIPT_SUCCESS);
    }

    @Override
    public void exportTransferReceipt(QueryTransferDTO queryTransferDTO, HttpServletResponse response) {
        var exportMap = new ConcurrentHashMap<String, List<List<Object>>>();
        var mainData = getTransferReceiptList(queryTransferDTO);
        if (!mainData.isEmpty()) {
            exportMap.put("转账单", ExcelUtils.getSheetData(mainData));
            if (queryTransferDTO.getIsExportDetail()) {
                var subData = new ArrayList<TransferAccountDataExportBO>();
                for (TransferVO transferVO : mainData) {
                    var detail = getTransferReceiptDetail(transferVO.getId()).getData().getTableData();
                    if (!detail.isEmpty()) {
                        detail.forEach(item -> {
                            var data = TransferAccountDataExportBO.builder()
                                    .receiptNumber(transferVO.getReceiptNumber())
                                    .accountName(item.getAccountName())
                                    .transferAmount(item.getTransferAmount())
                                    .remark(item.getRemark())
                                    .build();
                            subData.add(data);
                        });
                    }
                }
                exportMap.put("转账单明细", ExcelUtils.getSheetData(subData));
            }
            ExcelUtils.exportManySheet(response, "转账单", exportMap);
        }
    }

    @Override
    public void exportTransferReceiptDetail(String receiptNumber, HttpServletResponse response) {
        var id = lambdaQuery()
                .eq(FinancialMain::getReceiptNumber, receiptNumber)
                .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
                .eq(FinancialMain::getType, "转账")
                .one()
                .getId();

        var detail = getTransferReceiptDetail(id);
        if(detail.getData() != null) {
            var data = detail.getData();
            var tableData = data.getTableData();
            var exportData = new ArrayList<TransferAccountDataExportBO>();
            tableData.forEach(item -> {
                var transferAccountBO = new TransferAccountDataExportBO();
                transferAccountBO.setReceiptNumber(data.getReceiptNumber());
                BeanUtils.copyProperties(item, transferAccountBO);
                exportData.add(transferAccountBO);
            });
            var fileName = data.getReceiptNumber() + "-转账单明细";
            ExcelUtils.export(response, fileName, ExcelUtils.getSheetData(exportData));
        }
    }
}
