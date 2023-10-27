package com.wansenai.utils.enums

enum class WarehouseCodeEnum(val code: String, val msg: String) {

    ADD_WAREHOUSE_SUCCESS("W0001", "仓库添加成功"),
    ADD_WAREHOUSE_ERROR("W0500", "仓库添加失败"),

    UPDATE_WAREHOUSE_INFO_SUCCESS("W0002", "仓库资料修改成功"),
    UPDATE_WAREHOUSE_INFO_ERROR("W0501", "仓库资料修改失败"),

    DELETE_WAREHOUSE_SUCCESS("W0003", "仓库删除成功"),
    DELETE_WAREHOUSE_ERROR("W0504", "仓库删除失败"),

    UPDATE_WAREHOUSE_STATUS_SUCCESS("W0004", "仓库状态修改成功"),
    UPDATE_WAREHOUSE_STATUS_ERROR("W0505", "仓库状态修改失败")
}