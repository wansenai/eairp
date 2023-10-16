package com.wansensoft.vo.warehouse

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansensoft.NoArg
import com.wansensoft.bo.BigDecimalSerializerBO
import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
data class WarehouseVO (

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id : Long? = null,

    var warehouseManager : String? = null,

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime? = null,
)