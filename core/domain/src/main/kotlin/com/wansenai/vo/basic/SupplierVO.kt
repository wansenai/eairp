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
package com.wansenai.vo.basic

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansenai.NoArg
import com.wansenai.bo.BigDecimalSerializerBO

import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
data class SupplierVO (

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id: Long,

    var supplierName: String,

    var contact: String?,

    var contactNumber: String?,

    var phoneNumber: String?,

    var address: String?,

    var email: String?,

    var status: Int,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var firstQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var secondQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var thirdQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var fourthQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var totalAccountPayment: BigDecimal?,

    var fax: String?,

    var taxNumber: String?,

    var bankName: String?,

    var accountNumber: String?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var taxRate: BigDecimal?,

    var sort: Int?,

    var remark: String?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime?,
)