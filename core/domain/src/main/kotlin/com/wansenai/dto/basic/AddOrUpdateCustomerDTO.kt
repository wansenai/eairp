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
package com.wansenai.dto.basic

import com.wansenai.NoArg
import java.math.BigDecimal

@NoArg
data class AddOrUpdateCustomerDTO(

    val id: Long?,

    var customerName: String,

    var contact: String?,

    var phoneNumber: String?,

    var email: String?,

    var firstQuarterAccountReceivable: BigDecimal?,

    var secondQuarterAccountReceivable: BigDecimal?,

    var thirdQuarterAccountReceivable: BigDecimal?,

    var fourthQuarterAccountReceivable: BigDecimal?,

    var totalAccountReceivable: BigDecimal?,

    var address: String?,

    var taxNumber: String?,

    var bankName: String?,

    var accountNumber: String?,

    var taxRate: BigDecimal?,

    var status: Int?,

    var remark: String?,

    var sort: Int?,
)
