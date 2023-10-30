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
import com.wansenai.entities.SysDepartment
import com.wansenai.entities.financial.FinancialMain
import com.wansenai.entities.financial.FinancialSub
import com.wansenai.bo.AdvanceChargeDataBO
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
import com.wansenai.service.system.SysDepartmentService
import com.wansenai.service.user.ISysUserService
import com.wansenai.utils.SnowflakeIdUtil
import com.wansenai.utils.TimeUtil
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.FinancialCodeEnum
import com.wansenai.utils.response.Response
import com.wansenai.vo.financial.AdvanceChargeDetailVO
import com.wansenai.vo.financial.AdvanceChargeVO
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Slf4j
open class AdvanceChargeServiceImpl(
    private val baseService: com.wansenai.service.BaseService,
    private val userDeptRelService: com.wansenai.service.user.ISysUserDeptRelService,
    private val departmentService: SysDepartmentService,
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
        var deptId: Long? = null
        val userDeptRelList = userDeptRelService.queryByUserId(userId)

        deptId = userDeptRelList.takeIf { it.isNotEmpty() }
            ?.map { it.deptId }
            ?.let { deptIdList ->
                departmentService.lambdaQuery()
                    .`in`(deptIdList.isNotEmpty(), SysDepartment::getId, deptIdList)
                    .list()
                    .firstOrNull()
                    ?.let { it.parentId ?: it.id }
            }


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
                .organizationId(deptId)
                .handsPersonId(advanceChargeDTO.financialPersonnelId)
                .type("收预付款")
                .memberId(advanceChargeDTO.memberId)
                .changePrice(advanceChargeDTO.totalAmount)
                .totalPrice(advanceChargeDTO.totalAmount)
                .receiptNumber(advanceChargeDTO.receiptNumber)
                .receiptSource(0)
                .receiptTime(TimeUtil.parse(advanceChargeDTO.receiptDate))
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
            advanceChargeDTO?.financialPersonnelId?.let { eq(FinancialMain::getHandsPersonId, it) }
            advanceChargeDTO?.receiptNumber?.let { eq(FinancialMain::getReceiptNumber, it) }
            advanceChargeDTO?.status?.let { eq(FinancialMain::getStatus, it) }
            advanceChargeDTO?.operatorId?.let { eq(FinancialMain::getCreateBy, it) }
            advanceChargeDTO?.remark?.let { like(FinancialMain::getRemark, it) }
            advanceChargeDTO?.startDate?.let { ge(FinancialMain::getCreateTime, it) }
            advanceChargeDTO?.endDate?.let { le(FinancialMain::getCreateTime, it) }
            eq(FinancialMain::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = page?.run {
            financialMainMapper.selectPage(this, wrapper)
            records.map { financialMain ->
                val member = memberService.getMemberById(financialMain.memberId)
                val operator = userService.getById(financialMain.createBy)
                val financialPerson = operatorService.getOperatorById(financialMain.handsPersonId)

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
            receiptDate = this.receiptTime,
            operator = operator.name,
            financialPersonnel = financialPerson?.name ?: "",
            memberName = member?.memberName,
            totalAmount = this.totalPrice,
            collectedAmount = this.changePrice,
            status = this.status,
            remark = this.remark
        )
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
            val member = memberService.getMemberById(financialMain.memberId)

            val financialPerson = operatorService.getOperatorById(financialMain.handsPersonId)
            val subData = financialSubService.lambdaQuery()
                .eq(FinancialSub::getFinancialMainId, id)
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
                memberId = financialMain.memberId,
                memberName = member?.memberName,
                receiptNumber = financialMain.receiptNumber,
                receiptDate = financialMain.receiptTime,
                financialPersonnel = financialPerson?.name ?: "",
                financialPersonnelId = financialPerson?.id,
                totalAmount = financialMain.totalPrice,
                collectedAmount = financialMain.changePrice,
                tableData = tableData,
                remark = financialMain.remark,
                files = filesData
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
}