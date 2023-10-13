package com.wansensoft.utils.enums

/**
 * 因为BaseCodeEnum.java类将来需要扩展其他业务代码，默认占用首字母ABC
 * 所以这里CustomerCodeEnum枚举取第二个字母U大写
 */
enum class CustomerCodeEnum(val code: String, val msg: String) {

    ADD_CUSTOMER_SUCCESS("U0001", "供应商添加成功"),
    ADD_CUSTOMER_ERROR("U0500", "供应商添加失败"),

    UPDATE_CUSTOMER_SUCCESS("U0002", "供应商修改成功"),
    UPDATE_CUSTOMER_ERROR("U0501", "供应商修改失败"),

    DELETE_CUSTOMER_SUCCESS("U0003", "供应商删除成功"),
    DELETE_CUSTOMER_ERROR("U0504", "供应商删除失败"),

    UPDATE_CUSTOMER_STATUS_SUCCESS("U0004", "供应商状态修改成功"),
    UPDATE_CUSTOMER_STATUS_ERROR("U0505", "供应商状态修改失败")
}