package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum OperatorCodeEnum {

    ADD_OPERATOR_SUCCESS("O0001", "成功添加操作员"),

    ADD_OPERATOR_SUCCESS_EN("O0001", "Successfully added operator"),

    ADD_OPERATOR_ERROR("O0500", "系统异常，操作员添加失败"),

    ADD_OPERATOR_ERROR_EN("O0500", "System exception, failed to add operator"),

    UPDATE_OPERATOR_SUCCESS("O0002", "成功修改操作员信息"),

    UPDATE_OPERATOR_SUCCESS_EN("O0002", "Successfully modified operator information"),

    UPDATE_OPERATOR_ERROR("O0501", "系统异常，操作员信息修改失败"),

    UPDATE_OPERATOR_ERROR_EN("O0501", "System exception, failed to modify operator information"),

    DELETE_OPERATOR_SUCCESS("O0003", "成功删除操作员"),

    DELETE_OPERATOR_SUCCESS_EN("O0003", "Successfully deleted operator"),

    DELETE_OPERATOR_ERROR("O0502", "系统异常，操作员删除失败"),

    DELETE_OPERATOR_ERROR_EN("O0502", "System exception, failed to delete operator"),

    UPDATE_OPERATOR_STATUS_SUCCESS("O0004", "成功修改操作员状态"),

    UPDATE_OPERATOR_STATUS_SUCCESS_EN("O0004", "Successfully modified operator status"),

    UPDATE_OPERATOR_STATUS_ERROR("O0503", "系统异常，操作员状态修改失败"),

    UPDATE_OPERATOR_STATUS_ERROR_EN("O0503", "System exception, failed to modify operator status");

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
