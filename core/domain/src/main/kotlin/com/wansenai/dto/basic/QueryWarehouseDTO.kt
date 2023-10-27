package com.wansenai.dto.basic

data class QueryWarehouseDTO (

    val warehouseName: String?,

    val remark: String?,

    val page: Long?,

    val pageSize: Long?,

    val startDate: String?,

    val endDate: String?,
)