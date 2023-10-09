package com.wansensoft.dto.basic

data class QuerySupplierDTO (
    /**
     * 供应商名称
     */
    val supplierName: String?,
    /**
     * 联系人
     */
    val contact: String?,
    /**
     * 联系电话
     */
    val contactNumber: String?,
    /**
     * 手机号码
     */
    val phoneNumber: String?,

    /**
     * 分页信息
     */
    val page: Long?,

    val pageSize: Long?
)
