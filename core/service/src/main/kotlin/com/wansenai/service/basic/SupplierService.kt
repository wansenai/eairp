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
import com.wansenai.dto.basic.AddSupplierDTO
import com.wansenai.dto.basic.QuerySupplierDTO
import com.wansenai.dto.basic.UpdateSupplierDTO
import com.wansenai.dto.basic.UpdateSupplierStatusDTO
import com.wansenai.entities.basic.Supplier
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.SupplierVO
import jakarta.servlet.http.HttpServletResponse

interface SupplierService : IService<Supplier> {

    fun getSupplierPageList(supplier: QuerySupplierDTO?): Response<Page<SupplierVO>>

    fun addSupplier(supplier: AddSupplierDTO?): Response<String>

    fun getSupplierList(supplier: QuerySupplierDTO?): Response<List<SupplierVO>>

    /**
     * 内部使用方法
     */
    fun batchAddSupplier(suppliers: List<Supplier>?): Boolean

    fun updateSupplier(supplier: UpdateSupplierDTO?): Response<String>

    fun deleteSupplier(ids: List<Long>?): Response<String>

    fun updateSupplierStatus(updateSupplierStatusDTO: UpdateSupplierStatusDTO?): Response<String>

    fun exportSupplierData(querySupplierDTO: QuerySupplierDTO, response: HttpServletResponse)
}