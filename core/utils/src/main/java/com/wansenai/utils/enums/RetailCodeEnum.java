package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum RetailCodeEnum {

    ADD_RETAIL_SHIPMENTS_SUCCESS("R0001", "添加零售出库单成功"),

    ADD_RETAIL_SHIPMENTS_ERROR("R0500", "添加零售出库单失败"),

    UPDATE_RETAIL_SHIPMENTS_SUCCESS("R0002", "修改零售出库单成功"),

    UPDATE_RETAIL_SHIPMENTS_ERROR("R0501", "修改零售出库单失败"),

    DELETE_RETAIL_SHIPMENTS_SUCCESS("R0003", "删除零售出库单成功"),

    DELETE_RETAIL_SHIPMENTS_ERROR("R0502", "删除零售出库单失败"),

    ADD_RETAIL_REFUND_SUCCESS("R0004", "添加零售退货单成功"),

    ADD_RETAIL_REFUND_ERROR("R0503", "添加零售退货单失败"),

    UPDATE_RETAIL_REFUND_SUCCESS("R0005", "修改零售退货单成功"),

    UPDATE_RETAIL_REFUND_ERROR("R0504", "修改零售退货单失败"),

    DELETE_RETAIL_REFUND_SUCCESS("R0006", "删除零售货单成功"),

    DELETE_RETAIL_REFUND_ERROR("R0505", "删除零售货单失败");


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
