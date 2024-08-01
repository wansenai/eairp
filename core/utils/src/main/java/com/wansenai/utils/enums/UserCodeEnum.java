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

/**
 * 用户类枚举代码
 */
@Getter
public enum UserCodeEnum {

    // login business
    USER_REGISTER_SUCCESS("A0001", "用户注册成功"),

    USER_REGISTER_SUCCESS_EN("A0001", "Successfully register user"),

    USER_NAME_EXISTS("A0111", "用户名已存在"),

    USER_NAME_EXISTS_EN("A0111", "Username already exists"),

    USER_LOGOUT("A0012", "账户注销成功"),

    USER_LOGOUT_EN("A0012", "Successfully Logout account"),

    USER_NOT_EXISTS("A0201", "用户账户不存在"),

    USER_NOT_EXISTS_EN("A0201", "User account does not exist"),

    USER_PASSWORD_ERROR("A0218", "用户旧密码错误，请检查重新输入"),

    USER_PASSWORD_ERROR_EN("A0218", "User old password is incorrect. Please check and reenter it"),

    USER_REGISTER_PHONE_EXISTS("A0112", "当前手机号已注册，请直接登陆"),

    USER_REGISTER_PHONE_EXISTS_EN("A0112", "Current phone number has been registered, Please login"),

    PHONE_EXISTS("A0113", "当前手机号已被绑定使用, 请更换新的手机号"),

    PHONE_EXISTS_EN("A0113", "Current phone number has been bound for use. Please replace it with a new phone number"),

    USERNAME_OR_PASSWORD_ERROR("A0210", "登录失败，用户名或密码错误"),

    USERNAME_OR_PASSWORD_ERROR_EN("A0210", "Login failed, username or password incorrect"),

    UPDATE_PASSWORD_ERROR("A0211", "系统异常，密码修改失败"),

    UPDATE_PASSWORD_ERROR_EN("A0211", "System exception, failed to modify password"),

    USER_RESET_PASSWORD_ERROR("A0212", "系统异常，重置用户密码失败"),

    USER_RESET_PASSWORD_ERROR_EN("A0212", "System exception, failed to reset user password"),

    UPDATE_PASSWORD_SUCCESS("A0013", "成功修改密码"),

    UPDATE_PASSWORD_SUCCESS_EN("A0013", "Successfully changed password"),

    USER_RESET_PASSWORD_SUCCESS("A0015", "成功重置用户密码"),

    USER_RESET_PASSWORD_SUCCESS_EN("A0015", "Successfully reset user password"),

    USER_ACCOUNT_FREEZE("A0202", "账户被冻结"),

    USER_ACCOUNT_FREEZE_EN("A0202", "Account frozen"),

    USER_ACCOUNT_INVALID("A0203", "账户已作废"),

    USER_ACCOUNT_INVALID_EN("A0203", "Account has been invalidated"),

    EMAIL_EXISTS("A0502", "当前邮箱已被绑定使用"),

    EMAIL_EXISTS_EN("A0502", "Current email has been bound for use"),

    USER_PHONE_UPDATE_SUCCESS("A0016", "成功换绑手机号"),

    USER_PHONE_UPDATE_SUCCESS_EN("A0016", "Successfully changed bound phone number"),

    USER_PHONE_UPDATE_ERROR("A0503", "系统异常，手机号换绑失败"),

    USER_PHONE_UPDATE_ERROR_EN("A0503", "System exception, failed to change phone number"),

    USER_EMAIL_UPDATE_SUCCESS("A0017", "成功换绑邮箱换绑"),

    USER_EMAIL_UPDATE_SUCCESS_EN("A0017", "Successfully changed bound email address"),

    USER_EMAIL_UPDATE_ERROR("A0504", "系统异常，邮箱换绑失败"),

    USER_EMAIL_UPDATE_ERROR_EN("A0504", "System exception, failed to change email"),

    // user list table business
    USER_ADD_SUCCESS("A0002", "成功添加用户"),

    USER_ADD_SUCCESS_EN("A0002", "Successfully add user"),

    USER_INFO_UPDATE_SUCCESS("A0014", "成功修改用户信息"),

    USER_INFO_UPDATE_SUCCESS_EN("A0014", "Successfully modify user info"),

    USER_INFO_UPDATE_ERROR("A0205", "系统异常，改用户资料失败"),

    USER_INFO_UPDATE_ERROR_EN("A0205", "System exception, failed to modify user info"),

    USER_ADD_ERROR("A0204", "系统异常，添加用户失败"),

    USER_ADD_ERROR_EN("A0204", "System exception, failed to add user"),

    USER_DELETE_SUCCESS("A0003", "成功删除用户"),

    USER_DELETE_SUCCESS_EN("A0003", "Successfully delete user"),

    USER_DELETE_ERROR("A0206", "系统异常，删除用户失败"),

    USER_DELETE_ERROR_EN("A0206", "System exception, failed to delete user"),

    // user role
    USER_NOT_PERMISSION("D0000", "用户没有权限"),

    USER_NOT_PERMISSION_EN("D0000", "User does not have permission");

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
