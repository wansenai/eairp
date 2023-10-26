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
package com.wansensoft.dto.financial

import lombok.Data

@Data
data class QueryAdvanceChargeDTO (

    val receiptNumber: String? = null,

    val memberId: Long? = null,

    val operatorId: Long? = null,

    val financialPersonnelId: Long? = null,

    val status : Int? = null,

    val remark : String? = null,

    val page: Long?,

    val pageSize: Long?,

    val startDate: String?,

    val endDate: String?,
)
