package com.wansenai.utils.enums

/**
 * 因为BaseCodeEnum.java类将来需要扩展其他业务代码，默认占用首字母ABC
 * 所以这里CustomerCodeEnum枚举取第二个字母U大写
 */
enum class CustomerCodeEnum(val code: String, val msg: String) {

    ADD_CUSTOMER_SUCCESS("U0001", "成功添加客户"),

    ADD_CUSTOMER_SUCCESS_EN("U0001", "Successfully add customer"),

    ADD_CUSTOMER_ERROR("U0500", "系统异常，客户添加失败"),

    ADD_CUSTOMER_ERROR_EN("U0500", "System exception, failed to add customer"),

    UPDATE_CUSTOMER_SUCCESS("U0002", "成功修改客户信息"),

    UPDATE_CUSTOMER_SUCCESS_EN("U0002", "Successfully modify customer info"),

    UPDATE_CUSTOMER_ERROR("U0501", "系统异常，客户信息修改失败"),

    UPDATE_CUSTOMER_ERROR_EN("U0501", "System exception, failed to modify customer info"),

    DELETE_CUSTOMER_SUCCESS("U0003", "成功删除客户"),

    DELETE_CUSTOMER_SUCCESS_EN("U0003", "Successfully delete customer"),

    DELETE_CUSTOMER_ERROR("U0504", "系统异常，客户删除失败"),

    DELETE_CUSTOMER_ERROR_EN("U0504", "System exception, failed to delete customer"),

    UPDATE_CUSTOMER_STATUS_SUCCESS("U0004", "成功修改客户状态"),

    UPDATE_CUSTOMER_STATUS_SUCCESS_EN("U0004", "Successfully modify customer status"),

    UPDATE_CUSTOMER_STATUS_ERROR("U0505", "系统异常，客户状态修改失败"),

    UPDATE_CUSTOMER_STATUS_ERROR_EN("U0505", "System exception, failed to modify customer status"),
}