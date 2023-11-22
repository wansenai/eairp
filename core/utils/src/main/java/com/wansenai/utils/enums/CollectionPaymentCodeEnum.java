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
public enum CollectionPaymentCodeEnum {

    ADD_COLLECTION_RECEIPT_SUCCESS("C0001", "添加收款单成功"),

    ADD_COLLECTION_RECEIPT_ERROR("C0500", "添加收款单失败"),

    UPDATE_COLLECTION_RECEIPT_SUCCESS("C0003", "修改收款单成功"),

    UPDATE_COLLECTION_RECEIPT_ERROR("C0501", "修改收款单失败"),

    DELETE_COLLECTION_RECEIPT_SUCCESS("C0004", "删除收款单成功"),

    DELETE_COLLECTION_RECEIPT_ERROR("C0502", "删除收款单失败"),

    ADD_PAYMENT_RECEIPT_SUCCESS("P0024", "添加付款单成功"),

    ADD_PAYMENT_RECEIPT_ERROR("P0526", "添加付款单失败"),

    UPDATE_PAYMENT_RECEIPT_SUCCESS("P0025", "修改付款单成功"),

    UPDATE_PAYMENT_RECEIPT_ERROR("P0527", "修改付款单失败"),

    DELETE_PAYMENT_RECEIPT_SUCCESS("P0026", "删除付款单成功"),

    DELETE_PAYMENT_RECEIPT_ERROR("P0528", "删除付款单失败");


    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    CollectionPaymentCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
