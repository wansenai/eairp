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
import com.wansenai.dto.basic.AddOrUpdateCustomerDTO
import com.wansenai.dto.basic.QueryCustomerDTO
import com.wansenai.entities.basic.Customer
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.CustomerVO

interface CustomerService: IService<Customer> {

    fun getCustomerPageList(queryCustomerDTO: QueryCustomerDTO?): Response<Page<CustomerVO>>

    fun getCustomerList(): Response<List<CustomerVO>>

    fun addOrUpdateCustomer(addOrUpdateCustomerDTO: AddOrUpdateCustomerDTO): Response<String>

    fun deleteCustomer(ids: List<Long>?): Response<String>

    fun updateCustomerStatus(ids: List<Long>?, status: Int?): Response<String>

    fun batchAddCustomer(customers: List<Customer>?): Boolean
}