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
import com.wansensoft.NoArg
import com.wansensoft.bo.BigDecimalSerializerBO
import com.wansensoft.utils.excel.ExcelExport
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.Setter

import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
data class SupplierVO (

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id: Long,

    @ExcelExport(value = "供应商名称*", sort = 1)
    var supplierName: String,

    @ExcelExport(value = "联系人*", sort = 2)
    var contact: String?,

    @ExcelExport(value = "联系电话", sort = 4)
    var contactNumber: String?,

    @ExcelExport(value = "手机号码*", sort = 3)
    var phoneNumber: String?,

    @ExcelExport(value = "", sort = 22)
    var address: String?,

    @ExcelExport(value = "电子邮箱", sort = 5)
    var email: String?,

    var status: Int,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "一季度收款", sort = 7)
    var firstQuarterAccountReceivable: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "二季度收款", sort = 8)
    var secondQuarterAccountReceivable: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "三季度收款", sort = 9)
    var thirdQuarterAccountReceivable: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "四季度收款", sort = 10)
    var fourthQuarterAccountReceivable: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "累计应收账款", sort = 11)
    var totalAccountReceivable: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "一季度付款", sort = 12)
    var firstQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "二季度付款", sort = 13)
    var secondQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "三季度付款", sort = 14)
    var thirdQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "四季度付款", sort = 15)
    var fourthQuarterAccountPayment: BigDecimal?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "累计应付账款", sort = 16)
    var totalAccountPayment: BigDecimal?,

    @ExcelExport(value = "传真", sort = 6)
    var fax: String?,

    @ExcelExport(value = "纳税人识别号", sort = 17)
    var taxNumber: String?,

    @ExcelExport(value = "开户行", sort = 19)
    var bankName: String?,

    @ExcelExport(value = "账号", sort = 20)
    var accountNumber: Long?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    @ExcelExport(value = "税率(%)", sort = 18)
    var taxRate: BigDecimal?,

    @ExcelExport(value = "排序", sort = 21)
    var sort: Int?,

    @ExcelExport(value = "备注", sort = 23)
    var remark: String?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime?,
)