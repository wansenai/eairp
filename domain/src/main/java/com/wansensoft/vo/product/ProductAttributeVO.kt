package com.wansensoft.vo.product

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Data
import java.time.LocalDateTime

@Data
class ProductAttributeVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id : Long? = null

    var attributeName: String? = null

    var attributeValue: String? = null

    var remark: String? = null

    var sort: Int? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime? = null
}