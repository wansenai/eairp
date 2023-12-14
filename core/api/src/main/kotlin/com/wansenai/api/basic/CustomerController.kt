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
import com.wansenai.dto.basic.QueryCustomerDTO
import com.wansenai.dto.basic.AddOrUpdateCustomerDTO
import com.wansenai.service.basic.CustomerService
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.CustomerVO
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.ModelAttribute

@RestController
@RequestMapping("/basic/customer")
class CustomerController (private val customerService: CustomerService){

    @PostMapping("/pageList")
    fun customerPageList(@RequestBody queryCustomerDTO: QueryCustomerDTO?) : Response<Page<CustomerVO>> {
        return customerService.getCustomerPageList(queryCustomerDTO)
    }

    @GetMapping("list")
    fun customerList() : Response<List<CustomerVO>> {
        return customerService.getCustomerList(null)
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdateCustomer(@RequestBody addOrUpdateCustomerDTO: AddOrUpdateCustomerDTO) : Response<String> {
        return customerService.addOrUpdateCustomer(addOrUpdateCustomerDTO)
    }

    @DeleteMapping("/deleteBatch")
    fun deleteBatchCustomer(@RequestParam ids: List<Long>?) : Response<String> {
        return customerService.deleteCustomer(ids)
    }

    @PostMapping("/updateStatus")
    fun updateCustomerStatus(@RequestParam("ids") ids: List<Long>?, @RequestParam("status") status: Int?) : Response<String> {
        return customerService.updateCustomerStatus(ids, status)
    }

    @GetMapping("export")
    fun export(@ModelAttribute queryCustomerDTO: QueryCustomerDTO, response: HttpServletResponse) {
        customerService.exportCustomerData(queryCustomerDTO, response)
    }
}