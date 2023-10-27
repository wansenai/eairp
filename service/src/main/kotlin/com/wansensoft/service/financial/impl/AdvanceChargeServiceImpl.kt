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
package com.wansensoft.service.financial.impl

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.bo.AdvanceChargeDataBO
import com.wansensoft.dto.financial.AddOrUpdateAdvanceChargeDTO
import com.wansensoft.dto.financial.QueryAdvanceChargeDTO
import com.wansensoft.entities.SysDepartment
import com.wansensoft.entities.financial.FinancialMain
import com.wansensoft.entities.financial.FinancialSub
import com.wansensoft.entities.user.SysUserDeptRel
import com.wansensoft.mappers.financial.FinancialMainMapper
import com.wansensoft.mappers.financial.FinancialSubMapper
import com.wansensoft.service.BaseService
import com.wansensoft.service.basic.MemberService
import com.wansensoft.service.financial.AdvanceChargeService
import com.wansensoft.service.financial.FinancialSubService
import com.wansensoft.service.system.SysDepartmentService
import com.wansensoft.service.user.ISysUserDeptRelService
import com.wansensoft.utils.SnowflakeIdUtil
import com.wansensoft.utils.TimeUtil
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.FinancialCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.financial.AdvanceChargeDetailVO
import com.wansensoft.vo.financial.AdvanceChargeVO
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
@Slf4j
open class AdvanceChargeServiceImpl(
    private val baseService: BaseService,
    private val userDeptRelService: ISysUserDeptRelService,
    private val departmentService: SysDepartmentService,
    private val financialSubService: FinancialSubService,
    private val memberService: MemberService,
) : ServiceImpl<FinancialMainMapper, FinancialMain>(), AdvanceChargeService {

    @Transactional
    override fun addOrUpdateAdvanceCharge(advanceChargeDTO: AddOrUpdateAdvanceChargeDTO): Response<String> {
        // 先判断如果memberId和receiptDate为空返回必填参数错误
        if (advanceChargeDTO.memberId == null || advanceChargeDTO.receiptDate.isNullOrEmpty()) {
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
        TODO("Not yet implemented")
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