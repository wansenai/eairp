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

import com.wansensoft.utils.response.Response;
import lombok.Getter;

/**
 * <p>
 *  通用响应状态枚举
 * </p>
 */
@Getter
public enum BaseCodeEnum {

    // 一级基本宏观状态码
    SUCCESS("00000", "系统执行成功"),

    ERROR("B0001", "系统执行出错"),

    QUERY_DATA_EMPTY("A0404", "查询数据不存在"),

    PARAMETER_NULL("A0410","请求必填参数为空"),

    VERIFY_CODE_ERROR("A0240", "验证码错误"),

    VERIFY_CODE_EXPIRE("A0242", "验证码已过期"),

    SMS_VERIFY_CODE_EXPIRE("A0243", "短信校验码已过期"),

    SMS_VERIFY_SEND_SUCCESS("A0002", "短信验证码发送成功"),

    SMS_VERIFY_CODE_ERROR("A0131", "短信校验码错误"),

    PHONE_NUMBER_FORMAT_ERROR("A0131", "手机格式校验失败"),

    FILE_UPLOAD_ERROR("A0500", "文件上传失败"),

    FILE_UPLOAD_NO_FILENAME_MATCH("A0501", "文件上传失败，文件名不匹配");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    BaseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
