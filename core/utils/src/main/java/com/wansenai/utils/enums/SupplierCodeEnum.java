package com.wansenai.utils.enums;


import lombok.Getter;

@Getter
public enum SupplierCodeEnum {

    ADD_SUPPLIER_SUCCESS("S0001", "成功添加供应商"),

    ADD_SUPPLIER_SUCCESS_EN("S0001", "Successfully add supplier"),

    ADD_SUPPLIER_ERROR("S0500", "系统异常，供应商添加失败"),

    ADD_SUPPLIER_ERROR_EN("S0500", "System exception, failed to add supplier"),

    UPDATE_SUPPLIER_SUCCESS("S0002", "成功修改供应商信息"),

    UPDATE_SUPPLIER_SUCCESS_EN("S0002", "Successfully modify supplier information"),

    UPDATE_SUPPLIER_ERROR("S0501", "系统异常，供应商信息修改失败"),

    UPDATE_SUPPLIER_ERROR_EN("S0501", "System exception, failed to modify supplier information"),

    DELETE_SUPPLIER_SUCCESS("S0003", "成功删除供应商"),

    DELETE_SUPPLIER_SUCCESS_EN("S0003", "Successfully delete supplier"),

    DELETE_SUPPLIER_ERROR("S0504", "系统异常，供应商删除失败"),

    DELETE_SUPPLIER_ERROR_EN("S0504", "System exception, failed to delete supplier"),

    UPDATE_SUPPLIER_STATUS_SUCCESS("S0004", "成功修改供应商状态"),

    UPDATE_SUPPLIER_STATUS_SUCCESS_EN("S0004", "Successfully modify supplier status"),

    UPDATE_SUPPLIER_STATUS_ERROR("S0505", "系统异常，供应商状态修改失败"),

    UPDATE_SUPPLIER_STATUS_ERROR_EN("S0505", "System exception, failed to modify supplier status"),;

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
