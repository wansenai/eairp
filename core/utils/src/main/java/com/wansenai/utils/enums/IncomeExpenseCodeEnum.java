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

    DELETE_INCOME_EXPENSE_ERROR("I0502", "删除收支项目失败"),

    ADD_INCOME_RECEIPT_SUCCESS("I0004", "新增收入单成功"),

    ADD_INCOME_RECEIPT_ERROR("I0503", "新增收入单失败"),

    UPDATE_INCOME_RECEIPT_SUCCESS("I0005", "更新收入单成功"),

    UPDATE_INCOME_RECEIPT_ERROR("I0504", "更新收入单失败"),

    DELETE_INCOME_RECEIPT_SUCCESS("I0006", "删除收入单成功"),

    DELETE_INCOME_RECEIPT_ERROR("I0505", "删除收入单失败"),

    ADD_EXPENSE_RECEIPT_SUCCESS("E0004", "新增支出单成功"),

    ADD_EXPENSE_RECEIPT_ERROR("E0503", "新增支出单失败"),

    UPDATE_EXPENSE_RECEIPT_SUCCESS("E0005", "更新支出单成功"),

    UPDATE_EXPENSE_RECEIPT_ERROR("E0504", "更新支出单失败"),

    DELETE_EXPENSE_RECEIPT_SUCCESS("E0006", "删除支出单成功"),

    DELETE_EXPENSE_RECEIPT_ERROR("E0505", "删除支出单失败");

    private final String code;

    private final String msg;

    IncomeExpenseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
