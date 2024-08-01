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

    ADD_OTHER_SHIPMENT_STOCK_SUCCESS("S0013", "成功添加其他出库单据"),

    ADD_OTHER_SHIPMENT_STOCK_SUCCESS_EN("S0013", "Successfully add other outbound documents"),

    ADD_OTHER_SHIPMENT_STOCK_ERROR("S0512", "系统异常，添加其他出库单据失败"),

    ADD_OTHER_SHIPMENT_STOCK_ERROR_EN("S0512", "System exception, failed to add other outbound documents"),

    UPDATE_OTHER_SHIPMENT_STOCK_SUCCESS("S0014", "成功修改其他出库单据"),

    UPDATE_OTHER_SHIPMENT_STOCK_SUCCESS_EN("S0014", "Successfully modify other outbound documents"),

    UPDATE_OTHER_SHIPMENT_STOCK_ERROR("S0513", "系统异常，修改其他出库单据失败"),

    UPDATE_OTHER_SHIPMENT_STOCK_ERROR_EN("S0513", "System exception, failed to modify other outbound documents"),

    DELETE_OTHER_SHIPMENT_STOCK_SUCCESS("S0015", "成功删除其他出库单据"),

    DELETE_OTHER_SHIPMENT_STOCK_SUCCESS_EN("S0015", "Successfully delete other outbound documents"),

    DELETE_OTHER_SHIPMENT_STOCK_ERROR("S0514", "系统异常，删除其他出库单据失败"),

    DELETE_OTHER_SHIPMENT_STOCK_ERROR_EN("S0514", "System exception, failed to delete other outbound documents");

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
