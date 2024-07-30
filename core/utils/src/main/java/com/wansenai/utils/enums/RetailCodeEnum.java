package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum RetailCodeEnum {

    ADD_RETAIL_SHIPMENTS_SUCCESS("R0001", "添加零售出库单成功"),

    ADD_RETAIL_SHIPMENTS_SUCCESS_EN("R0001", "Successfully add retail outbound document"),

    ADD_RETAIL_SHIPMENTS_ERROR("R0500", "系统异常，添加零售出库单失败"),

    ADD_RETAIL_SHIPMENTS_ERROR_EN("R0500", "System exception, failed to add retail outbound document"),

    UPDATE_RETAIL_SHIPMENTS_SUCCESS("R0002", "修改零售出库单成功"),

    UPDATE_RETAIL_SHIPMENTS_SUCCESS_EN("R0002", "Successfully modify retail outbound document"),

    UPDATE_RETAIL_SHIPMENTS_ERROR("R0501", "系统异常，修改零售出库单失败"),

    UPDATE_RETAIL_SHIPMENTS_ERROR_EN("R0501", "System exception, failed to modify retail outbound document"),

    DELETE_RETAIL_SHIPMENTS_SUCCESS("R0003", "删除零售出库单成功"),

    DELETE_RETAIL_SHIPMENTS_SUCCESS_EN("R0003", "Successfully delete retail outbound document"),

    DELETE_RETAIL_SHIPMENTS_ERROR("R0502", "系统异常，删除零售出库单失败"),

    DELETE_RETAIL_SHIPMENTS_ERROR_EN("R0502", "System exception, failed to delete retail outbound document"),

    ADD_RETAIL_REFUND_SUCCESS("R0004", "添加零售退货单成功"),

    ADD_RETAIL_REFUND_SUCCESS_EN("R0004", "Successfully add retail return document"),

    ADD_RETAIL_REFUND_ERROR("R0503", "系统异常，添加零售退货单失败"),

    ADD_RETAIL_REFUND_ERROR_EN("R0503", "System exception, failed to add retail return document"),

    UPDATE_RETAIL_REFUND_SUCCESS("R0005", "修改零售退货单成功"),

    UPDATE_RETAIL_REFUND_SUCCESS_EN("R0005", "Successfully modify retail return document"),

    UPDATE_RETAIL_REFUND_ERROR("R0504", "系统异常，修改零售退货单失败"),

    UPDATE_RETAIL_REFUND_ERROR_EN("R0504", "System exception, failed to modify retail return document"),

    DELETE_RETAIL_REFUND_SUCCESS("R0006", "删除零售货单成功"),

    DELETE_RETAIL_REFUND_SUCCESS_EN("R0006", "Successfully delete retail return document"),

    DELETE_RETAIL_REFUND_ERROR("R0505", "系统异常，删除零售货单失败"),

    DELETE_RETAIL_REFUND_ERROR_EN("R0505", "System exception, failed to delete retail return document");


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
