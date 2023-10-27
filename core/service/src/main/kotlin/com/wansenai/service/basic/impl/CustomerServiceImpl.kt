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
import com.wansenai.entities.basic.Customer
import com.wansenai.dto.basic.AddOrUpdateCustomerDTO
import com.wansenai.dto.basic.QueryCustomerDTO
import com.wansenai.service.basic.CustomerService
import com.wansenai.mappers.basic.CustomerMapper
import com.wansenai.utils.SnowflakeIdUtil
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.CustomerCodeEnum
import com.wansenai.utils.response.Response
import com.wansenai.vo.basic.CustomerVO
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Slf4j
@Service
open class CustomerServiceImpl(
    private val baseService: com.wansenai.service.BaseService,
    private val customerMapper: CustomerMapper
) : ServiceImpl<CustomerMapper, Customer>(), CustomerService {

    override fun getCustomerList(queryCustomerDTO: QueryCustomerDTO?): Response<Page<CustomerVO>> {
        val page = queryCustomerDTO?.run { Page<Customer>(page ?: 1, pageSize ?: 10) }
        val wrapper = LambdaQueryWrapper<Customer>().apply {
            queryCustomerDTO?.customerName?.let { like(Customer::getCustomerName, it) }
            queryCustomerDTO?.contact?.let { like(Customer::getContact, it) }
            queryCustomerDTO?.phoneNumber?.let { like(Customer::getPhoneNumber, it) }
            queryCustomerDTO?.startDate?.let { ge(Customer::getCreateTime, it) }
            queryCustomerDTO?.endDate?.let { le(Customer::getCreateTime, it) }
            eq(Customer::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = page?.run {
            customerMapper.selectPage(this, wrapper)
            val listVo = records.map { customer ->
                CustomerVO(
                    id = customer.id,
                    customerName = customer.customerName,
                    contact = customer.contact,
                    phoneNumber = customer.phoneNumber,
                    email = customer.email,
                    firstQuarterAccountReceivable = customer.firstQuarterAccountReceivable,
                    secondQuarterAccountReceivable = customer.secondQuarterAccountReceivable,
                    thirdQuarterAccountReceivable = customer.thirdQuarterAccountReceivable,
                    fourthQuarterAccountReceivable = customer.fourthQuarterAccountReceivable,
                    totalAccountReceivable = customer.totalAccountReceivable,
                    address = customer.address,
                    taxNumber = customer.taxNumber,
                    bankName = customer.bankName,
                    accountNumber = customer.accountNumber,
                    taxRate = customer.taxRate,
                    status = customer.status,
                    remark = customer.remark,
                    sort = customer.sort,
                    createTime = customer.createTime,
                )
            }
            Page<CustomerVO>().apply {
                records = listVo
                total = this@run.total
                pages = this@run.pages
                size = this@run.size
            }
        }
        return result.let { Response.responseData(it) } ?: Response.responseMsg(
            BaseCodeEnum.QUERY_DATA_EMPTY)
    }

    open fun calculateTotalAccount(list: List<BigDecimal?>): BigDecimal {
        return list.mapNotNull { it }.sumOf { it }.setScale(3, RoundingMode.HALF_UP)
    }

    @Transactional
    override fun addOrUpdateCustomer(addOrUpdateCustomerDTO: AddOrUpdateCustomerDTO): Response<String> {
        val userId = baseService.getCurrentUserId()
        val isAdd = addOrUpdateCustomerDTO.id == null

        val customer = Customer().apply {
            id = if (isAdd) SnowflakeIdUtil.nextId() else addOrUpdateCustomerDTO.id
            customerName = addOrUpdateCustomerDTO.customerName.orEmpty()
            contact = addOrUpdateCustomerDTO.contact.orEmpty()
            phoneNumber = addOrUpdateCustomerDTO.phoneNumber.orEmpty()
            email = addOrUpdateCustomerDTO.email.orEmpty()
            firstQuarterAccountReceivable = addOrUpdateCustomerDTO.firstQuarterAccountReceivable ?: BigDecimal.ZERO
            secondQuarterAccountReceivable = addOrUpdateCustomerDTO.secondQuarterAccountReceivable ?: BigDecimal.ZERO
            thirdQuarterAccountReceivable = addOrUpdateCustomerDTO.thirdQuarterAccountReceivable ?: BigDecimal.ZERO
            fourthQuarterAccountReceivable = addOrUpdateCustomerDTO.fourthQuarterAccountReceivable ?: BigDecimal.ZERO
            totalAccountReceivable = calculateTotalAccount(
                listOf(
                    addOrUpdateCustomerDTO.firstQuarterAccountReceivable,
                    addOrUpdateCustomerDTO.secondQuarterAccountReceivable,
                    addOrUpdateCustomerDTO.thirdQuarterAccountReceivable,
                    addOrUpdateCustomerDTO.fourthQuarterAccountReceivable
                )
            )
            address = addOrUpdateCustomerDTO.address.orEmpty()
            taxNumber = addOrUpdateCustomerDTO.taxNumber.orEmpty()
            bankName = addOrUpdateCustomerDTO.bankName.orEmpty()
            accountNumber = addOrUpdateCustomerDTO.accountNumber.orEmpty()
            taxRate = addOrUpdateCustomerDTO.taxRate ?: BigDecimal.ZERO
            status = addOrUpdateCustomerDTO.status ?: 0
            remark = addOrUpdateCustomerDTO.remark.orEmpty()
            sort = addOrUpdateCustomerDTO.sort ?: 0
            if (isAdd) {
                createTime = LocalDateTime.now()
                createBy = userId
            } else {
                updateTime = LocalDateTime.now()
                updateBy = userId
            }
        }

        val saveResult = saveOrUpdate(customer)
        return when {
            saveResult && isAdd -> Response.responseMsg(CustomerCodeEnum.ADD_CUSTOMER_SUCCESS)
            saveResult && !isAdd -> Response.responseMsg(CustomerCodeEnum.UPDATE_CUSTOMER_SUCCESS)
            else -> Response.fail()
        }
    }

    override fun deleteCustomer(ids: List<Long>?): Response<String> {
        return ids.takeIf { !it.isNullOrEmpty() }
            ?.let {
                val updateResult = customerMapper.deleteBatchIds(it)
                if (updateResult > 0) {
                    Response.responseMsg(CustomerCodeEnum.DELETE_CUSTOMER_SUCCESS)
                } else {
                    Response.responseMsg(CustomerCodeEnum.DELETE_CUSTOMER_ERROR)
                }
            }
            ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun updateCustomerStatus(ids: List<Long>?, status: Int?): Response<String> {
        return ids.takeIf { !it.isNullOrEmpty() }?.let {
            status?.let { s ->
                val updateResult = lambdaUpdate()
                    .`in`(Customer::getId, it)
                    .set(Customer::getStatus, s)
                    .update()

                if (!updateResult) {
                    Response.responseMsg(CustomerCodeEnum.UPDATE_CUSTOMER_STATUS_ERROR)
                } else {
                    Response.responseMsg(CustomerCodeEnum.UPDATE_CUSTOMER_STATUS_SUCCESS)
                }
            }
        } ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    @Transactional
    override fun batchAddCustomer(customers: List<Customer>?): Boolean {
        val customerEntities = mutableListOf<Customer>()
        val existingCustomers = HashSet<Pair<String, String>>() // 存储已存在的供应商名称和联系人的组合

        customers?.forEach { customer ->
            val customerKey = Pair(customer.customerName, customer.contact)
            if (!existingCustomers.contains(customerKey)) {
                val customerEntity = customerMapper.selectOne(
                    LambdaQueryWrapper<Customer>()
                        .eq(Customer::getCustomerName, customer.customerName)
                        .eq(Customer::getContact, customer.contact)
                )
                if (customerEntity == null) {
                    val newCustomerEntity = Customer().apply {
                        BeanUtils.copyProperties(customer, this)
                        firstQuarterAccountReceivable = customer.firstQuarterAccountReceivable
                        secondQuarterAccountReceivable = customer.secondQuarterAccountReceivable
                        thirdQuarterAccountReceivable = customer.thirdQuarterAccountReceivable
                        fourthQuarterAccountReceivable = customer.fourthQuarterAccountReceivable
                        taxRate = customer.taxRate
                        totalAccountReceivable = calculateTotalAccount(
                            listOf(
                                customer.firstQuarterAccountReceivable,
                                customer.secondQuarterAccountReceivable,
                                customer.thirdQuarterAccountReceivable,
                                customer.fourthQuarterAccountReceivable
                            )
                        )
                        createTime = LocalDateTime.now()
                        createBy = baseService.currentUserId
                    }
                    customerEntities.add(newCustomerEntity)
                    existingCustomers.add(customerKey)
                }
            }
        }
        return saveBatch(customerEntities)
    }
}