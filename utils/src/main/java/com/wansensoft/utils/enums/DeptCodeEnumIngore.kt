package com.wansensoft.utils.enums

import lombok.Getter

@Getter
enum class DeptCodeEnumIngore(val code: String, val msg: String) {

    ADD_DEPARTMENT_SUCCESS("A0009", "添加部门成功"),

    ADD_DEPARTMENT_ERROR("A0212", "添加部门失败"),

    UPDATE_DEPARTMENT_SUCCESS("A0010", "修改部门成功"),

    UPDATE_DEPARTMENT_ERROR("A0213", "修改部门失败"),

    DELETE_DEPARTMENT_SUCCESS("A0011", "删除部门成功"),

    DELETE_DEPARTMENT_ERROR("A0214", "删除部门失败"),
}