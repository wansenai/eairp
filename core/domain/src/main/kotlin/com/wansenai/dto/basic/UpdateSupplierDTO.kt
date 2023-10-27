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
package com.wansenai.dto.basic

import java.math.BigDecimal

data class UpdateSupplierDTO(
    /**
     * 供应商ID
     */
    val id: Long,
    /**
     * 供应商名称
     */
    val supplierName: String,
    /**
     * 联系人
     */
    val contact: String,
    /**
     * 联系电话
     */
    val contactNumber: String?,
    /**
     * 手机号码
     */
    val phoneNumber: String,
    /**
     * 地址
     */
    val address: String?,
    /**
     * 邮箱
     */
    val email: String?,
    /**
     * 状态
     */
    val status: Int?,
    /**
     * 第一季度应付账款
     */
    val firstQuarterAccountPayment: Double?,
    /**
     * 第二季度应付账款
     */
    val secondQuarterAccountPayment: Double?,
    /**
     * 第三季度应付账款
     */
    val thirdQuarterAccountPayment: Double?,
    /**
     * 第四季度应付账款
     */
    val fourthQuarterAccountPayment: Double?,
    /**
     * 应付账款总额
     */
    val totalAccountPayment: BigDecimal?,
    /**
     * 传真
     */
    val fax: String?,
    /**
     * 税号
     */
    val taxNumber: String?,
    /**
     * 开户行
     */
    val bankName: String?,
    /**
     * 账号
     */
    val accountNumber: Long?,
    /**
     * 税率
     */
    val taxRate: Int?,
    /**
     * 排序
     */
    val sort: Int?,
    /**
     * 备注
     */
    val remark: String?,
)
