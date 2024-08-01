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

    ADD_INCOME_EXPENSE_SUCCESS("I0001", "成功新增收支项目"),

    ADD_INCOME_EXPENSE_SUCCESS_EN("I0001", "Successfully add income/expense item"),

    ADD_INCOME_EXPENSE_ERROR("I0500", "系统异常，新增收支项目失败"),

    ADD_INCOME_EXPENSE_ERROR_EN("I0500", "System exception, failed to add income/expense item"),

    UPDATE_INCOME_EXPENSE_SUCCESS("I0002", "成功更新收支项目"),

    UPDATE_INCOME_EXPENSE_SUCCESS_EN("I0002", "Successfully modify income/expense item"),

    UPDATE_INCOME_EXPENSE_ERROR("I0501", "系统异常，更新收支项目失败"),

    UPDATE_INCOME_EXPENSE_ERROR_EN("I0501", "System exception, failed to modify income/expense item"),

    DELETE_INCOME_EXPENSE_SUCCESS("I0003", "成功删除收支项目"),

    DELETE_INCOME_EXPENSE_SUCCESS_EN("I0003", "Successfully delete income/expense item"),

    DELETE_INCOME_EXPENSE_ERROR("I0502", "系统异常，删除收支项目失败"),

    DELETE_INCOME_EXPENSE_ERROR_EN("I0502", "System exception, failed to delete income/expense item"),

    ADD_INCOME_RECEIPT_SUCCESS("I0004", "成功新增收入单据"),

    ADD_INCOME_RECEIPT_SUCCESS_EN("I0004", "Successfully add income receipt"),

    ADD_INCOME_RECEIPT_ERROR("I0503", "系统异常，新增收入单失败"),

    ADD_INCOME_RECEIPT_ERROR_EN("I0503", "System exception, failed to add income receipt"),

    UPDATE_INCOME_RECEIPT_SUCCESS("I0005", "成功更新收入单据"),

    UPDATE_INCOME_RECEIPT_SUCCESS_EN("I0005", "Successfully modify income receipt"),

    UPDATE_INCOME_RECEIPT_ERROR("I0504", "系统异常，更新收入单失败"),

    UPDATE_INCOME_RECEIPT_ERROR_EN("I0504", "System exception, failed to modify income receipt"),

    DELETE_INCOME_RECEIPT_SUCCESS("I0006", "成功删除收入单据"),

    DELETE_INCOME_RECEIPT_SUCCESS_EN("I0006", "Successfully delete income receipt"),

    DELETE_INCOME_RECEIPT_ERROR("I0505", "系统异常，删除收入单据失败"),

    DELETE_INCOME_RECEIPT_ERROR_EN("I0505", "System exception, failed to delete income receipt"),

    ADD_EXPENSE_RECEIPT_SUCCESS("E0004", "成功新增支出单据"),

    ADD_EXPENSE_RECEIPT_SUCCESS_EN("E0004", "Successfully add expense receipt"),

    ADD_EXPENSE_RECEIPT_ERROR("E0503", "系统异常，新增支出单据失败"),

    ADD_EXPENSE_RECEIPT_ERROR_EN("E0503", "System exception, failed to add expense receipt"),

    UPDATE_EXPENSE_RECEIPT_SUCCESS("E0005", "成功更新支出单据"),

    UPDATE_EXPENSE_RECEIPT_SUCCESS_EN("E0005", "Successfully modify expense receipt"),

    UPDATE_EXPENSE_RECEIPT_ERROR("E0504", "系统异常，更新支出单据失败"),

    UPDATE_EXPENSE_RECEIPT_ERROR_EN("E0504", "System exception, failed to modify expense receipt"),

    DELETE_EXPENSE_RECEIPT_SUCCESS("E0006", "成功删除支出单据"),

    DELETE_EXPENSE_RECEIPT_SUCCESS_EN("E0006", "Successfully delete expense receipt"),

    DELETE_EXPENSE_RECEIPT_ERROR("E0505", "系统异常，删除支出单据失败"),

    DELETE_EXPENSE_RECEIPT_ERROR_EN("E0505", "System exception, failed to delete expense receipt");

    private final String code;

    private final String msg;

    IncomeExpenseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
