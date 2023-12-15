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
package com.wansenai.service.basic.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.entities.basic.Supplier
import com.wansenai.dto.basic.AddSupplierDTO
import com.wansenai.dto.basic.QuerySupplierDTO
import com.wansenai.dto.basic.UpdateSupplierDTO
import com.wansenai.dto.basic.UpdateSupplierStatusDTO
import com.wansenai.mappers.basic.SystemSupplierMapper
import com.wansenai.service.basic.SupplierService
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.SupplierCodeEnum
import com.wansenai.utils.excel.ExcelUtils
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.SupplierVO
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
open class SupplierServiceImpl(
    private val baseService: com.wansenai.service.BaseService,
    private val supplierMapper: SystemSupplierMapper
) : ServiceImpl<SystemSupplierMapper, Supplier>(), SupplierService {

    override fun getSupplierPageList(supplier: QuerySupplierDTO?): Response<Page<SupplierVO>> {
        val page = supplier?.run { Page<Supplier>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<Supplier>().apply {
            supplier?.supplierName?.let { like(Supplier::getSupplierName, it) }
            supplier?.contact?.let { like(Supplier::getContact, it) }
            supplier?.contactNumber?.let { like(Supplier::getContactNumber, it) }
            supplier?.phoneNumber?.let { like(Supplier::getPhoneNumber, it) }
            supplier?.startDate?.let { ge(Supplier::getCreateTime, it) }
            supplier?.endDate?.let { le(Supplier::getCreateTime, it) }
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
        return result?.let { Response.responseData(it) } ?: Response.responseMsg(
            BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    open fun calculateTotalAccount(list: List<BigDecimal?>): BigDecimal {
        return list.mapNotNull { it }.sumOf { it }.setScale(3, RoundingMode.HALF_UP)
    }

    override fun addSupplier(supplier: AddSupplierDTO?): Response<String> {
        val supplierEntity = supplier?.let {
            Supplier().apply {
                BeanUtils.copyProperties(it, this)
                firstQuarterAccountPayment = it.firstQuarterAccountPayment?.toBigDecimal()
                secondQuarterAccountPayment = it.secondQuarterAccountPayment?.toBigDecimal()
                thirdQuarterAccountPayment = it.thirdQuarterAccountPayment?.toBigDecimal()
                fourthQuarterAccountPayment = it.fourthQuarterAccountPayment?.toBigDecimal()
                taxRate = it.taxRate?.toBigDecimal()
            }
        } ?: Supplier()

        val totalAccountPayment = calculateTotalAccount(
            listOf(
                supplierEntity.firstQuarterAccountPayment,
                supplierEntity.secondQuarterAccountPayment,
                supplierEntity.thirdQuarterAccountPayment,
                supplierEntity.fourthQuarterAccountPayment
            )
        )

        supplierEntity.totalAccountPayment = totalAccountPayment
        supplierEntity.createTime = LocalDateTime.now()
        supplierEntity.createBy = baseService.currentUserId

        return if (save(supplierEntity)) {
            Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_SUCCESS)
        } else {
            Response.responseMsg(SupplierCodeEnum.ADD_SUPPLIER_ERROR)
        }
    }

    override fun getSupplierList(supplier: QuerySupplierDTO?): Response<List<SupplierVO>> {
        val wrapper = LambdaQueryWrapper<Supplier>().apply {
            supplier?.supplierName?.let { like(Supplier::getSupplierName, it) }
            supplier?.contact?.let { like(Supplier::getContact, it) }
            supplier?.contactNumber?.let { like(Supplier::getContactNumber, it) }
            supplier?.phoneNumber?.let { like(Supplier::getPhoneNumber, it) }
            supplier?.startDate?.let { ge(Supplier::getCreateTime, it) }
            supplier?.endDate?.let { le(Supplier::getCreateTime, it) }
            eq(Supplier::getStatus, CommonConstants.STATUS_NORMAL)
            eq(Supplier::getDeleteFlag, CommonConstants.NOT_DELETED)
            orderByAsc(Supplier::getSort)
        }
        val list = supplierMapper.selectList(wrapper)
        val listVo = list.map { supplier ->
            SupplierVO(
                id = supplier.id,
                supplierName = supplier.supplierName,
                contact = supplier.contact,
                contactNumber = supplier.contactNumber,
                phoneNumber = supplier.phoneNumber,
                address = supplier.address,
                email = supplier.email,
                status = supplier.status,
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
        return Response.responseData(listVo)
    }

    @Transactional
    override fun batchAddSupplier(suppliers: List<Supplier>?): Boolean {
        val supplierEntities = mutableListOf<Supplier>()
        val existingSuppliers = HashSet<Pair<String, String>>() // 存储已存在的供应商名称和联系人的组合

        suppliers?.forEach { supplier ->
            val supplierKey = Pair(supplier.supplierName, supplier.contact)
            if (!existingSuppliers.contains(supplierKey)) {
                val supplierEntity = supplierMapper.selectOne(
                    LambdaQueryWrapper<Supplier>()
                        .eq(Supplier::getSupplierName, supplier.supplierName)
                        .eq(Supplier::getContact, supplier.contact)
                )
                if (supplierEntity == null) {
                    val newSupplierEntity = Supplier().apply {
                        BeanUtils.copyProperties(supplier, this)
                        firstQuarterAccountPayment = supplier.firstQuarterAccountPayment
                        secondQuarterAccountPayment = supplier.secondQuarterAccountPayment
                        thirdQuarterAccountPayment = supplier.thirdQuarterAccountPayment
                        fourthQuarterAccountPayment = supplier.fourthQuarterAccountPayment
                        taxRate = supplier.taxRate
                        totalAccountPayment = calculateTotalAccount(
                            listOf(
                                supplier.firstQuarterAccountPayment,
                                supplier.secondQuarterAccountPayment,
                                supplier.thirdQuarterAccountPayment,
                                supplier.fourthQuarterAccountPayment
                            )
                        )
                        createTime = LocalDateTime.now()
                        createBy = baseService.currentUserId
                    }
                    supplierEntities.add(newSupplierEntity)
                    existingSuppliers.add(supplierKey)
                }
            }
        }
        return saveBatch(supplierEntities)
    }


    override fun updateSupplier(supplier: UpdateSupplierDTO?): Response<String> {
        val supplierEntity = supplier?.let {
            Supplier().apply {
                BeanUtils.copyProperties(it, this)
                firstQuarterAccountPayment = it.firstQuarterAccountPayment?.toBigDecimal()
                secondQuarterAccountPayment = it.secondQuarterAccountPayment?.toBigDecimal()
                thirdQuarterAccountPayment = it.thirdQuarterAccountPayment?.toBigDecimal()
                fourthQuarterAccountPayment = it.fourthQuarterAccountPayment?.toBigDecimal()
                taxRate = it.taxRate?.toBigDecimal()
            }
        } ?: Supplier()

        val totalAccountPayment = calculateTotalAccount(
            listOf(
                supplierEntity.firstQuarterAccountPayment,
                supplierEntity.secondQuarterAccountPayment,
                supplierEntity.thirdQuarterAccountPayment,
                supplierEntity.fourthQuarterAccountPayment
            )
        )

        supplierEntity.totalAccountPayment = totalAccountPayment
        supplierEntity.updateTime = LocalDateTime.now()
        supplierEntity.updateBy = baseService.currentUserId

        return if (updateById(supplierEntity)) {
            Response.responseMsg(SupplierCodeEnum.UPDATE_SUPPLIER_SUCCESS)
        } else {
            Response.responseMsg(SupplierCodeEnum.UPDATE_SUPPLIER_ERROR)
        }
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

    override fun updateSupplierStatus(updateSupplierStatusDTO: UpdateSupplierStatusDTO?): Response<String> {
        updateSupplierStatusDTO?.let {
            val supplier = Supplier().apply {
                status = it.status
                updateTime = LocalDateTime.now()
                updateBy = baseService.currentUserId
            }
            val updateResult = supplierMapper.update(supplier, LambdaQueryWrapper<Supplier>().`in`(
                Supplier::getId, it.ids))
            if (updateResult == 0) {
                return Response.responseMsg(SupplierCodeEnum.UPDATE_SUPPLIER_STATUS_ERROR)
            }
            return Response.responseMsg(SupplierCodeEnum.UPDATE_SUPPLIER_STATUS_SUCCESS)
        } ?: return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun exportSupplierData(querySupplierDTO: QuerySupplierDTO, response: HttpServletResponse) {
        val suppliers = getSupplierList(querySupplierDTO)
        if(suppliers.data.isNotEmpty()) {
            val exportData = ArrayList<SupplierVO>()
            suppliers.data.forEach {supplier ->
                val supplierVO =  SupplierVO(
                    id = supplier.id,
                    supplierName = supplier.supplierName,
                    contact = supplier.contact,
                    contactNumber = supplier.contactNumber,
                    phoneNumber = supplier.phoneNumber,
                    address = supplier.address,
                    email = supplier.email,
                    status = supplier.status,
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
                exportData.add(supplierVO)
            }
            ExcelUtils.export(response, "供应商数据", ExcelUtils.getSheetData(exportData))
        }
    }
}