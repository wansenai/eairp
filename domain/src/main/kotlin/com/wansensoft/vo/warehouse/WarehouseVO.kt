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