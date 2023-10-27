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
package com.wansenai.service.product.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.dto.product.AddOrUpdateProductUnitDTO
import com.wansenai.dto.product.ProductUnitQueryDTO
import com.wansenai.dto.product.ProductUnitStatusDTO
import com.wansenai.entities.product.ProductUnit
import com.wansenai.mappers.product.ProductUnitMapper
import com.wansenai.service.BaseService
import com.wansenai.service.product.ProductUnitService
import com.wansenai.utils.SnowflakeIdUtil
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.ProdcutCodeEnum
import com.wansenai.utils.response.Response
import com.wansenai.vo.product.ProductUnitVO
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
open class ProductUnitServiceImpl(
    private val productUnitMapper: ProductUnitMapper,
    private val baseService: BaseService
) :ServiceImpl<ProductUnitMapper, ProductUnit>(), ProductUnitService {

    override fun productUnitList(productUnitQuery: ProductUnitQueryDTO?): Response<Page<ProductUnitVO>> {
        val page = productUnitQuery?.run { Page<ProductUnit>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<ProductUnit>().apply {
            productUnitQuery?.computeUnit?.let { like(ProductUnit::getComputeUnit, it) }
            eq(ProductUnit::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = page?.run {
            productUnitMapper.selectPage(this, wrapper)
            val listVo = records.map { unit ->
                ProductUnitVO(
                    id = unit.id,
                    computeUnit = unit.computeUnit,
                    basicUnit = unit.basicUnit,
                    otherUnit = unit.otherUnit,
                    otherUnitTwo = unit.otherUnitTwo,
                    otherUnitThree = unit.otherUnitThree,
                    ratio = unit.ratio,
                    ratioTwo = unit.ratioTwo,
                    ratioThree = unit.ratioThree,
                    status = unit.status,
                    createTime = unit.createTime,
                    otherComputeUnit = formatBigDecimal(unit.ratio, unit.otherUnit, unit.basicUnit),
                    otherComputeUnitTwo = formatBigDecimal(unit.ratioTwo, unit.otherUnitTwo, unit.basicUnit),
                    otherComputeUnitThree = formatBigDecimal(unit.ratioThree, unit.otherUnitThree, unit.basicUnit)
                )
            }
            Page<ProductUnitVO>().apply {
                records = listVo
                total = this@run.total
                pages = this@run.pages
                size = this@run.size
            }
        } ?: Page<ProductUnitVO>()

        return Response.responseData(result)
    }

    private fun formatBigDecimal(ratio: BigDecimal?, otherUnit: String?, basicUnit: String?): String? {
        return ratio?.let {
            val scaledValue = it.setScale(3, RoundingMode.HALF_UP)
            val formattedValue = if (scaledValue.stripTrailingZeros().scale() <= 0) {
                scaledValue.toBigInteger().toString()
            } else {
                scaledValue.toString()
            }
            "$otherUnit=$formattedValue$basicUnit"
        }
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
        val creator = baseService.currentUserId
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
            // 如果id为空，说明是新增，需要设置创建时间
            if (unit.id == null) {
                createTime = LocalDateTime.now()
                createBy = creator
            } else {
                updateTime = LocalDateTime.now()
                updateBy = creator
            }
        }
    }

    private fun buildComputeUnit(productUnit: AddOrUpdateProductUnitDTO): String {
        val computeUnit = StringBuilder()
        computeUnit.append("${productUnit.basicUnit}/(${productUnit.otherUnit}=${productUnit.ratio}${productUnit.basicUnit})")
        productUnit.otherUnitTwo?.let { computeUnit.append("/(${it}=${productUnit.ratioTwo}${productUnit.basicUnit})") }
        productUnit.otherUnitThree?.let { computeUnit.append("/(${it}=${productUnit.ratioThree}${productUnit.basicUnit})") }
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

    override fun updateUnitStatus(productUnitStatus: ProductUnitStatusDTO?): Response<String> {
        return productUnitStatus?.let { item ->
            val unit = ProductUnit().apply {
                id = item.id
                status = item.status
            }
            val updateResult = productUnitMapper.updateById(unit)
            if (updateResult == 0) {
                Response.responseMsg(ProdcutCodeEnum.UPDATE_PRODUCT_UNIT_STATUS_ERROR)
            } else {
                Response.responseMsg(ProdcutCodeEnum.UPDATE_PRODUCT_UNIT_STATUS_SUCCESS)
            }
        } ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }
}