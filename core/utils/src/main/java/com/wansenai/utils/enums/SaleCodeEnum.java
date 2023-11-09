package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum SaleCodeEnum {

    ADD_SALE_ORDER_SUCCESS("S0001", "添加销售订单成功"),

    ADD_SALE_ORDER_ERROR("S0500", "添加销售订单失败"),

    UPDATE_SALE_ORDER_SUCCESS("S0002", "修改销售订单成功"),

    UPDATE_SALE_ORDER_ERROR("S0501", "修改销售订单失败"),

    DELETE_SALE_ORDER_SUCCESS("S0003", "删除销售订单成功"),

    DELETE_SALE_ORDER_ERROR("S0502", "删除销售订单失败"),

    ADD_SALE_SHIPMENTS_SUCCESS("S0004", "添加销售出货成功"),

    ADD_SALE_SHIPMENTS_ERROR("S0503", "添加销售出货失败"),

    UPDATE_SALE_SHIPMENTS_SUCCESS("S0005", "修改销售出货成功"),

    UPDATE_SALE_SHIPMENTS_ERROR("S0504", "修改销售出货失败"),

    DELETE_SALE_SHIPMENTS_SUCCESS("S0006", "删除销售出货成功"),

    DELETE_SALE_SHIPMENTS_ERROR("S0505", "删除销售出货失败"),

    ADD_SALE_REFUND_SUCCESS("S0007", "添加销售退货成功"),

    ADD_SALE_REFUND_ERROR("S0506", "添加销售退货失败"),

    UPDATE_SALE_REFUND_SUCCESS("S0008", "修改销售退货成功"),

    UPDATE_SALE_REFUND_ERROR("S0507", "修改销售退货失败"),

    DELETE_SALE_REFUND_SUCCESS("S0009", "删除销售退货成功"),

    DELETE_SALE_REFUND_ERROR("S0508", "删除销售退货失败");

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
