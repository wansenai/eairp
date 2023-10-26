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
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.financial.AdvanceChargeDetailVO
import com.wansensoft.vo.financial.AdvanceChargeVO
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
@Slf4j
open class AdvanceChargeServiceImpl (
    private val financialMainMapper: FinancialMainMapper,
    private val baseService: BaseService,
    private val userDeptRelService: ISysUserDeptRelService,
    private val departmentService: SysDepartmentService,
    private val financialSubService: FinancialSubService,
    private val memberService: MemberService,
) : ServiceImpl<FinancialMainMapper, FinancialMain>(), AdvanceChargeService {

    @Transactional
    override fun addOrUpdateAdvanceCharge(advanceChargeDTO: AddOrUpdateAdvanceChargeDTO): Response<String> {
        // 先判断如果memberId 和 receiptDate为空返回必填参数错误
        if (advanceChargeDTO.memberId == null || !StringUtils.hasLength(advanceChargeDTO.receiptDate)) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }

        val userId = baseService.currentUserId;
        var deptId: Long? = null
        val userDeptRelList = userDeptRelService.queryByUserId(userId)

        if (userDeptRelList.isNotEmpty()) {
            val deptIdList = userDeptRelList.map { it.deptId }
            val deptList = departmentService.lambdaQuery()
                .`in`(deptIdList.isNotEmpty(), SysDepartment::getId, deptIdList)
                .list()
            if (deptList.isNotEmpty()) {
                val dept = deptList.first()
                deptId = dept.parentId ?: dept.id
            }
        }

        // If the id is empty, it is a new addition, otherwise it is a modification
        if (advanceChargeDTO.id == null) {
            val files = advanceChargeDTO.files
            var fileIds = ""
            if (!files.isNullOrEmpty()) {
                fileIds = files.map { it.id }.joinToString(",")
            }
            if (advanceChargeDTO.tableData.isNotEmpty()) {
                val accountIdList = advanceChargeDTO.tableData.map { it.accountId }
                val accountIdSet = accountIdList.toSet()
                if (accountIdSet.size == 1) {
                    // Selected an account
                    val accountId = accountIdSet.first()
                    val financialMain = FinancialMain.builder()
                        .id(SnowflakeIdUtil.nextId())
                        .organizationId(deptId)
                        .handsPersonId(advanceChargeDTO.financialPersonnelId)
                        .accountId(accountId)
                        .type("收预付款")
                        .changePrice(advanceChargeDTO.totalAmount)
                        .totalPrice(advanceChargeDTO.totalAmount)
                        .receiptNumber(advanceChargeDTO.receiptNumber)
                        .receiptSource(0)
                        .receiptTime(TimeUtil.parse(advanceChargeDTO.receiptDate))
                        .fileId(fileIds)
                        .remark(advanceChargeDTO.remark)
                        .build()
                    save(financialMain)

                    // 把tableData的数据插入到financial_sub表中 循环批量插入
                    val financialSubList = mutableListOf<FinancialSub>()
                    for (advanceChargeDataBO in advanceChargeDTO.tableData) {
                        val financialSub = FinancialSub.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .financialMainId(financialMain.id)
                            .accountId(advanceChargeDataBO.accountId)
                            .singleAmount(advanceChargeDataBO.amount)
                            .remark(advanceChargeDataBO.remark)
                            .build()
                        financialSubList.add(financialSub)
                    }
                    financialSubService.saveBatch(financialSubList)

                } else {
                    // Multiple accounts selected
                    val financialMainList = mutableListOf<FinancialMain>()
                    for (accountId in accountIdSet) {
                        val financialMain = FinancialMain.builder()
                            .id(SnowflakeIdUtil.nextId())
                            .organizationId(deptId)
                            .handsPersonId(advanceChargeDTO.financialPersonnelId)
                            .accountId(accountId)
                            .type("收预付款")
                            .changePrice(advanceChargeDTO.totalAmount)
                            .totalPrice(advanceChargeDTO.totalAmount)
                            .receiptNumber(advanceChargeDTO.receiptNumber)
                            .receiptSource(0)
                            .receiptTime(TimeUtil.parse(advanceChargeDTO.receiptDate))
                            .fileId(fileIds)
                            .remark(advanceChargeDTO.remark)
                            .build()
                        financialMainList.add(financialMain)
                    }
                    saveBatch(financialMainList)
                }
                // Modify member prepayment balance
                memberService.updateAdvanceChargeAmount(advanceChargeDTO.memberId, advanceChargeDTO.totalAmount)
            }
        }
        return Response.success();
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