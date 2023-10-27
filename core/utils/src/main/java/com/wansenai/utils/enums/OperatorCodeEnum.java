package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum OperatorCodeEnum {

    ADD_OPERATOR_SUCCESS("O0001", "经手人添加成功"),

    ADD_OPERATOR_ERROR("O0500", "经手人添加失败"),

    UPDATE_OPERATOR_SUCCESS("O0002", "经手人修改成功"),

    UPDATE_OPERATOR_ERROR("O0501", "经手人修改失败"),

    DELETE_OPERATOR_SUCCESS("O0003", "经手人删除成功"),

    DELETE_OPERATOR_ERROR("O0502", "经手人删除失败"),

    UPDATE_OPERATOR_STATUS_SUCCESS("O0004", "经手人状态修改成功"),

    UPDATE_OPERATOR_STATUS_ERROR("O0503", "经手人状态修改失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    OperatorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
