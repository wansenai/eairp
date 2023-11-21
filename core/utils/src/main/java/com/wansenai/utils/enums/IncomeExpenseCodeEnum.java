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
public enum IncomeExpenseCodeEnum {

    ADD_INCOME_EXPENSE_SUCCESS("I0001", "新增收支项目成功"),

    ADD_INCOME_EXPENSE_ERROR("I0500", "新增收支项目失败"),

    UPDATE_INCOME_EXPENSE_SUCCESS("I0002", "更新收支项目成功"),

    UPDATE_INCOME_EXPENSE_ERROR("I0501", "更新收支项目失败"),

    DELETE_INCOME_EXPENSE_SUCCESS("I0003", "删除收支项目成功"),

    DELETE_INCOME_EXPENSE_ERROR("I0502", "删除收支项目失败");

    private final String code;

    private final String msg;

    IncomeExpenseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
