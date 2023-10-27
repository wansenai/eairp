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
package com.wansenai.api.product

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.wansenai.dto.product.AddOrUpdateProductAttributeDTO
import com.wansenai.dto.product.ProductAttributeQueryDTO
import com.wansenai.service.product.ProductAttributeService
import com.wansenai.utils.response.Response
import com.wansenai.vo.product.ProductAttributeNameVO
import com.wansenai.vo.product.ProductAttributeVO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/attribute")
class ProductAttributeController(private val productAttributeService: ProductAttributeService) {

    @PostMapping("/list")
    fun productAttributeList(@RequestBody productAttributeQueryDTO: ProductAttributeQueryDTO): Response<Page<ProductAttributeVO>> {
        return productAttributeService.productAttributeList(productAttributeQueryDTO)
    }

    @PostMapping("/addOrUpdate")
    fun addOrUpdateProductAttribute(@RequestBody productAttributeDTO: AddOrUpdateProductAttributeDTO): Response<String> {
        return productAttributeService.addOrUpdateProductAttribute(productAttributeDTO)
    }

    @DeleteMapping("/deleteBatch")
    fun deleteProductAttribute(@RequestParam ids: List<Long>): Response<String> {
        return productAttributeService.batchDeleteProductAttribute(ids)
    }

    @GetMapping("/getValuesById")
    fun getAttributeValuesById(@RequestParam("id") id: Long?): List<ProductAttributeNameVO> {
        return productAttributeService.getAttributeValuesById(id)
    }

}