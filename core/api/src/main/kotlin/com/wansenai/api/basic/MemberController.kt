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
package com.wansenai.api.basic

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.wansenai.dto.basic.AddOrUpdateMemberDTO
import com.wansenai.dto.basic.QueryMemberDTO
import com.wansenai.service.basic.MemberService
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.MemberVO
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ModelAttribute

@RestController
@RequestMapping("/basic/member")
class MemberController (private val memberService: MemberService){

    @PostMapping("/pageList")
    fun getMemberPageList(@RequestBody memberDTO: QueryMemberDTO) : Response<Page<MemberVO>> {
        return memberService.getMemberPageList(memberDTO)
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdateMember(@RequestBody memberDTO: AddOrUpdateMemberDTO) : Response<String> {
        return memberService.addOrUpdateMember(memberDTO)
    }

    @DeleteMapping("/deleteBatch")
    fun deleteBatchMembers(@RequestParam ids: List<Long>) : Response<String> {
        return memberService.deleteBatchMember(ids)
    }

    @PostMapping("/updateStatus")
    fun updateMemberStatus(@RequestParam("ids") ids: List<Long>, @RequestParam("status") status: Int) : Response<String> {
        return memberService.updateMemberStatus(ids, status)
    }

    @GetMapping("/list")
    fun getMemberList() : Response<List<MemberVO>> {
        return memberService.getMemberList(null)
    }

    @GetMapping("export")
    fun export(@ModelAttribute queryMemberDTO: QueryMemberDTO, response: HttpServletResponse) {
        memberService.exportMemberData(queryMemberDTO, response)
    }
}