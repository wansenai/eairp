package com.wansenai.utils.enums

enum class WarehouseCodeEnum(val code: String, val msg: String) {

    ADD_WAREHOUSE_SUCCESS("W0001", "成功添加仓库"),

    ADD_WAREHOUSE_SUCCESS_EN("W0001", "Successfully add warehouse"),

    ADD_WAREHOUSE_ERROR("W0500", "系统异常，仓库添加失败"),

    ADD_WAREHOUSE_ERROR_EN("W0500", "System exception, failed to add warehouse"),

    UPDATE_WAREHOUSE_INFO_SUCCESS("W0002", "成功修改仓库信息"),

    UPDATE_WAREHOUSE_INFO_SUCCESS_EN("W0002", "Successfully modify warehouse info"),

    UPDATE_WAREHOUSE_INFO_ERROR("W0501", "系统异常，仓库信息修改失败"),

    UPDATE_WAREHOUSE_INFO_ERROR_EN("W0501", "System exception, failed to modify warehouse info"),

    DELETE_WAREHOUSE_SUCCESS("W0003", "成功删除仓库"),

    DELETE_WAREHOUSE_SUCCESS_EN("W0003", "Successfully delete warehouse"),

    DELETE_WAREHOUSE_ERROR("W0504", "系统异常，仓库删除失败"),

    DELETE_WAREHOUSE_ERROR_EN("W0504", "System exception, failed to delete warehouse"),

    UPDATE_WAREHOUSE_STATUS_SUCCESS("W0004", "成功修改仓库状态"),

    UPDATE_WAREHOUSE_STATUS_SUCCESS_EN("W0004", "Successfully modify warehouse status"),

    UPDATE_WAREHOUSE_STATUS_ERROR("W0505", "系统异常，仓库状态修改失败"),

    UPDATE_WAREHOUSE_STATUS_ERROR_EN("W0505", "System exception, failed to modify warehouse status"),
}