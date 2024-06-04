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
package com.wansenai.service.financial.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.entities.financial.FinancialMain
import com.wansenai.entities.financial.FinancialSub
import com.wansenai.bo.AdvanceChargeDataBO
import com.wansenai.bo.AdvanceChargeDataExportBO
import com.wansenai.bo.FileDataBO
import com.wansenai.dto.financial.AddOrUpdateAdvanceChargeDTO
import com.wansenai.dto.financial.QueryAdvanceChargeDTO
import com.wansenai.entities.basic.Member
import com.wansenai.entities.basic.Operator
import com.wansenai.entities.system.SysFile
import com.wansenai.entities.user.SysUser
import com.wansenai.mappers.financial.FinancialMainMapper
import com.wansenai.mappers.system.SysFileMapper
import com.wansenai.service.basic.IOperatorService
import com.wansenai.service.basic.MemberService
import com.wansenai.service.financial.AdvanceChargeService
import com.wansenai.service.financial.IFinancialAccountService
import com.wansenai.service.user.ISysUserService
import com.wansenai.utils.SnowflakeIdUtil
import com.wansenai.utils.TimeUtil
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.FinancialCodeEnum
import com.wansenai.utils.excel.ExcelUtils
import com.wansenai.utils.response.Response
import com.wansenai.vo.financial.AdvanceChargeDetailVO
import com.wansenai.vo.financial.AdvanceChargeVO
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Service
@Slf4j
open class AdvanceChargeServiceImpl(
    private val baseService: com.wansenai.service.BaseService,
    private val financialSubService: com.wansenai.service.financial.FinancialSubService,
    private val financialMainMapper: FinancialMainMapper,
    private val memberService: MemberService,
    private val operatorService: IOperatorService,
    private val userService: ISysUserService,
    private val accountService: IFinancialAccountService,
    private val fileMapper: SysFileMapper,
) : ServiceImpl<FinancialMainMapper, FinancialMain>(), AdvanceChargeService {

    @Transactional
    override fun addOrUpdateAdvanceCharge(advanceChargeDTO: AddOrUpdateAdvanceChargeDTO): Response<String> {
        if (advanceChargeDTO.memberId == null || advanceChargeDTO.receiptDate.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }
        val userId = baseService.currentUserId
        val fileIdList = ArrayList<Long>()
        if (advanceChargeDTO.id != null) {
            val financialSubList = financialSubService.lambdaQuery()
                .eq(FinancialSub::getFinancialMainId, advanceChargeDTO.id)
                .list()

            if (financialSubList.isNotEmpty()) {
                val financialSubIdList = financialSubList.map { it.id }
                val deleteFinancialSubResult = financialSubService.removeByIds(financialSubIdList)

                if (!deleteFinancialSubResult) {
                    return Response.responseMsg(FinancialCodeEnum.UPDATE_ADVANCE_ERROR)
                }
            }

            // If the id is empty, it is a new addition, otherwise it is a modification
            if (advanceChargeDTO.files != null) {
                val financialMain = getById(advanceChargeDTO.id)
                if (financialMain != null) {
                    if (!financialMain.fileId.isNullOrEmpty()) {
                        val ids = financialMain.fileId.split(",").map { it.toLong() }
                        fileMapper.deleteBatchIds(ids)
                    }
                }

                advanceChargeDTO.files?.map { file ->
                    val fileId = SnowflakeIdUtil.nextId()
                    val fileEntity = SysFile.builder()
                        .id(fileId)
                        .uid(file.uid)
                        .fileName(file.fileName)
                        .fileUrl(file.fileUrl)
                        .fileType(file.fileType)
                        .fileSize(file.fileSize)
                        .build()
                    fileIdList.add(fileId)
                    fileMapper.insert(fileEntity)
                }
            }
        }
        val fileIds = fileIdList.map { it }.joinToString(",")

        if (advanceChargeDTO.tableData.isNotEmpty()) {
            val financialMainId = advanceChargeDTO.id ?: SnowflakeIdUtil.nextId()
            val financialMain = FinancialMain.builder()
                .id(financialMainId)
                .relatedPersonId(advanceChargeDTO.memberId)
                .operatorId(advanceChargeDTO.financialPersonnelId)
                .type("收预付款")
                .changeAmount(advanceChargeDTO.totalAmount)
                .totalAmount(advanceChargeDTO.totalAmount)
                .receiptNumber(advanceChargeDTO.receiptNumber)
                .receiptSource(0)
                .receiptDate(TimeUtil.parse(advanceChargeDTO.receiptDate))
                .fileId(fileIds)
                .status(advanceChargeDTO.review ?: CommonConstants.UNAUDITED)
                .createBy(userId)
                .remark(advanceChargeDTO.remark)
                .build()

            if (advanceChargeDTO.id == null) {
                financialMain.createTime = LocalDateTime.now()
            } else {
                financialMain.updateBy = userId
                financialMain.updateTime = LocalDateTime.now()
            }

            val isFinancialMainAdded = saveOrUpdate(financialMain)

            val financialSubList = advanceChargeDTO.tableData.toFinancialSubList(financialMainId)
            val areFinancialSubsAdded = financialSubService.saveBatch(financialSubList)
            val isMemberUpdated = memberService.updateAdvanceChargeAmount(advanceChargeDTO.memberId, advanceChargeDTO.totalAmount)

            if (isFinancialMainAdded && areFinancialSubsAdded && isMemberUpdated) {
                return Response.responseMsg(FinancialCodeEnum.ADD_ADVANCE_SUCCESS)
            }
            return Response.responseMsg(FinancialCodeEnum.ADD_ADVANCE_ERROR)
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    private fun List<AdvanceChargeDataBO>.toFinancialSubList(financialMainId: Long): List<FinancialSub> {
        return map {
            FinancialSub.builder()
                .id(SnowflakeIdUtil.nextId())
                .financialMainId(financialMainId)
                .accountId(it.accountId)
                .singleAmount(it.amount)
                .remark(it.remark)
                .build()
        }
    }

    override fun getAdvanceChargePageList(advanceChargeDTO: QueryAdvanceChargeDTO?): Response<Page<AdvanceChargeVO>> {
        val page = advanceChargeDTO?.run { Page<FinancialMain>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<FinancialMain>().apply {
            advanceChargeDTO?.financialPersonnelId?.let { eq(FinancialMain::getOperatorId, it) }
            advanceChargeDTO?.receiptNumber?.let { eq(FinancialMain::getReceiptNumber, it) }
            advanceChargeDTO?.status?.let { eq(FinancialMain::getStatus, it) }
            advanceChargeDTO?.operatorId?.let { eq(FinancialMain::getCreateBy, it) }
            advanceChargeDTO?.remark?.let { like(FinancialMain::getRemark, it) }
            advanceChargeDTO?.startDate?.let { ge(FinancialMain::getCreateTime, it) }
            advanceChargeDTO?.endDate?.let { le(FinancialMain::getCreateTime, it) }
            eq(FinancialMain::getType, "收预付款")
            eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
            orderByDesc(FinancialMain::getCreateTime)
        }

        val result = page?.run {
            financialMainMapper.selectPage(this, wrapper)
            records.map { financialMain ->
                val member = memberService.getMemberById(financialMain.relatedPersonId)
                val operator = userService.getById(financialMain.createBy)
                val financialPerson = operatorService.getOperatorById(financialMain.operatorId)

                financialMain.toAdvanceChargeVO(member, operator, financialPerson)
            }.toPage(this@run.total, this@run.pages, this@run.size)
        }
        return result?.let { Response.responseData(it) } ?: Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    // Extension function to convert FinancialMain to AdvanceChargeVO
    private fun FinancialMain.toAdvanceChargeVO(member: Member?, operator: SysUser, financialPerson: Operator?): AdvanceChargeVO {
        return AdvanceChargeVO(
            id = this.id,
            receiptNumber = this.receiptNumber,
            receiptDate = this.receiptDate,
            operator = operator.name,
            financialPersonnel = financialPerson?.name ?: "",
            memberName = member?.memberName ?: "",
            totalAmount = this.totalAmount,
            collectedAmount = this.changeAmount ?: BigDecimal.ZERO,
            status = this.status,
            remark = this.remark
        )
    }

    private fun getAdvanceChargeList(advanceChargeDTO: QueryAdvanceChargeDTO?): List<AdvanceChargeVO> {
        val wrapper = LambdaQueryWrapper<FinancialMain>().apply {
            advanceChargeDTO?.financialPersonnelId?.let { eq(FinancialMain::getOperatorId, it) }
            advanceChargeDTO?.receiptNumber?.let { eq(FinancialMain::getReceiptNumber, it) }
            advanceChargeDTO?.status?.let { eq(FinancialMain::getStatus, it) }
            advanceChargeDTO?.operatorId?.let { eq(FinancialMain::getCreateBy, it) }
            advanceChargeDTO?.remark?.let { like(FinancialMain::getRemark, it) }
            advanceChargeDTO?.startDate?.let { ge(FinancialMain::getCreateTime, it) }
            advanceChargeDTO?.endDate?.let { le(FinancialMain::getCreateTime, it) }
            eq(FinancialMain::getType, "收预付款")
            eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = financialMainMapper.selectList(wrapper)
        return result.map { financialMain ->
            val member = memberService.getMemberById(financialMain.relatedPersonId)
            val operator = userService.getById(financialMain.createBy)
            val financialPerson = operatorService.getOperatorById(financialMain.operatorId)

            financialMain.toAdvanceChargeVO(member, operator, financialPerson)
        }
    }

    // Extension function, converting List to Page
    private fun <T> List<T>.toPage(total: Long, pages: Long, size: Long): Page<T> {
        return Page<T>().apply {
            records = this@toPage
            this.total = total
            this.pages = pages
            this.size = size
        }
    }

    override fun getAdvanceChargeDetailById(id: Long): Response<AdvanceChargeDetailVO> {
        val financialMain = getById(id)
        if(financialMain != null) {
            val member = memberService.getMemberById(financialMain.relatedPersonId)

            val financialPerson = operatorService.getOperatorById(financialMain.operatorId)
            val subData = financialSubService.lambdaQuery()
                .eq(FinancialSub::getFinancialMainId, id)
                .eq(FinancialSub::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list()

            val tableData = ArrayList<AdvanceChargeDataBO>()
            subData.map { financialSub ->
                val account = accountService.getById(financialSub.accountId)
                val record = AdvanceChargeDataBO(
                    accountId = financialSub.accountId,
                    accountName = account.accountName,
                    amount = financialSub.singleAmount,
                    remark = financialSub.remark,
                )
                tableData.add(record)
            }

            val filesData = ArrayList<FileDataBO>()
            if(!financialMain.fileId.isNullOrEmpty()) {
                val ids = financialMain.fileId.split(",").map { it.toLong() }
                val fileList = fileMapper.selectBatchIds(ids)
                fileList.map { file ->
                    val fileBo = FileDataBO(
                        id = file.id,
                        fileName = file.fileName,
                        fileUrl = file.fileUrl,
                        fileType = file.fileType,
                        fileSize = file.fileSize
                    )
                    filesData.add(fileBo)
                }
            }

            val resultVO = AdvanceChargeDetailVO(
                memberId = financialMain.relatedPersonId,
                memberName = member?.memberName ?: "",
                receiptNumber = financialMain.receiptNumber,
                receiptDate = financialMain.receiptDate,
                financialPersonnel = financialPerson?.name ?: "",
                financialPersonnelId = financialPerson?.id,
                totalAmount = financialMain.totalAmount,
                collectedAmount = financialMain.changeAmount,
                tableData = tableData,
                remark = financialMain.remark,
                files = filesData,
                status = financialMain.status
            )
            return Response.responseData(resultVO);
        }
        return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    @Transactional
    override fun deleteAdvanceChargeById(ids: List<Long>): Response<String> {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }
        val financialMainList = ids.map { id ->
            FinancialMain.builder()
                .id(id)
                .deleteFlag(CommonConstants.DELETED)
                .build()
        }
        val isDeleted = updateBatchById(financialMainList)
        val financialSubList = financialSubService.lambdaQuery()
            .`in`(FinancialSub::getFinancialMainId, ids)
            .list()
        val financialSubIdList = financialSubList.map { it.id }
        val isFinancialSubDeleted = financialSubService.updateBatchById(financialSubIdList.map { id ->
            FinancialSub.builder()
                .id(id)
                .deleteFlag(CommonConstants.DELETED)
                .build()
        })
        if (isDeleted && isFinancialSubDeleted) {
            return Response.responseMsg(FinancialCodeEnum.DELETE_ADVANCE_SUCCESS)
        }
        return Response.responseMsg(FinancialCodeEnum.DELETE_ADVANCE_ERROR)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun updateAdvanceChargeStatusById(ids: List<Long>, status: Int): Response<String> {
        val financialMainList = ids.map { id ->
            FinancialMain.builder()
                .id(id)
                .status(status)
                .build()
        }
        val isUpdated = updateBatchById(financialMainList)
        if (isUpdated) {
            return Response.responseMsg(FinancialCodeEnum.UPDATE_ADVANCE_SUCCESS)
        }
        return Response.responseMsg(FinancialCodeEnum.UPDATE_ADVANCE_ERROR)
    }

    override fun exportAdvanceCharge(advanceChargeDTO: QueryAdvanceChargeDTO, response: HttpServletResponse) {
        val exportMap = ConcurrentHashMap<String, List<List<Any>>>()
        val mainData = getAdvanceChargeList(advanceChargeDTO)
        if (mainData.isNotEmpty()) {
            exportMap["收预付款"] = ExcelUtils.getSheetData(mainData)
            if (advanceChargeDTO.isExportDetail == true) {
                val subData = mainData.flatMap { advanceChargeVO ->
                    advanceChargeVO.id?.let { getAdvanceChargeDetailById(it) }?.data?.tableData?.map { item ->
                        AdvanceChargeDataExportBO(
                            memberName = advanceChargeVO.memberName,
                            receiptNumber = advanceChargeVO.receiptNumber,
                            accountName = item.accountName,
                            amount = item.amount,
                            remark = item.remark
                        )
                    } ?: emptyList()
                }
                exportMap["收预付款单据明细"] = ExcelUtils.getSheetData(subData)
            }
            ExcelUtils.exportManySheet(response, "收预付款", exportMap)
        }
    }

    override fun exportAdvanceChargeDetail(receiptNumber: String, response: HttpServletResponse) {
        val id = lambdaQuery()
            .eq(FinancialMain::getReceiptNumber, receiptNumber)
            .eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
            .eq(FinancialMain::getType, "收预付款")
            .one()
            .id
        val detail = getAdvanceChargeDetailById(id)
        if (detail.data != null) {
            val exportData = ArrayList<AdvanceChargeDataExportBO>()
            detail.data.tableData.map { item ->
                detail.data?.let {
                    val data = AdvanceChargeDataExportBO(
                        memberName = detail.data.memberName,
                        receiptNumber = detail.data.receiptNumber,
                        accountName = item.accountName,
                        amount = item.amount,
                        remark = item.remark
                    )
                    exportData.add(data)
                }
            }
            ExcelUtils.export(response, "收预付款单据明细", ExcelUtils.getSheetData(exportData))
        }
    }
}