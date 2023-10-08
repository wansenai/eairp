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
package com.wansensoft.service.product

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansensoft.dto.product.AddOrUpdateProductUnitDTO
import com.wansensoft.dto.product.ProductUnitQueryDTO
import com.wansensoft.dto.product.ProductUnitStatusDTO
import com.wansensoft.entities.product.ProductUnit
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.product.ProductUnitVO

interface ProductUnitService: IService<ProductUnit> {

    fun productUnitList(productUnitQuery: ProductUnitQueryDTO?): Response<Page<ProductUnitVO>>

    fun addOrUpdateProductUnit(productUnit: AddOrUpdateProductUnitDTO?): Response<String>

    fun deleteProductUnit(ids:  List<Long>?): Response<String>

    fun updateUnitStatus(productUnitStatus: ProductUnitStatusDTO?): Response<String>
}