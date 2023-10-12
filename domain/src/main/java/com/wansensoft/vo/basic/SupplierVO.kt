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
package com.wansensoft.vo.basic

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansensoft.bo.BigDecimalSerializerBO
import java.math.BigDecimal
import java.time.LocalDateTime

data class SupplierVO(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: Long,
    val supplierName: String,
    val contact: String?,
    val contactNumber: String?,
    val phoneNumber: String?,
    val address: String?,
    val email: String?,
    val status: Int,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val firstQuarterAccountReceivable: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val secondQuarterAccountReceivable: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val thirdQuarterAccountReceivable: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val fourthQuarterAccountReceivable: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val totalAccountReceivable: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val firstQuarterAccountPayment: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val secondQuarterAccountPayment: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val thirdQuarterAccountPayment: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val fourthQuarterAccountPayment: BigDecimal?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val totalAccountPayment: BigDecimal?,
    val fax: String?,
    val taxNumber: String?,
    val bankName: String?,
    val accountNumber: Long?,
    @JsonSerialize(using = BigDecimalSerializerBO::class)
    val taxRate: BigDecimal?,
    val sort: Int?,
    val remark: String?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    val createTime: LocalDateTime?,
)
