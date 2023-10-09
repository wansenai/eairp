package com.wansensoft.dto.basic

import java.math.BigDecimal

data class UpdateSupplierDTO(
    /**
     * 供应商ID
     */
    val id: Long,
    /**
     * 供应商名称
     */
    val supplierName: String,
    /**
     * 联系人
     */
    val contact: String,
    /**
     * 联系电话
     */
    val contactNumber: String,
    /**
     * 手机号码
     */
    val phoneNumber: String,
    /**
     * 地址
     */
    val address: String,
    /**
     * 邮箱
     */
    val email: String,
    /**
     * 状态
     */
    val status: Int,
    /**
     * 第一季度应收账款
     */
    val firstQuarterAccountReceivable: Double,
    /**
     * 第二季度应收账款
     */
    val secondQuarterAccountReceivable: Double,
    /**
     * 第三季度应收账款
     */
    val thirdQuarterAccountReceivable: Double,
    /**
     * 第四季度应收账款
     */
    val fourthQuarterAccountReceivable: Double,
    /**
     * 应收账款总额
     */
    val totalAccountReceivable: BigDecimal,
    /**
     * 第一季度应付账款
     */
    val firstQuarterAccountPayment: Double,
    /**
     * 第二季度应付账款
     */
    val secondQuarterAccountPayment: Double,
    /**
     * 第三季度应付账款
     */
    val thirdQuarterAccountPayment: Double,
    /**
     * 第四季度应付账款
     */
    val fourthQuarterAccountPayment: Double,
    /**
     * 应付账款总额
     */
    val totalAccountPayment: BigDecimal,
    /**
     * 传真
     */
    val fax: String,
    /**
     * 税号
     */
    val taxNumber: String,
    /**
     * 开户行
     */
    val bankName: String,
    /**
     * 账号
     */
    val accountNumber: String,
    /**
     * 税率
     */
    val taxRate: String,
    /**
     * 排序
     */
    val sort: Int,
    /**
     * 备注
     */
    val remark: String,
)
