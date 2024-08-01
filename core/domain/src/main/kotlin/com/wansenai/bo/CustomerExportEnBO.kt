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
data class CustomerExportEnBO(

    var id: Long?,

    @ExcelExport(value = "Customer", sort = 1)
    var customerName: String?,

    @ExcelExport(value = "Contact", sort = 2)
    var contact: String?,

    @ExcelExport(value = "Phone Number", sort = 3)
    var phoneNumber: String?,

    @ExcelExport(value = "Email", sort = 4)
    var email: String?,

    @ExcelExport(value = "Fax", sort = 5)
    var fax: String?,

    @ExcelExport(value = "First Quarter Collection", sort = 8)
    var firstQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "Second Quarter Collection", sort = 9)
    var secondQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "Third Quarter Collection", sort = 10)
    var thirdQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "Fourth Quarter Collection", sort = 11)
    var fourthQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "Total Collection", sort = 12)
    var totalAccountReceivable: BigDecimal?,

    @ExcelExport(value = "Address", sort = 6)
    var address: String?,

    @ExcelExport(value = "Tax Number", sort = 13)
    var taxNumber: String?,

    @ExcelExport(value = "Bank", sort = 15)
    var bankName: String?,

    @ExcelExport(value = "Bank Account Number", sort = 16)
    var accountNumber: String?,

    @ExcelExport(value = "Tax Rate(%)", sort = 14)
    var taxRate: BigDecimal?,

    @ExcelExport(value = "Status", kv="0-Enable;1-Deactivate", sort = 7)
    var status: Int?,

    @ExcelExport(value = "Remark", sort = 17)
    var remark: String?,

    var sort: Int?,

    var createTime: LocalDateTime?,
)
