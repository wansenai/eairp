package com.wansensoft.dto.product

import com.wansensoft.dto.PageSizeDTO
import lombok.Data

@Data
class ProductAttributeQueryDTO : PageSizeDTO() {

    val attributeName: String? = null
}