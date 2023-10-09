package com.wansensoft.utils.enums;


import lombok.Getter;

@Getter
public enum SupplierCodeEnum {

    ADD_SUPPLIER_SUCCESS("S0001", "供应商添加成功"),

    ADD_SUPPLIER_ERROR("S0500", "供应商添加失败"),

    UPDATE_SUPPLIER_SUCCESS("S0002", "供应商修改成功"),

    UPDATE_SUPPLIER_ERROR("S0501", "供应商修改失败"),

    DELETE_SUPPLIER_SUCCESS("S0003", "供应商删除成功"),

    DELETE_SUPPLIER_ERROR("S0504", "供应商删除失败");


    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    SupplierCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
