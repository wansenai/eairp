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
package com.wansenai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoVO {

    /** 用户id */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /** 用户昵称（别名 姓名） */
    private String name;

    /** 职位 */
    private String position;

    /** 邮箱 */
    private String email;

    /** 电话号 */
    private String phoneNumber;

    /** 用户个人简介 */
    private String description;

    /** 用户名（登陆的账户） */
    private String userName;

    /** 用户头像地址 */
    private String avatar;

    /** 用户语言 */
    private String systemLanguage;

    /** 用户token */
    private String token;

    /** 过期 */
    private long expire;
}
