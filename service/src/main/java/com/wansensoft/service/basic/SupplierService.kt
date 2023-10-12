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
package com.wansensoft.service.basic

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansensoft.dto.basic.AddSupplierDTO
import com.wansensoft.dto.basic.QuerySupplierDTO
import com.wansensoft.dto.basic.UpdateSupplierDTO
import com.wansensoft.dto.basic.UpdateSupplierStatusDTO
import com.wansensoft.entities.basic.Supplier
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.basic.SupplierVO

interface SupplierService : IService<Supplier> {

    fun getSupplierList(supplier: QuerySupplierDTO?): Response<Page<SupplierVO>>

    fun addSupplier(supplier: AddSupplierDTO?): Response<String>

    /**
     * 内部使用方法
     */
    fun batchAddSupplier(suppliers: List<Supplier>?): Boolean

    fun updateSupplier(supplier: UpdateSupplierDTO?): Response<String>

    fun deleteSupplier(ids: List<Long>?): Response<String>

    fun updateSupplierStatus(updateSupplierStatusDTO: UpdateSupplierStatusDTO?): Response<String>
}