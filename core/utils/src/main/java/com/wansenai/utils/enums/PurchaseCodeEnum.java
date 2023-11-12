package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum PurchaseCodeEnum {

    ADD_PURCHASE_ORDER_SUCCESS("P0015", "添加采购订单成功"),

    ADD_PURCHASE_ORDER_ERROR("P0517", "添加采购订单失败"),

    UPDATE_PURCHASE_ORDER_SUCCESS("P0016", "修改采购订单成功"),

    UPDATE_PURCHASE_ORDER_ERROR("P0518", "修改采购订单失败"),

    DELETE_PURCHASE_ORDER_SUCCESS("P0017", "删除采购订单成功"),

    DELETE_PURCHASE_ORDER_ERROR("P0519", "删除采购订单失败"),

    ADD_PURCHASE_RECEIPT_SUCCESS("P0018", "添加采购收货成功"),

    ADD_PURCHASE_RECEIPT_ERROR("P0520", "添加采购收货失败"),

    UPDATE_PURCHASE_RECEIPT_SUCCESS("P0019", "修改采购收货成功"),

    UPDATE_PURCHASE_RECEIPT_ERROR("P0521", "修改采购收货失败"),

    DELETE_PURCHASE_RECEIPT_SUCCESS("P0020", "删除采购收货成功"),

    DELETE_PURCHASE_RECEIPT_ERROR("P0522", "删除采购收货失败"),

    ADD_PURCHASE_REFUND_SUCCESS("P0021", "添加采购退货成功"),

    ADD_PURCHASE_REFUND_ERROR("P0523", "添加采购退货失败"),

    UPDATE_PURCHASE_REFUND_SUCCESS("P0022", "修改采购退货成功"),

    UPDATE_PURCHASE_REFUND_ERROR("P0524", "修改采购退货失败"),

    DELETE_PURCHASE_REFUND_SUCCESS("P0023", "删除采购退货成功"),

    DELETE_PURCHASE_REFUND_ERROR("P0525", "删除采购退货失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    PurchaseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
