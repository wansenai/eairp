package com.wansensoft.dto.basic

import com.wansensoft.NoArg
import java.math.BigDecimal

@NoArg
data class AddOrUpdateWarehouseDTO (

    val id : Long?,

    // 用户的id
    val warehouseManager: Long?,

    val warehouseName: String?,

    val address: String?,

    val price: BigDecimal?,

    val truckage: BigDecimal?,

    val type: Int?,

    val status: Int?,

    val remark: String?,

    val sort: Int?,

    val isDefault: Int?,
)