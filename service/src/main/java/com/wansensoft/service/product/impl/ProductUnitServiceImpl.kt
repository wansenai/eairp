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
package com.wansensoft.service.product.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.product.AddOrUpdateProductUnitDTO
import com.wansensoft.dto.product.ProductUnitQueryDTO
import com.wansensoft.entities.product.ProductUnit
import com.wansensoft.mappers.product.ProductUnitMapper
import com.wansensoft.service.product.ProductUnitService
import com.wansensoft.utils.SnowflakeIdUtil
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.ProdcutCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.product.ProductUnitVO
import org.springframework.stereotype.Service

@Service
open class ProductUnitServiceImpl(
    private val productUnitMapper: ProductUnitMapper
) :ServiceImpl<ProductUnitMapper, ProductUnit>(), ProductUnitService {

    override fun productUnitList(productUnitQuery: ProductUnitQueryDTO?): Response<Page<ProductUnitVO>> {
        val page = productUnitQuery?.let { query ->
            val pageSizeDTO = query.page
            val page = pageSizeDTO?.page ?: 0L
            val pageSize = pageSizeDTO?.pageSize ?: 10L
            Page<ProductUnit>(page, pageSize)
        }
        val wrapper = LambdaQueryWrapper<ProductUnit>().apply {
            productUnitQuery?.computeUnit?.let { like(ProductUnit::getComputeUnit, it) }
            eq(ProductUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = page?.run {
            productUnitMapper.selectPage(this, wrapper)
            val listVo = records.map { unit ->
                ProductUnitVO(
                    id              = unit.id,
                    computeUnit     = unit.computeUnit,
                    basicUnit       = unit.basicUnit,
                    otherUnit       = unit.otherUnit,
                    otherUnitTwo    = unit.otherUnitTwo,
                    otherUnitThree  = unit.otherUnitThree,
                    status          = unit.status,
                    createTime      = unit.createTime,
                )
            }
            Page<ProductUnitVO>().apply {
                records = listVo
                total   = this@run.total
                pages   = this@run.pages
                size    = this@run.size
            }
        } ?: Page<ProductUnitVO>()

        return Response.responseData(result)
    }


    override fun addOrUpdateProductUnit(productUnit: AddOrUpdateProductUnitDTO?): Response<String> {
        productUnit?.let { unit ->
            val unitId = unit.id ?: SnowflakeIdUtil.nextId()
            val unitWrapper = LambdaQueryWrapper<ProductUnit>().apply {
                eq(ProductUnit::getComputeUnit, buildComputeUnit(unit))
                eq(ProductUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
                unit.id?.let { ne(ProductUnit::getId, it) }
            }
            val unitExists = productUnitMapper.exists(unitWrapper)
            if (unitExists) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_COMPUTE_UNIT_EXIST)
            }

            val result = if (unit.id == null) {
                save(buildProductUnit(unitId, unit))
            } else {
                updateById(buildProductUnit(unitId, unit))
            }

            return if (result) {
                if (unit.id == null) {
                    Response.responseMsg(ProdcutCodeEnum.PRODUCT_UNIT_ADD_SUCCESS)
                } else {
                    Response.responseMsg(ProdcutCodeEnum.PRODUCT_UNIT_UPDATE_SUCCESS)
                }
            } else {
                if (unit.id == null) {
                    Response.responseMsg(ProdcutCodeEnum.PRODUCT_UNIT_ADD_ERROR)
                } else {
                    Response.responseMsg(ProdcutCodeEnum.PRODUCT_UNIT_UPDATE_ERROR)
                }
            }
        } ?: return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    private fun buildProductUnit(id: Long, unit: AddOrUpdateProductUnitDTO): ProductUnit {
        return ProductUnit().apply {
            this.id = id
            computeUnit = buildComputeUnit(unit)
            basicUnit = unit.basicUnit
            otherUnit = unit.otherUnit
            otherUnitTwo = unit.otherUnitTwo
            otherUnitThree = unit.otherUnitThree
            ratio = unit.ratio
            ratioTwo = unit.ratioTwo
            ratioThree = unit.ratioThree
            status = unit.status
        }
    }

    private fun buildComputeUnit(productUnit: AddOrUpdateProductUnitDTO): String {
        val computeUnit = StringBuilder()
        computeUnit.append("${productUnit.basicUnit}/(${productUnit.otherUnit}=${productUnit.ratio})")
        productUnit.otherUnitTwo?.let { computeUnit.append("/(${it}=${productUnit.ratioTwo})") }
        productUnit.otherUnitThree?.let { computeUnit.append("/(${it}=${productUnit.ratioThree})") }
        return computeUnit.toString()
    }

    override fun deleteProductUnit(ids:  List<Long>?): Response<String> {
        ids?.let {
            val deleteResult = productUnitMapper.deleteBatchIds(ids)
            if(deleteResult == 0) {
                return Response.responseMsg(ProdcutCodeEnum.PRODUCT_UNIT_DELETE_ERROR)
            }
            return Response.responseMsg(ProdcutCodeEnum.PRODUCT_UNIT_DELETE_SUCCESS)
        }?: return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

}