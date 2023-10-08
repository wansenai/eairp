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
import com.wansensoft.dto.product.AddOrUpdateProductAttributeDTO
import com.wansensoft.dto.product.ProductAttributeQueryDTO
import com.wansensoft.entities.product.ProductAttribute
import com.wansensoft.mappers.product.ProductAttributeMapper
import com.wansensoft.service.product.ProductAttributeService
import com.wansensoft.service.user.ISysUserService
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.ProdcutCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.product.ProductAttributeVO
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class ProductAttributeServiceImpl(
    private val productAttributeMapper: ProductAttributeMapper,
    private val userService: ISysUserService
):ServiceImpl<ProductAttributeMapper, ProductAttribute>(), ProductAttributeService {

    override fun productAttributeList(productAttributeQuery : ProductAttributeQueryDTO?): Response<Page<ProductAttributeVO>> {
        val page = productAttributeQuery?.let { Page<ProductAttribute>(it.page, it.pageSize) }
        val wrapper = LambdaQueryWrapper<ProductAttribute>().apply() {
            productAttributeQuery?.attributeName?.let { like(ProductAttribute::getAttributeName, it) }
            eq(ProductAttribute::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = page?.run {
            productAttributeMapper.selectPage(this, wrapper)
            if (records.isNotEmpty()) {
                val listVo = records.map { attribute ->
                    ProductAttributeVO().apply {
                        BeanUtils.copyProperties(attribute, this)
                    }
                }
                Page<ProductAttributeVO>().apply {
                    records = listVo
                    total = this@run.total
                    pages = this@run.pages
                    size = this@run.size
                }

            } else {
                Page<ProductAttributeVO>()
            }
        }?: Page<ProductAttributeVO>()

        return Response.responseData(result)
    }

    override fun addOrUpdateProductAttribute(productAttributeAddOrUpdate: AddOrUpdateProductAttributeDTO?): Response<String> {
        productAttributeAddOrUpdate?.run {
            val attribute = ProductAttribute().apply {
                BeanUtils.copyProperties(this@run, this)
            }
            val userId = userService.currentUserId
            when (attribute.id) {
                null -> {
                    val wrapper = createWrapper(attribute)
                    val count = getCount(wrapper)
                    if (count > 0) {
                        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ATTRIBUTE_NAME_EXIST)
                    }
                    attribute.createTime = LocalDateTime.now()
                    attribute.createBy = userId
                    val saveResult = saveAttribute(attribute)
                    if (saveResult == 0) {
                        return Response.responseMsg(ProdcutCodeEnum.ADD_PRODUCT_ATTRIBUTE_ERROR)
                    }
                    return Response.responseMsg(ProdcutCodeEnum.ADD_PRODUCT_ATTRIBUTE_SUCCESS)
                }
                else -> {
                    val wrapper = createWrapper(attribute).ne(ProductAttribute::getId, attribute.id)
                    val count = getCount(wrapper)
                    if (count > 0) {
                        return Response.responseMsg(ProdcutCodeEnum.PRODUCT_ATTRIBUTE_NAME_EXIST)
                    }
                    attribute.updateBy = userId
                    attribute.updateTime = LocalDateTime.now()
                    val updateResult = updateAttribute(attribute)
                    if (updateResult == 0) {
                        return Response.responseMsg(ProdcutCodeEnum.UPDATE_PRODUCT_ATTRIBUTE_ERROR)
                    }
                    return Response.responseMsg(ProdcutCodeEnum.UPDATE_PRODUCT_ATTRIBUTE_SUCCESS)
                }
            }
        } ?: return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    private fun createWrapper(attribute: ProductAttribute) = LambdaQueryWrapper<ProductAttribute>().apply {
        eq(ProductAttribute::getAttributeName, attribute.attributeName)
        eq(ProductAttribute::getDeleteFlag, CommonConstants.NOT_DELETED)
    }

    private fun getCount(wrapper: LambdaQueryWrapper<ProductAttribute>) = productAttributeMapper.selectCount(wrapper)

    private fun saveAttribute(attribute: ProductAttribute) = productAttributeMapper.insert(attribute)

    private fun updateAttribute(attribute: ProductAttribute) = productAttributeMapper.updateById(attribute)

    override fun batchDeleteProductAttribute(ids: List<Long>?): Response<String> {
        // Change the status from unmodified to physically deleted data
        ids?.let {
            val deleteResult = productAttributeMapper.deleteBatchIds(ids)
            if(deleteResult == 0) {
                return Response.responseMsg(ProdcutCodeEnum.DELETE_PRODUCT_ATTRIBUTE_ERROR)
            }
            return Response.responseMsg(ProdcutCodeEnum.DELETE_PRODUCT_ATTRIBUTE_SUCCESS)
        }?: return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }
}