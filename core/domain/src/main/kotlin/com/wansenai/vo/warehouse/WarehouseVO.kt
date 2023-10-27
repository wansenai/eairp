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
package com.wansenai.vo.warehouse

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansenai.NoArg
import com.wansenai.bo.BigDecimalSerializerBO
import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
@JsonInclude(JsonInclude.Include.NON_NULL)
data class WarehouseVO (

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id : Long? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var warehouseManager : Long? = null,

    var warehouseManagerName : String? = null,

    var warehouseName : String? = null,

    var address : String? = null,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var price : BigDecimal? = null,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var truckage: BigDecimal? = null,

    var type: Int? = null,

    var status: Int? = null,

    var remark: String? = null,

    var sort: Int? = null,

    var isDefault: Int? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime? = null,
)