/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum AllotShipmentCodeEnum {

    ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS("S0010", "添加调拨出库单据成功"),

    ADD_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR("S0509", "添加调拨出库单据失败"),

    UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS("S0011", "修改调拨出库单据成功"),

    UPDATE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR("S0510", "修改调拨出库单据失败"),

    DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_SUCCESS("S0012", "删除调拨出库单据成功"),

    DELETE_ALLOT_SHIPMENT_STOCK_RECEIPT_ERROR("S0511", "删除调拨出库单据失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    AllotShipmentCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
