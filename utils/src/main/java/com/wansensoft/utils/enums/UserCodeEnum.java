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

/**
 * 用户类枚举代码
 */
@Getter
public enum UserCodeEnum {

    // login business
    USER_REGISTER_SUCCESS("A0001", "用户注册成功"),

    USER_NAME_EXISTS("A0111", "用户名已存在"),

    USER_LOGOUT("A0012", "账户注销成功"),

    USER_NOT_EXISTS("A0201", "用户账户不存在"),

    USER_REGISTER_PHONE_EXISTS("A0112", "当前手机号已注册，请直接登陆"),

    PHONE_EXISTS("A0113", "当前手机号已存在"),

    USERNAME_OR_PASSWORD_ERROR("A0210", "登录失败，用户名或密码错误"),

    UPDATE_PASSWORD_ERROR("A0211", "系统错误，密码修改失败"),

    USER_RESET_PASSWORD_ERROR("A0212", "系统错误，用户重置密码失败"),

    UPDATE_PASSWORD_SUCCESS("A0013", "密码修改成功"),

    USER_RESET_PASSWORD_SUCCESS("A0015", "用户重置密码成功"),

    USER_ACCOUNT_FREEZE("A0202", "用户账户被冻结"),

    USER_ACCOUNT_INVALID("A0203", "用户账户已作废"),

    // user list table business
    USER_ADD_SUCCESS("A0002", "用户添加成功"),

    USER_INFO_UPDATE_SUCCESS("A0014", "用户资料修改成功"),

    USER_INFO_UPDATE_ERROR("A0205", "用户资料修改失败"),

    USER_ADD_ERROR("A0204", "用户添加失败"),

    USER_DELETE_SUCCESS("A0003", "用户删除成功"),

    USER_DELETE_ERROR("A0206", "用户删除失败"),

    // user role
    USER_NOT_PERMISSION("D0000", "用户没有权限");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    UserCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
