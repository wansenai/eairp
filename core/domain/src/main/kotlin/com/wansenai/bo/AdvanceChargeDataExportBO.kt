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
package com.wansenai.bo

import com.wansenai.NoArg
import com.wansenai.utils.excel.ExcelExport
import lombok.Data
import java.math.BigDecimal

@NoArg
@Data
data class AdvanceChargeDataExportBO(

    @ExcelExport(value = "会员")
    val memberName: String,

    @ExcelExport(value = "预付款单据编号")
    val receiptNumber: String,

    @ExcelExport(value = "账户名称")
    val accountName: String,

    @ExcelExport(value = "金额")
    val amount : BigDecimal,

    @ExcelExport(value = "备注")
    val remark : String? = null,
)