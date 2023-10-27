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
import com.wansenai.dto.financial.AddOrUpdateAdvanceChargeDTO
import com.wansenai.dto.financial.QueryAdvanceChargeDTO
import com.wansenai.mappers.financial.FinancialMainMapper
import com.wansenai.service.basic.MemberService
import com.wansenai.service.financial.AdvanceChargeService
import com.wansenai.service.system.SysDepartmentService
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
import org.springframework.transaction.annotation.Transactional

@Service
@Slf4j
open class AdvanceChargeServiceImpl(
    private val baseService: com.wansenai.service.BaseService,
    private val userDeptRelService: com.wansenai.service.user.ISysUserDeptRelService,
    private val departmentService: SysDepartmentService,
    private val financialSubService: com.wansenai.service.financial.FinancialSubService,
    private val financialMainMapper: FinancialMainMapper,
    private val memberService: MemberService,
) : ServiceImpl<FinancialMainMapper, FinancialMain>(), AdvanceChargeService {

    @Transactional
    override fun addOrUpdateAdvanceCharge(advanceChargeDTO: AddOrUpdateAdvanceChargeDTO): Response<String> {
        // 先判断如果memberId和receiptDate为空返回必填参数错误
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
        }

        // If the id is empty, it is a new addition, otherwise it is a modification
        val fileIds = advanceChargeDTO.files?.map { it.id }?.joinToString(",")

        if (advanceChargeDTO.tableData.isNotEmpty()) {
            val financialMainId = SnowflakeIdUtil.nextId()
            val financialMain = FinancialMain.builder()
                .id(financialMainId)
                .organizationId(deptId)
                .handsPersonId(advanceChargeDTO.financialPersonnelId)
                .type("收预付款")
                .changePrice(advanceChargeDTO.totalAmount)
                .totalPrice(advanceChargeDTO.totalAmount)
                .receiptNumber(advanceChargeDTO.receiptNumber)
                .receiptSource(0)
                .receiptTime(TimeUtil.parse(advanceChargeDTO.receiptDate))
                .fileId(fileIds)
                .remark(advanceChargeDTO.remark)
                .build()

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
        val wrapper = LambdaQueryWrapper<FinancialMain>().apply{
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
            val listVo = records.map { financialMain ->
//                AdvanceChargeVO(
//                    id = financialMain.id,
//                )
            }
            Page<AdvanceChargeVO>().apply {
                records = null
                total = this@run.total
                pages = this@run.pages
                size = this@run.size
            }
        }
        return result?.let { Response.responseData(it) } ?: Response.responseMsg(
            BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    override fun getAdvanceChargeDetailById(id: Long): Response<AdvanceChargeDetailVO> {
        TODO("Not yet implemented")
    }

    override fun deleteAdvanceChargeById(ids: List<Long>): Response<String> {
        TODO("Not yet implemented")
    }

    override fun updateAdvanceChargeStatusById(ids: List<Long>, status: Int): Response<String> {
        TODO("Not yet implemented")
    }
}