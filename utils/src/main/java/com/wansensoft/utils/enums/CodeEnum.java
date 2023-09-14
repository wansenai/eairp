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
 * <p>
 *  响应状态枚举
 * </p>
 */
@Getter
public enum CodeEnum {

    // 一级基本宏观状态码
    SUCCESS("00000", "系统执行成功"),
    ERROR("B0001", "系统执行出错"),
    // 用户类代码
    REGISTER_SUCCESS("A0001", "用户注册成功"),
    USER_EXISTS("A0011", "用户名已存在"),
    USER_LOGOUT("A0012", "账户注销成功"),
    USERNAME_OR_PASSWORD_ERROR("A0210", "登录失败，用户名或密码错误"),
    QUERY_DATA_EMPTY("A0404", "查询数据不存在"),
    PARAMETER_NULL("A0410","请求必填参数为空"),
    NOT_PERMISSION("D0000", "没有权限"),
    VERIFY_CODE_ERROR("A0240", "用户验证码错误"),

    VERIFY_CODE_EXPIRE("A0242", "验证码已过期");

    /**
     * 响应状态码
     */
    private String code;

    /**
     * 响应提示
     */
    private String msg;

    CodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
