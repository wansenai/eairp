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
package com.wansensoft.service.basic.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.basic.AddSupplierDTO
import com.wansensoft.dto.basic.QuerySupplierDTO
import com.wansensoft.dto.basic.UpdateSupplierDTO
import com.wansensoft.entities.basic.Supplier
import com.wansensoft.mappers.SystemSupplierMapper
import com.wansensoft.service.BaseService
import com.wansensoft.service.basic.SupplierService
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.SupplierCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.basic.SupplierVO
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
open class SupplierServiceImpl(
    private val baseService: BaseService,
    private val supplierMapper: SystemSupplierMapper
) : ServiceImpl<SystemSupplierMapper, Supplier>(), SupplierService {

    override fun getSupplierList(supplier: QuerySupplierDTO?): Response<Page<SupplierVO>> {
        val page = supplier?.run { Page<Supplier>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<Supplier>().apply {
            supplier?.supplierName?.let { like(Supplier::getSupplierName, it) }
            supplier?.contact?.let { like(Supplier::getContact, it) }
            supplier?.contactNumber?.let { like(Supplier::getContactNumber, it) }
            supplier?.phoneNumber?.let { like(Supplier::getPhoneNumber, it) }
            eq(Supplier::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = page?.run {
            supplierMapper.selectPage(this, wrapper)
            val listVo = records.map { supplier ->
                SupplierVO(
                    id = supplier.id,
                    supplierName = supplier.supplierName,
                    contact = supplier.contact,
                    contactNumber = supplier.contactNumber,
                    phoneNumber = supplier.phoneNumber,
                    address = supplier.address,
                    email = supplier.email,
                    status = supplier.status,
                    firstQuarterAccountReceivable = supplier.firstQuarterAccountReceivable,
                    secondQuarterAccountReceivable = supplier.secondQuarterAccountReceivable,
                    thirdQuarterAccountReceivable = supplier.thirdQuarterAccountReceivable,
                    fourthQuarterAccountReceivable = supplier.fourthQuarterAccountReceivable,
                    totalAccountReceivable = supplier.totalAccountReceivable,
                    firstQuarterAccountPayment = supplier.firstQuarterAccountPayment,
                    secondQuarterAccountPayment = supplier.secondQuarterAccountPayment,
                    thirdQuarterAccountPayment = supplier.thirdQuarterAccountPayment,
                    fourthQuarterAccountPayment = supplier.fourthQuarterAccountPayment,
                    totalAccountPayment = supplier.totalAccountPayment,
                    fax = supplier.fax,
                    taxNumber = supplier.taxNumber,
                    bankName = supplier.bankName,
                    accountNumber = supplier.accountNumber,
                    taxRate = supplier.taxRate,
                    sort = supplier.sort,
                    remark = supplier.remark,
                    createTime = supplier.createTime
                )
            }
            Page<SupplierVO>().apply {
                records = listVo
                total = this@run.total
                pages = this@run.pages
                size = this@run.size
            }
        }
        return result?.let { Response.responseData(it) } ?: Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    override fun addSupplier(supplier: AddSupplierDTO?): Response<String> {
        val supplierEntity = Supplier()
        if (supplier != null) {
            BeanUtils.copyProperties(supplier, supplierEntity)
        }

        val totalAccountPayment = (supplierEntity.firstQuarterAccountPayment ?: BigDecimal.ZERO)
            .add(supplierEntity.secondQuarterAccountPayment ?: BigDecimal.ZERO)
            .add(supplierEntity.thirdQuarterAccountPayment ?: BigDecimal.ZERO)
            .add(supplierEntity.fourthQuarterAccountPayment ?: BigDecimal.ZERO)
            .setScale(3, RoundingMode.HALF_UP) // 保留3位小数，四舍五入
        supplierEntity.totalAccountPayment = totalAccountPayment
        supplierEntity.createTime = LocalDateTime.now()
        supplierEntity.createBy = baseService.currentUserId

        val saveResult = save(supplierEntity)
        if (!saveResult) {
            return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_ERROR)
        }
        return Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_SUCCESS)
    }

    override fun updateSupplier(supplier: UpdateSupplierDTO?): Response<String> {
        val supplierEntity = Supplier()
        if (supplier != null) {
            BeanUtils.copyProperties(supplier, supplierEntity)
        }
        supplierEntity.updateTime = LocalDateTime.now()
        supplierEntity.updateBy = baseService.currentUserId

        val updateResult = updateById(supplierEntity)
        if (!updateResult) {
            return Response.responseMsg(SupplierCodeEnum.UPDATE_SUPPLIER_ERROR)
        }
        return Response.responseMsg(SupplierCodeEnum.UPDATE_SUPPLIER_SUCCESS)
    }

    override fun deleteSupplier(ids: List<Long>?): Response<String> {
        ids?.let {
            val deleteResult = supplierMapper.deleteBatchIds(ids)
            if (deleteResult == 0) {
                return Response.responseMsg(SupplierCodeEnum.DELETE_SUPPLIER_ERROR)
            }
            return Response.responseMsg(SupplierCodeEnum.DELETE_SUPPLIER_SUCCESS)
        } ?: return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }
}