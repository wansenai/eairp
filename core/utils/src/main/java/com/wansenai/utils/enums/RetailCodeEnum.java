package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum RetailCodeEnum {

    ADD_RETAIL_SHIPMENTS_SUCCESS("R0001", "添加零售出库单成功"),

    ADD_RETAIL_SHIPMENTS_ERROR("R0500", "添加零售出库单失败"),

    UPDATE_RETAIL_SHIPMENTS_SUCCESS("R0002", "修改零售出库单成功"),

    UPDATE_RETAIL_SHIPMENTS_ERROR("R0501", "修改零售出库单失败");


    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    RetailCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
