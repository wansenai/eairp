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
public enum OtherShipmentCodeEnum {

    ADD_OTHER_SHIPMENT_STOCK_SUCCESS("S0013", "添加其他出库单据成功"),

    ADD_OTHER_SHIPMENT_STOCK_ERROR("S0512", "添加其他出库单据失败"),

    UPDATE_OTHER_SHIPMENT_STOCK_SUCCESS("S0014", "修改其他出库单据成功"),

    UPDATE_OTHER_SHIPMENT_STOCK_ERROR("S0513", "修改其他出库单据失败"),

    DELETE_OTHER_SHIPMENT_STOCK_SUCCESS("S0015", "删除其他出库单据成功"),

    DELETE_OTHER_SHIPMENT_STOCK_ERROR("S0514", "删除其他出库单据失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    OtherShipmentCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
