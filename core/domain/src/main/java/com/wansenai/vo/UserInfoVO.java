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

    /** 用户名（登陆的账户） */
    private String userName;

    /** 用户头像地址 */
    private String avatar;

    /** 用户token */
    private String token;

    /** 过期 */
    private long expire;
}
