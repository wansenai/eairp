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
package com.wansensoft.dto.basic

data class AddSupplierDTO (
    /**
     * 供应商名称
     */
    val supplierName: String? = null,
    /**
     * 联系人
     */
    val contact: String? = null,
    /**
     * 联系电话
     */
    val contactNumber: String? = null,
    /**
     * 手机号码
     */
    val phoneNumber: String? = null,
    /**
     * 地址
     */
    val address: String? = null,
    /**
     * 邮箱
     */
    val email: String? = null,
    /**
     * 状态
     */
    val status: Int? = null,
    /**
     * 第一季度应付账款
     */
    val firstQuarterAccountPayment: Double? = null,
    /**
     * 第二季度应付账款
     */
    val secondQuarterAccountPayment: Double? = null,
    /**
     * 第三季度应付账款
     */
    val thirdQuarterAccountPayment: Double? = null,
    /**
     * 第四季度应付账款
     */
    val fourthQuarterAccountPayment: Double? = null,
    /**
     * 传真
     */
    val fax: String? = null,
    /**
     * 税号
     */
    val taxNumber: String? = null,
    /**
     * 开户行
     */
    val bankName: String? = null,
    /**
     * 账号
     */
    val accountNumber: String? = null,
    /**
     * 税率
     */
    val taxRate: String? = null,
    /**
     * 排序
     */
    val sort: Int? = null,
    /**
     * 备注
     */
    val remark: String? = null,
)
