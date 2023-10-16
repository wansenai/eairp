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
package com.wansensoft.utils.enums;

import lombok.Getter;

@Getter
public enum FinancialCodeEnum {

    ADD_ACCOUNT_SUCCESS("F0000", "新增账户成功"),

    ADD_ACCOUNT_ERROR("F0500", "新增账户失败"),

    UPDATE_ACCOUNT_SUCCESS("F0002", "更新账户成功"),

    UPDATE_ACCOUNT_ERROR("F0501", "更新账户失败"),

    DELETE_ACCOUNT_SUCCESS("F0003", "删除账户成功"),

    DELETE_ACCOUNT_ERROR("F0502", "删除账户失败"),

    UPDATE_ACCOUNT_STATUS_SUCCESS("F0004", "修改账号状态成功"),

    UPDATE_ACCOUNT_STATUS_ERROR("F0503", "修改账号状态失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    FinancialCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
