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
package com.wansenai.service.basic

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansenai.dto.basic.AddOrUpdateMemberDTO
import com.wansenai.dto.basic.QueryMemberDTO
import com.wansenai.entities.basic.Member
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.MemberVO
import java.math.BigDecimal

interface MemberService: IService<Member> {

    fun getMemberPageList(memberDTO: QueryMemberDTO?): Response<Page<MemberVO>>

    fun addOrUpdateMember(memberDTO: AddOrUpdateMemberDTO): Response<String>

    fun deleteBatchMember(ids: List<Long>): Response<String>

    fun updateMemberStatus(ids: List<Long>, status: Int): Response<String>

    fun batchAddMember(members: List<Member>?): Boolean

    fun getMemberList(): Response<List<MemberVO>>

    fun updateAdvanceChargeAmount(memberId: Long?, amount: BigDecimal): Boolean
}