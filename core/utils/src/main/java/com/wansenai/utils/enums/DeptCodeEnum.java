package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum DeptCodeEnum {

    ADD_DEPARTMENT_SUCCESS("A0009", "成功添加部门"),

    ADD_DEPARTMENT_SUCCESS_EN("A0009", "Successfully add department"),

    ADD_DEPARTMENT_ERROR("A0212", "系统异常，添加部门失败"),

    ADD_DEPARTMENT_ERROR_EN("A0212", "System exception, failed to add department"),

    UPDATE_DEPARTMENT_SUCCESS("A0010", "成功修改部门"),

    UPDATE_DEPARTMENT_SUCCESS_EN("A0010", "Successfully modify department"),

    UPDATE_DEPARTMENT_ERROR("A0213", "系统异常，修改部门失败"),

    UPDATE_DEPARTMENT_ERROR_EN("A0213", "System exception, failed to modify department"),

    DELETE_DEPARTMENT_SUCCESS("A0011", "成功删除部门"),

    DELETE_DEPARTMENT_SUCCESS_EN("A0011", "Successfully delete department"),

    DELETE_DEPARTMENT_ERROR("A0214", "系统异常，删除部门失败"),

    DELETE_DEPARTMENT_ERROR_EN("A0214", "System exception, failed to delete department");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    DeptCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
