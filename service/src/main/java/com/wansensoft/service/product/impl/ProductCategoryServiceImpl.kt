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

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.product.AddOrUpdateProductCategoryDTO
import com.wansensoft.entities.product.ProductCategory
import com.wansensoft.mappers.product.ProductCategoryMapper
import com.wansensoft.service.product.ProductCategoryService
import com.wansensoft.service.user.ISysUserService
import com.wansensoft.utils.SnowflakeIdUtil
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.ProdcutCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.ProductCategoryVO
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.LocalDateTime

@Service
open class ProductCategoryServiceImpl(
    private val productCategoryMapper: ProductCategoryMapper,
    private val userService: ISysUserService
) : ServiceImpl<ProductCategoryMapper, ProductCategory>(), ProductCategoryService {

    override fun productCategoryList(): Response<List<ProductCategoryVO>> {
        val productCategoryVOs = mutableListOf<ProductCategoryVO>()
        val productCategories = lambdaQuery()
            .eq(ProductCategory::getDeleteFlag, CommonConstants.NOT_DELETED)
            .orderByAsc(ProductCategory::getSort)
            .list()
        productCategories.forEach {
            val productCategoryVO = ProductCategoryVO()
            // 获取item的父级分类名称
            val parentId = it.parentId
            if (parentId != null) {
                val parentCategory = lambdaQuery()
                    .eq(ProductCategory::getId, parentId)
                    .one()
                productCategoryVO.parentName = parentCategory.categoryName
            }
            BeanUtils.copyProperties(it, productCategoryVO)
            productCategoryVOs.add(productCategoryVO)
        }

        return Response.responseData(productCategoryVOs)
    }

    override fun addOrUpdateProductCategory(productCategory: AddOrUpdateProductCategoryDTO): Response<String> {
        productCategory.let { dto ->
            val userId = userService.getCurrentTenantId().toLong()
            if (dto.id == null) {
                // Add product category
                val savaResult = save(
                    ProductCategory.builder()
                        .id(SnowflakeIdUtil.nextId())
                        .tenantId(userId)
                        .categoryName(dto.categoryName)
                        .categoryNumber(dto.categoryNumber)
                        .parentId(dto.parentId)
                        .sort(dto.sort)
                        .remark(dto.remark)
                        .createTime(LocalDateTime.now())
                        .createBy(userId)
                        .build()
                )
                if (!savaResult) {
                    return Response.responseMsg(ProdcutCodeEnum.ADD_PRODUCT_CATEGORY_ERROR)
                }
                return Response.responseMsg(ProdcutCodeEnum.ADD_PRODUCT_CATEGORY_SUCCESS)

            } else {
                val updateResult = lambdaUpdate()
                    .eq(ProductCategory::getId, dto.id)
                    .apply {
                        set(StringUtils.hasText(dto.categoryName), ProductCategory::getCategoryName, dto.categoryName)
                        set(StringUtils.hasText(dto.categoryNumber), ProductCategory::getCategoryNumber, dto.categoryNumber)
                        set(dto.parentId != null, ProductCategory::getParentId, dto.parentId)
                        set(dto.sort != null, ProductCategory::getSort, dto.sort)
                        set(StringUtils.hasText(dto.remark), ProductCategory::getRemark, dto.remark)
                        set(ProductCategory::getUpdateTime, LocalDateTime.now())
                        set(ProductCategory::getUpdateBy, userId)
                    }
                    .update()

                if (!updateResult) {
                    return Response.responseMsg(ProdcutCodeEnum.UPDATE_PRODUCT_CATEGORY_ERROR)
                }
                return Response.responseMsg(ProdcutCodeEnum.UPDATE_PRODUCT_CATEGORY_SUCCESS)
            }
        }
    }

    override fun deleteProductCategory(ids: List<Long>?): Response<String> {
        // 如果id为空返回参数错误 否则进行逻辑删除产品分类id
        if(ids.isNullOrEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }
        val deleteResult = lambdaUpdate()
            .`in`(ProductCategory::getId, ids)
            .set(ProductCategory::getDeleteFlag, CommonConstants.DELETED)
            .update()

        if(!deleteResult){
            return Response.responseMsg(ProdcutCodeEnum.DELETE_PRODUCT_CATEGORY_ERROR)
        }

        return Response.responseMsg(ProdcutCodeEnum.DELETE_PRODUCT_CATEGORY_SUCCESS)
    }
}