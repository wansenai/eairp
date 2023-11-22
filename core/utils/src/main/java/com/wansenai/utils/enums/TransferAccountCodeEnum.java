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
public enum TransferAccountCodeEnum {

    ADD_TRANSFER_ACCOUNT_RECEIPT_SUCCESS("T0001", "添加转账单成功"),

    ADD_TRANSFER_ACCOUNT_RECEIPT_ERROR("T0502", "添加转账单失败"),

    UPDATE_TRANSFER_ACCOUNT_RECEIPT_SUCCESS("T0002", "修改转账单成功"),

    UPDATE_TRANSFER_ACCOUNT_RECEIPT_ERROR("T0503", "修改转账单失败"),

    DELETE_TRANSFER_ACCOUNT_RECEIPT_SUCCESS("T0003", "删除转账单成功"),

    DELETE_TRANSFER_ACCOUNT_RECEIPT_ERROR("T0504", "删除转账单失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    TransferAccountCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
