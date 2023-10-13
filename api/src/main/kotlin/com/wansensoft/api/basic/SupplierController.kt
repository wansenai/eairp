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
package com.wansensoft.api.basic

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.wansensoft.dto.basic.AddSupplierDTO
import com.wansensoft.dto.basic.QuerySupplierDTO
import com.wansensoft.dto.basic.UpdateSupplierDTO
import com.wansensoft.dto.basic.UpdateSupplierStatusDTO
import com.wansensoft.service.basic.SupplierService
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.basic.SupplierVO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/basic/supplier")
class SupplierController (private val supplierService: SupplierService){

    @PostMapping("/list")
    fun supplierList(@RequestBody querySupplierDTO: QuerySupplierDTO) : Response<Page<SupplierVO>> {
        return supplierService.getSupplierList(querySupplierDTO)
    }

    @PostMapping("/add")
    fun addSupplier(@RequestBody addSupplierDTO: AddSupplierDTO) : Response<String> {
        return supplierService.addSupplier(addSupplierDTO)
    }

    @PostMapping("/update")
    fun updateSupplier(@RequestBody updateSupplierDTO: UpdateSupplierDTO) : Response<String> {
        return supplierService.updateSupplier(updateSupplierDTO)
    }

    @DeleteMapping("/deleteBatch")
    fun deleteSupplier(@RequestParam ids: List<Long>) : Response<String> {
        return supplierService.deleteSupplier(ids)
    }

    @PostMapping("/updateStatus")
    fun updateSupplierStatus(@RequestBody updateSupplierStatusDTO: UpdateSupplierStatusDTO) : Response<String> {
        return supplierService.updateSupplierStatus(updateSupplierStatusDTO)
    }
}