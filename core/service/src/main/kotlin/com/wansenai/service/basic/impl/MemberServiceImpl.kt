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
package com.wansenai.service.basic.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.bo.MemberExportBO
import com.wansenai.bo.MemberExportEnBO
import com.wansenai.entities.basic.Member
import com.wansenai.dto.basic.AddOrUpdateMemberDTO
import com.wansenai.dto.basic.QueryMemberDTO
import com.wansenai.mappers.basic.MemberMapper
import com.wansenai.service.BaseService
import com.wansenai.service.basic.MemberService
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.MemberCodeEnum
import com.wansenai.utils.excel.ExcelUtils
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.MemberVO
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.math.BigDecimal
import java.time.LocalDateTime

@Slf4j
@Service
open class MemberServiceImpl(
    private val baseService: BaseService,
    private val memberMapper: MemberMapper
) : ServiceImpl<MemberMapper, Member>(), MemberService {

    override fun getMemberPageList(memberDTO: QueryMemberDTO?): Response<Page<MemberVO>> {
        val page = memberDTO?.run { Page<Member>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<Member>().apply {
            memberDTO?.memberNumber?.let { like(Member::getMemberNumber, it) }
            memberDTO?.phoneNumber?.let { like(Member::getPhoneNumber, it) }
            memberDTO?.startDate?.let { ge(Member::getCreateTime, it) }
            memberDTO?.endDate?.let { le(Member::getCreateTime, it) }
            eq(Member::getDeleteFlag, CommonConstants.NOT_DELETED)
            orderByDesc(Member::getCreateTime)
        }

        val result = page?.run {
            memberMapper.selectPage(this, wrapper)
            val listVo = records.map { member ->
                MemberVO(
                    id = member.id,
                    memberNumber = member.memberNumber,
                    memberName = member.memberName,
                    phoneNumber = member.phoneNumber,
                    email = member.email,
                    advancePayment = member.advancePayment,
                    status = member.status,
                    remark = member.remark,
                    sort = member.sort,
                    createTime = member.createTime
                )
            }
            Page<MemberVO>().apply {
                records = listVo
                total = this@run.total
                pages = this@run.pages
                size = this@run.size
            }
        }
        return result.let { Response.responseData(it) } ?: Response.responseMsg(
            BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    @Transactional
    override fun addOrUpdateMember(memberDTO: AddOrUpdateMemberDTO): Response<String> {
        val userId = baseService.getCurrentUserId()
        val isAdd = memberDTO.id == null
        val systemLanguage = baseService.currentUserSystemLanguage
        val member = Member().apply {
            id = memberDTO.id
            memberNumber = memberDTO.memberNumber
            memberName = memberDTO.memberName
            phoneNumber = memberDTO.phoneNumber.orEmpty()
            email = memberDTO.email.orEmpty()
            advancePayment = memberDTO.advancePayment ?: BigDecimal.ZERO
            status = memberDTO.status
            remark = memberDTO.remark.orEmpty()
            sort = memberDTO.sort ?: 0
            if (isAdd) {
                createTime = LocalDateTime.now()
                createBy = userId
            } else {
                updateTime = LocalDateTime.now()
                updateBy = userId
            }
        }
        val saveResult = saveOrUpdate(member)
        if (systemLanguage == "zh_CN") {
            return when {
                saveResult && isAdd -> Response.responseMsg(MemberCodeEnum.ADD_MEMBER_SUCCESS)
                saveResult && !isAdd -> Response.responseMsg(MemberCodeEnum.UPDATE_MEMBER_INFO_SUCCESS)
                else -> Response.fail()
            }
        } else {
            return when {
                saveResult && isAdd -> Response.responseMsg(MemberCodeEnum.ADD_MEMBER_SUCCESS_EN)
                saveResult && !isAdd -> Response.responseMsg(MemberCodeEnum.UPDATE_MEMBER_INFO_SUCCESS_EN)
                else -> Response.fail()
            }
        }
    }

    override fun deleteBatchMember(ids: List<Long>): Response<String> {
        return ids.takeIf { it.isNotEmpty() }
            ?.let {
                val updateResult = memberMapper.deleteBatchIds(it)
                val systemLanguage = baseService.currentUserSystemLanguage
                if (updateResult > 0) {
                    if (systemLanguage == "zh_CN") {
                        return Response.responseMsg(MemberCodeEnum.DELETE_MEMBER_SUCCESS)
                    }
                    return Response.responseMsg(MemberCodeEnum.DELETE_MEMBER_SUCCESS_EN)
                } else {
                    if (systemLanguage == "zh_CN") {
                        return Response.responseMsg(MemberCodeEnum.DELETE_MEMBER_ERROR)
                    }
                    Response.responseMsg(MemberCodeEnum.DELETE_MEMBER_ERROR_EN)
                }
            }
            ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun updateMemberStatus(ids: List<Long>, status: Int): Response<String> {
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }

        val updateResult = lambdaUpdate()
            .`in`(Member::getId, ids)
            .set(Member::getStatus, status)
            .update()
        val systemLanguage = baseService.currentUserSystemLanguage
        return if (!updateResult) {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(MemberCodeEnum.UPDATE_MEMBER_STATUS_ERROR)
            } else {
                Response.responseMsg(MemberCodeEnum.UPDATE_MEMBER_STATUS_ERROR_EN)
            }
        } else {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(MemberCodeEnum.UPDATE_MEMBER_STATUS_SUCCESS)
            } else {
                Response.responseMsg(MemberCodeEnum.UPDATE_MEMBER_STATUS_SUCCESS_EN)
            }
        }
    }

    @Transactional
    override fun batchAddMember(members: List<Member>?): Boolean {
        val memberEntities = mutableListOf<Member>()
        val existingMembers = HashSet<Pair<String, String>>()

        members?.forEach { member ->
            val memberKey = Pair(member.memberNumber, member.memberName)
            if (!existingMembers.contains(memberKey)) {
                val memberEntity = memberMapper.selectOne(
                    LambdaQueryWrapper<Member>()
                        .eq(Member::getMemberName, member.memberName)
                        .eq(Member::getMemberNumber, member.memberNumber)
                )
                if (memberEntity == null) {
                    val newMemberEntity = Member().apply {
                        BeanUtils.copyProperties(member, this)
                        createTime = LocalDateTime.now()
                        createBy = baseService.currentUserId
                    }
                    memberEntities.add(newMemberEntity)
                    existingMembers.add(memberKey)
                }
            }
        }
        return saveBatch(memberEntities)
    }

    override fun getMemberList(memberDTO: QueryMemberDTO?): Response<List<MemberVO>> {
        val memberList = list(
            LambdaQueryWrapper<Member>()
                .eq(StringUtils.hasLength(memberDTO?.memberNumber), Member::getMemberNumber, memberDTO?.memberNumber)
                .eq(StringUtils.hasLength(memberDTO?.phoneNumber), Member::getPhoneNumber, memberDTO?.phoneNumber)
                .ge(StringUtils.hasLength(memberDTO?.startDate), Member::getCreateTime, memberDTO?.startDate)
                .le(StringUtils.hasLength(memberDTO?.endDate), Member::getCreateTime, memberDTO?.endDate)
                .eq(Member::getStatus, CommonConstants.STATUS_NORMAL)
                .eq(Member::getDeleteFlag, CommonConstants.NOT_DELETED)
                .orderByAsc(Member::getSort)
        )
        val memberVOList = memberList.map { member ->
            MemberVO(
                id = member.id,
                memberNumber = member.memberNumber,
                memberName = member.memberName,
                phoneNumber = member.phoneNumber,
                email = member.email,
                advancePayment = member.advancePayment,
                status = member.status,
                remark = member.remark,
                sort = member.sort,
                createTime = member.createTime
            )
        }
        return Response.responseData(memberVOList)
    }

    override fun updateAdvanceChargeAmount(memberId: Long?, amount: BigDecimal): Boolean {
        if (memberId == null || amount <= BigDecimal.ZERO) {
            return false
        }
        val member = getById(memberId)
        if (member == null) {
            log.error("Member does not exist, memberId: $memberId")
            return false
        }
        return lambdaUpdate()
            .eq(Member::getId, memberId)
            .set(Member::getAdvancePayment, member.advancePayment.add(amount))
            .update()
    }

    override fun getMemberById(memberId: Long?): Member? {
        return memberId?.let {
            val member = getById(memberId)
            if (member == null) {
                log.error("Member does not exist, memberId: $memberId")
                return null;
            }
            return member
        }
    }

    override fun exportMemberData(memberDTO: QueryMemberDTO?, response: HttpServletResponse) {
        val members = getMemberList(memberDTO)
        if(members.data.isNotEmpty()) {
            val systemLanguage = baseService.currentUserSystemLanguage
            if (systemLanguage == "zh_CN") {
                val exportData = ArrayList<MemberExportBO>()
                members.data.forEach { member ->
                    val memberExportBO =  MemberExportBO(
                        id = member.id,
                        memberNumber = member.memberNumber,
                        memberName = member.memberName,
                        phoneNumber = member.phoneNumber,
                        email = member.email,
                        advancePayment = member.advancePayment,
                        status = member.status,
                        remark = member.remark,
                        sort = member.sort,
                        createTime = member.createTime
                    )
                    exportData.add(memberExportBO)
                }
                ExcelUtils.export(response, "会员信息", ExcelUtils.getSheetData(exportData))
            } else {
                val exportEnData = ArrayList<MemberExportEnBO>()
                members.data.forEach { member ->
                    val memberExportEnBO =  MemberExportEnBO(
                        id = member.id,
                        memberNumber = member.memberNumber,
                        memberName = member.memberName,
                        phoneNumber = member.phoneNumber,
                        email = member.email,
                        advancePayment = member.advancePayment,
                        status = member.status,
                        remark = member.remark,
                        sort = member.sort,
                        createTime = member.createTime
                    )
                    exportEnData.add(memberExportEnBO)
                }
                ExcelUtils.export(response, "Member Info", ExcelUtils.getSheetData(exportEnData))
            }
        }
    }
}