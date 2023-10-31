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
public enum DeptCodeEnum {

    ADD_DEPARTMENT_SUCCESS("A0009", "添加部门成功"),

    ADD_DEPARTMENT_ERROR("A0212", "添加部门失败"),

    UPDATE_DEPARTMENT_SUCCESS("A0010", "修改部门成功"),

    UPDATE_DEPARTMENT_ERROR("A0213", "修改部门失败"),

    DELETE_DEPARTMENT_SUCCESS("A0011", "删除部门成功"),

    DELETE_DEPARTMENT_ERROR("A0214", "删除部门失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    DeptCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
