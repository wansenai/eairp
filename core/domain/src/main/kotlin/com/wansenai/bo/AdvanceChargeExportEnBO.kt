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
data class AdvanceChargeExportEnBO (

    var id: Long? = null,

    @ExcelExport(value = "Member")
    var memberName: String,

    @ExcelExport(value = "Receipt Number")
    var receiptNumber: String,

    @ExcelExport(value = "Receipt Date")
    var receiptDate: LocalDateTime,

    @ExcelExport(value = "Collected Amount")
    var collectedAmount : BigDecimal,

    @ExcelExport(value = "Total Amount")
    var totalAmount : BigDecimal,

    @ExcelExport(value = "Financial Personnel")
    var financialPersonnel: String,

    @ExcelExport(value = "Operator")
    var operator: String,

    @ExcelExport(value = "Remark")
    var remark: String? = null,

    @ExcelExport(value = "Status", kv = "0-Unaudited;1-Audited;")
    var status: Int,
)