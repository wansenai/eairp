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
public enum FinancialCodeEnum {

    ADD_ACCOUNT_SUCCESS("F0000", "成功添加账户"),

    ADD_ACCOUNT_SUCCESS_EN("F0000", "Successfully add account"),

    ADD_ACCOUNT_ERROR("F0500", "系统异常，添加账户失败"),

    ADD_ACCOUNT_ERROR_EN("F0500", "System exception, failed to add account"),

    UPDATE_ACCOUNT_SUCCESS("F0002", "成功修改账户"),

    UPDATE_ACCOUNT_SUCCESS_EN("F0002", "Successfully modify account"),

    UPDATE_ACCOUNT_ERROR("F0501", "系统异常，修改账户失败"),

    UPDATE_ACCOUNT_ERROR_EN("F0501", "System exception, failed to modify account"),

    DELETE_ACCOUNT_SUCCESS("F0003", "成功删除账户"),

    DELETE_ACCOUNT_SUCCESS_EN("F0003", "Successfully delete account"),

    DELETE_ACCOUNT_ERROR("F0502", "系统异常，删除账户失败"),

    DELETE_ACCOUNT_ERROR_EN("F0502", "System exception, failed to delete account"),

    UPDATE_ACCOUNT_STATUS_SUCCESS("F0004", "成功修改账户状态"),

    UPDATE_ACCOUNT_STATUS_SUCCESS_EN("F0004", "Successfully modify account status"),

    UPDATE_ACCOUNT_STATUS_ERROR("F0503", "系统异常，账户状态修改失败"),

    UPDATE_ACCOUNT_STATUS_ERROR_EN("F0503", "System exception, failed to modify account status"),

    ADD_ADVANCE_SUCCESS("F0005", "成功添加预收款单据"),

    ADD_ADVANCE_SUCCESS_EN("F0005", "Successfully add advance payment document"),

    ADD_ADVANCE_ERROR("F0504", "系统异常，添加预收款单据失败"),

    ADD_ADVANCE_ERROR_EN("F0504", "System exception, failed to add advance payment document"),

    UPDATE_ADVANCE_SUCCESS("F0006", "成功修改预收款单据"),

    UPDATE_ADVANCE_SUCCESS_EN("F0006", "Successfully modify advance payment document"),

    UPDATE_ADVANCE_ERROR("F0505", "系统异常，更新预收款单据失败"),

    UPDATE_ADVANCE_ERROR_EN("F0505", "System exception, failed to modify advance payment document"),

    DELETE_ADVANCE_SUCCESS("F0007", "成功删除预收款单据"),

    DELETE_ADVANCE_SUCCESS_EN("F0007", "Successfully delete advance payment document"),

    DELETE_ADVANCE_ERROR("F0506", "系统异常，删除预收款单据失败"),

    DELETE_ADVANCE_ERROR_EN("F0506", "System exception, failed to delete advance payment document");

    private final String code;

    private final String msg;

    FinancialCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
