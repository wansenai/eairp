package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum PurchaseCodeEnum {

    ADD_PURCHASE_ORDER_SUCCESS("P0015", "成功添加采购订单"),

    ADD_PURCHASE_ORDER_SUCCESS_EN("P0015", "Successfully add purchase order"),

    ADD_PURCHASE_ORDER_ERROR("P0517", "系统异常，添加采购订单失败"),

    ADD_PURCHASE_ORDER_ERROR_EN("P0517", "System exception, failed to add purchase order"),

    UPDATE_PURCHASE_ORDER_SUCCESS("P0016", "成功修改采购订单"),

    UPDATE_PURCHASE_ORDER_SUCCESS_EN("P0016", "Successfully modify purchase order"),

    UPDATE_PURCHASE_ORDER_ERROR("P0518", "系统异常，修改采购订单失败"),

    UPDATE_PURCHASE_ORDER_ERROR_EN("P0518", "System exception, failed to modify purchase order"),

    DELETE_PURCHASE_ORDER_SUCCESS("P0017", "成功删除采购订单"),

    DELETE_PURCHASE_ORDER_SUCCESS_EN("P0017", "Successfully delete purchase order"),

    DELETE_PURCHASE_ORDER_ERROR("P0519", "系统异常，删除采购订单失败"),

    DELETE_PURCHASE_ORDER_ERROR_EN("P0519", "System exception, failed to delete purchase order"),

    ADD_PURCHASE_RECEIPT_SUCCESS("P0018", "成功添加采购入库单据"),

    ADD_PURCHASE_RECEIPT_SUCCESS_EN("P0018", "Successfully add purchase receipt document"),

    ADD_PURCHASE_RECEIPT_ERROR("P0520", "系统异常，添加采购入库单据失败"),

    ADD_PURCHASE_RECEIPT_ERROR_EN("P0520", "System exception, failed to add purchase receipt document"),

    UPDATE_PURCHASE_RECEIPT_SUCCESS("P0019", "修改采购入库单据成功"),

    UPDATE_PURCHASE_RECEIPT_SUCCESS_EN("P0019", "Successfully modify purchase receipt document"),

    UPDATE_PURCHASE_RECEIPT_ERROR("P0521", "系统异常，修改采购入库单据失败"),

    UPDATE_PURCHASE_RECEIPT_ERROR_EN("P0521", "System exception, failed to modify purchase receipt document"),

    DELETE_PURCHASE_RECEIPT_SUCCESS("P0020", "成功删除采购入库单据"),

    DELETE_PURCHASE_RECEIPT_SUCCESS_EN("P0020", "Successfully delete purchase receipt document"),

    DELETE_PURCHASE_RECEIPT_ERROR("P0522", "系统异常，删除采购入库单据失败"),

    DELETE_PURCHASE_RECEIPT_ERRORS_EN("P0522", "System exception, failed to delete purchase receipt document"),

    ADD_PURCHASE_REFUND_SUCCESS("P0021", "成功添加采购退货单据"),

    ADD_PURCHASE_REFUND_SUCCESS_EN("P0021", "Successfully add purchase return document"),

    ADD_PURCHASE_REFUND_ERROR("P0523", "系统异常，添加采购退货单据失败"),

    ADD_PURCHASE_REFUND_ERROR_EN("P0523", "System exception, failed to add purchase return document"),

    UPDATE_PURCHASE_REFUND_SUCCESS("P0022", "成功修改采购退货单据"),

    UPDATE_PURCHASE_REFUND_SUCCESS_EN("P0022", "Successfully modify purchase return document"),

    UPDATE_PURCHASE_REFUND_ERROR("P0524", "系统异常，修改采购退货单据失败"),

    UPDATE_PURCHASE_REFUND_ERROR_EN("P0524", "System exception, failed to modify purchase return document"),

    DELETE_PURCHASE_REFUND_SUCCESS("P0023", "成功删除采购退货单据"),

    DELETE_PURCHASE_REFUND_SUCCESS_EN("P0023", "Successfully delete purchase return document"),

    DELETE_PURCHASE_REFUND_ERROR("P0525", "系统异常，删除采购退货单据失败"),

    DELETE_PURCHASE_REFUND_ERROR_EN("P0525", "System exception, failed to delete purchase return document");

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
