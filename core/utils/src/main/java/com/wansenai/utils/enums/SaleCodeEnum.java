package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum SaleCodeEnum {

    ADD_SALE_ORDER_SUCCESS("S0001", "添加销售订单成功"),

    ADD_SALE_ORDER_ERROR("S0500", "添加销售订单失败"),

    UPDATE_SALE_ORDER_SUCCESS("S0002", "修改销售订单成功"),

    UPDATE_SALE_ORDER_ERROR("S0501", "修改销售订单失败"),

    DELETE_SALE_ORDER_SUCCESS("S0003", "删除销售订单成功"),

    DELETE_SALE_ORDER_ERROR("S0502", "删除销售订单失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    SaleCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
