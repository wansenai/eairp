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
package com.wansenai.vo.product

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansenai.bo.BigDecimalSerializerBO
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductUnitVO (

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id : Long,

    var basicUnit: String? = null,

    var otherUnit: String? = null,

    // Table All Unit Assembly Text Data
    var computeUnit : String? = null,

    // Table Multiple Unit Text Data
    var otherComputeUnit : String? = null,

    var otherUnitTwo: String? = null,

    // Table Multiple Unit 2 Text Data
    var otherComputeUnitTwo : String? = null,

    var otherUnitThree: String? = null,

    // Table Multiple Unit 3 Text Data
    var otherComputeUnitThree : String? = null,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var ratio: BigDecimal? = null,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var ratioTwo: BigDecimal? = null,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var ratioThree: BigDecimal? = null,

    var status: Int? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime? = null
)