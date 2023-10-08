package com.wansensoft.dto.product

import lombok.Data

@Data
class AddOrUpdateProductCategoryDTO {

    val id: Long? = null

    val categoryName: String? = null

    val categoryNumber: String? = null

    val parentId: Long? = null

    val sort: Int? = null

    val remark: String? = null
}