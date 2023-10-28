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
package com.wansenai.api.financial

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.wansenai.dto.financial.AddOrUpdateAdvanceChargeDTO
import com.wansenai.dto.financial.QueryAdvanceChargeDTO
import com.wansenai.service.financial.AdvanceChargeService
import com.wansenai.utils.response.Response
import com.wansenai.vo.financial.AdvanceChargeDetailVO
import com.wansenai.vo.financial.AdvanceChargeVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/financial/advance-charge")
class AdvanceChargeController(private val advanceChargeService: AdvanceChargeService) {

    @PostMapping("addOrUpdate")
    fun addOrUpdateAdvanceCharge(@RequestBody advanceChargeDTO: AddOrUpdateAdvanceChargeDTO): Response<String> {
        return advanceChargeService.addOrUpdateAdvanceCharge(advanceChargeDTO)
    }

    @PostMapping("pageList")
    fun getAdvanceChargePageList(@RequestBody advanceChargeDTO: QueryAdvanceChargeDTO) : Response<Page<AdvanceChargeVO>> {
        return advanceChargeService.getAdvanceChargePageList(advanceChargeDTO)
    }

    @GetMapping("getDetailById/{id}")
    fun getDetailById(@PathVariable id: Long) : Response<AdvanceChargeDetailVO> {
        return advanceChargeService.getAdvanceChargeDetailById(id)
    }

    @PutMapping("deleteByIds")
    fun deleteByIds(@RequestParam("ids") ids: List<Long>) : Response<String> {
        return advanceChargeService.deleteAdvanceChargeById(ids)
    }

    @PutMapping("updateStatusByIds")
    fun updateStatus(@RequestParam("ids") ids: List<Long>, @RequestParam("status") status: Int) : Response<String> {
        return advanceChargeService.updateAdvanceChargeStatusById(ids, status)
    }
}