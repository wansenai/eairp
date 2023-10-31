/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansenai.utils.enums;

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

    UPDATE_ACCOUNT_STATUS_ERROR("F0503", "修改账号状态失败"),

    ADD_ADVANCE_SUCCESS("F0005", "新增预收款成功"),

    ADD_ADVANCE_ERROR("F0504", "新增预收款失败"),

    UPDATE_ADVANCE_SUCCESS("F0006", "更新预收款数据成功"),

    UPDATE_ADVANCE_ERROR("F0505", "更新预收款数据失败"),

    DELETE_ADVANCE_SUCCESS("F0007", "删除预收款数据成功"),

    DELETE_ADVANCE_ERROR("F0506", "删除预收款数据失败");

    private final String code;

    private final String msg;

    FinancialCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
