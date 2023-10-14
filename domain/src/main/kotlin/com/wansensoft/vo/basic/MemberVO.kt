package com.wansensoft.vo.basic

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wansensoft.NoArg
import com.wansensoft.bo.BigDecimalSerializerBO
import java.math.BigDecimal
import java.time.LocalDateTime

@NoArg
data class MemberVO(

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var id: Long?,

    var memberNumber: String?,

    var memberName: String?,

    var phoneNumber: String?,

    var email: String?,

    @JsonSerialize(using = BigDecimalSerializerBO::class)
    var advancePayment: BigDecimal?,

    var status: Int?,

    var remark: String?,

    var sort: Int?,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: LocalDateTime?,
)
