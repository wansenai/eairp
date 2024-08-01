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

    ADD_COLLECTION_RECEIPT_SUCCESS("C0001", "成功添加收款单据"),

    ADD_COLLECTION_RECEIPT_SUCCESS_EN("C0001", "Successfully add collection document"),

    ADD_COLLECTION_RECEIPT_ERROR("C0500", "系统异常，添加收款单据失败"),

    ADD_COLLECTION_RECEIPT_ERROR_EN("C0500", "System exception, failed to add collection document"),

    UPDATE_COLLECTION_RECEIPT_SUCCESS("C0003", "成功修改收款单据"),

    UPDATE_COLLECTION_RECEIPT_SUCCESS_EN("C0003", "Successfully modify collection document"),

    UPDATE_COLLECTION_RECEIPT_ERROR("C0501", "系统异常，修改收款单据失败"),

    UPDATE_COLLECTION_RECEIPT_ERROR_EN("C0501", "System exception, failed to modify collection document"),

    DELETE_COLLECTION_RECEIPT_SUCCESS("C0004", "成功删除收款单据"),

    DELETE_COLLECTION_RECEIPT_SUCCESS_EN("C0004", "Successfully delete collection document"),

    DELETE_COLLECTION_RECEIPT_ERROR("C0502", "系统异常，删除收款单据失败"),

    DELETE_COLLECTION_RECEIPT_ERROR_EN("C0502", "System exception, failed to delete collection document"),

    ADD_PAYMENT_RECEIPT_SUCCESS("P0024", "成功添加付款单据"),

    ADD_PAYMENT_RECEIPT_SUCCESS_EN("P0024", "Successfully add payment document"),

    ADD_PAYMENT_RECEIPT_ERROR("P0526", "系统异常，添加付款单据失败"),

    ADD_PAYMENT_RECEIPT_ERROR_EN("P0526", "System exception, failed to add payment document"),

    UPDATE_PAYMENT_RECEIPT_SUCCESS("P0025", "成功修改付款单据"),

    UPDATE_PAYMENT_RECEIPT_SUCCESS_EN("P0025", "Successfully modify payment document"),

    UPDATE_PAYMENT_RECEIPT_ERROR("P0527", "系统异常，修改付款单据失败"),

    UPDATE_PAYMENT_RECEIPT_ERROR_EN("P0527", "System exception, failed to modify payment document"),

    DELETE_PAYMENT_RECEIPT_SUCCESS("P0026", "成功删除付款单据"),

    DELETE_PAYMENT_RECEIPT_SUCCESS_EN("P0026", "Successfully delete payment document"),

    DELETE_PAYMENT_RECEIPT_ERROR("P0528", "系统异常，删除付款单据失败"),

    DELETE_PAYMENT_RECEIPT_ERROR_EN("P0528", "System exception, failed to delete payment document");

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
