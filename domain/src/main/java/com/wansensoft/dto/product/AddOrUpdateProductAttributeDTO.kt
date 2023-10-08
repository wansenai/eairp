package com.wansensoft.dto.product

import lombok.Data

@Data
class AddOrUpdateProductAttributeDTO {

    var id : Long? = null

    var attributeName: String? = null

    var attributeValue: String? = null

    var remark: String? = null

    var sort: String? = null
}