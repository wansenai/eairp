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
package com.wansenai.vo.financial

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansenai.NoArg
import com.wansenai.bo.AdvanceChargeDataBO
import com.wansenai.bo.BigDecimalSerializerBO
import com.wansenai.bo.FileDataBO
import lombok.Data
import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
@Data
data class AdvanceChargeDetailVO (

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var memberId: Long? = null,

    var memberName: String? = null,

    var receiptNumber: String,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var receiptDate: LocalDateTime,

    var financialPersonnel: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var financialPersonnelId: Long? = null,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var totalAmount : BigDecimal,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var collectedAmount : BigDecimal,

    var tableData: List<AdvanceChargeDataBO>,

    var files: List<FileDataBO>? = null,
)
