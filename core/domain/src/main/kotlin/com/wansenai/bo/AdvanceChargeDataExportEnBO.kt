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

@NoArg
@Data
data class AdvanceChargeDataExportEnBO(

    @ExcelExport(value = "Member")
    val memberName: String,

    @ExcelExport(value = "Receipt Number")
    val receiptNumber: String,

    @ExcelExport(value = "Account")
    val accountName: String,

    @ExcelExport(value = "Amount")
    val amount : BigDecimal,

    @ExcelExport(value = "Remark")
    val remark : String? = null,
)