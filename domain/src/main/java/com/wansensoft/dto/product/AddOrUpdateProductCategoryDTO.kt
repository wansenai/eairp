package com.wansensoft.dto.product

import lombok.Data

@Data
class AddOrUpdateProductCategoryDTO {

    val id: Long? = null

    val categoryName: String? = null

    val categoryLevel: Int? = null

    val parentId: Long? = null

    val sort: Int? = null

    val serialNumber: String? = null

    val remark: String? = null
}