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
data class CustomerExportBO(

    var id: Long?,

    @ExcelExport(value = "客户", sort = 1)
    var customerName: String?,

    @ExcelExport(value = "联系人", sort = 2)
    var contact: String?,

    @ExcelExport(value = "手机号码", sort = 3)
    var phoneNumber: String?,

    @ExcelExport(value = "电子邮箱", sort = 4)
    var email: String?,

    @ExcelExport(value = "传真", sort = 5)
    var fax: String?,

    @ExcelExport(value = "一季度收款", sort = 8)
    var firstQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "二季度收款", sort = 9)
    var secondQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "三季度收款", sort = 10)
    var thirdQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "四季度收款", sort = 11)
    var fourthQuarterAccountReceivable: BigDecimal?,

    @ExcelExport(value = "累计应收账款", sort = 12)
    var totalAccountReceivable: BigDecimal?,

    @ExcelExport(value = "地址", sort = 6)
    var address: String?,

    @ExcelExport(value = "纳税人识别号", sort = 13)
    var taxNumber: String?,

    @ExcelExport(value = "开户行", sort = 15)
    var bankName: String?,

    @ExcelExport(value = "银行账户", sort = 16)
    var accountNumber: String?,

    @ExcelExport(value = "税率(%)", sort = 14)
    var taxRate: BigDecimal?,

    @ExcelExport(value = "状态", kv="0-启用;1-停用", sort = 7)
    var status: Int?,

    @ExcelExport(value = "备注", sort = 17)
    var remark: String?,

    var sort: Int?,

    var createTime: LocalDateTime?,
)
