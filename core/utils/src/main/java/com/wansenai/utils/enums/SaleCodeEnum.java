package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum SaleCodeEnum {

    ADD_SALE_ORDER_SUCCESS("S0001", "成功添加销售订单"),

    ADD_SALE_ORDER_SUCCESS_EN("S0001", "Successfully add sales order"),

    ADD_SALE_ORDER_ERROR("S0500", "系统异常，添加销售订单失败"),

    ADD_SALE_ORDER_ERROR_EN("S0500", "System exception, failed to add sales order"),

    UPDATE_SALE_ORDER_SUCCESS("S0002", "成功修改销售订单"),

    UPDATE_SALE_ORDER_SUCCESS_EN("S0002", "Successfully modify sales order"),

    UPDATE_SALE_ORDER_ERROR("S0501", "系统异常，修改销售订单失败"),

    UPDATE_SALE_ORDER_ERROR_EN("S0501", "System exception, failed to modify sales order"),

    DELETE_SALE_ORDER_SUCCESS("S0003", "成功删除销售订单"),

    DELETE_SALE_ORDER_SUCCESS_EN("S0003", "Successfully delete sales order"),

    DELETE_SALE_ORDER_ERROR("S0502", "系统异常，删除销售订单失败"),

    DELETE_SALE_ORDER_ERROR_EN("S0502", "System exception, failed to delete sales order"),

    ADD_SALE_SHIPMENTS_SUCCESS("S0004", "成功添加销售出库单据"),

    ADD_SALE_SHIPMENTS_SUCCESS_EN("S0004", "Successfully add sales outbound document"),

    ADD_SALE_SHIPMENTS_ERROR("S0503", "系统异常，添加销售出库单据失败"),

    ADD_SALE_SHIPMENTS_ERROR_EN("S0503", "System exception, failed to add sales outbound document"),

    UPDATE_SALE_SHIPMENTS_SUCCESS("S0005", "成功修改销售出库单据"),

    UPDATE_SALE_SHIPMENTS_SUCCESS_EN("S0005", "Successfully modify sales outbound document"),

    UPDATE_SALE_SHIPMENTS_ERROR("S0504", "系统异常，修改销售单据单据失败"),

    UPDATE_SALE_SHIPMENTS_ERROR_EN("S0504", "System exception, failed to modify sales outbound document"),

    DELETE_SALE_SHIPMENTS_SUCCESS("S0006", "成功删除销售出库单据"),

    DELETE_SALE_SHIPMENTS_SUCCESS_EN("S0006", "Successfully delete sales outbound document"),

    DELETE_SALE_SHIPMENTS_ERROR("S0505", "系统异常，删除销售出库单据失败"),

    DELETE_SALE_SHIPMENTS_ERROR_EN("S0505", "System exception, failed to delete sales outbound document"),

    ADD_SALE_REFUND_SUCCESS("S0007", "成功添加销售退货单据"),

    ADD_SALE_REFUND_SUCCESS_EN("S0007", "Successfully add sales return document"),

    ADD_SALE_REFUND_ERROR("S0506", "系统异常，添加销售退货单据失败"),

    ADD_SALE_REFUND_ERROR_EN("S0506", "System exception, failed to add sales return document"),

    UPDATE_SALE_REFUND_SUCCESS("S0008", "成功修改销售退货单据"),

    UPDATE_SALE_REFUND_SUCCESS_EN("S0008", "Successfully modify sales return document"),

    UPDATE_SALE_REFUND_ERROR("S0507", "系统异常，修改销售退货单据失败"),

    UPDATE_SALE_REFUND_ERROR_EN("S0507", "System exception, failed to modify sales return document"),

    DELETE_SALE_REFUND_SUCCESS("S0009", "成功删除销售退货单据"),

    DELETE_SALE_REFUND_SUCCESS_EN("S0009", "Successfully delete sales return document"),

    DELETE_SALE_REFUND_ERROR("S0508", "系统异常，删除销售退货单据失败"),

    DELETE_SALE_REFUND_ERROR_EN("S0508", "System exception, failed to delete sales return document");

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
