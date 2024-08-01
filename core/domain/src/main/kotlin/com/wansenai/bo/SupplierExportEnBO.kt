/*
 * Copyright 2024-2033 WanSen AI Team, Inc. All Rights Reserved.
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
package com.wansenai.bo

import com.wansenai.NoArg
import com.wansenai.utils.excel.ExcelExport
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
@Data
data class SupplierExportEnBO (
    var id: Long,

    @ExcelExport(value = "Supplier*", sort = 1)
    var supplierName: String,

    @ExcelExport(value = "Contact*", sort = 2)
    var contact: String?,

    @ExcelExport(value = "Contact Phone", sort = 4)
    var contactNumber: String?,

    @ExcelExport(value = "Phone Number*", sort = 3)
    var phoneNumber: String?,

    @ExcelExport(value = "Address", sort = 22)
    var address: String?,

    @ExcelExport(value = "Email", sort = 5)
    var email: String?,

    @ExcelExport(value = "Status", kv="0-Enable;1-Deactivate", sort = 7)
    var status: Int,

    @ExcelExport(value = "First Quarter Payment", sort = 12)
    var firstQuarterAccountPayment: BigDecimal?,

    @ExcelExport(value = "Second Quarter Payment", sort = 13)
    var secondQuarterAccountPayment: BigDecimal?,

    @ExcelExport(value = "Third Quarter Payment", sort = 14)
    var thirdQuarterAccountPayment: BigDecimal?,

    @ExcelExport(value = "Fourth Quarter Payment", sort = 15)
    var fourthQuarterAccountPayment: BigDecimal?,

    @ExcelExport(value = "Total Payment", sort = 16)
    var totalAccountPayment: BigDecimal?,

    @ExcelExport(value = "Fax", sort = 6)
    var fax: String?,

    @ExcelExport(value = "Tax Number", sort = 17)
    var taxNumber: String?,

    @ExcelExport(value = "Bank", sort = 19)
    var bankName: String?,

    @ExcelExport(value = "Bank Account Number", sort = 20)
    var accountNumber: String?,

    @ExcelExport(value = "Tax Rate(%)", sort = 18)
    var taxRate: BigDecimal?,

    var sort: Int?,

    @ExcelExport(value = "Remark", sort = 23)
    var remark: String?,

    var createTime: LocalDateTime?,
)