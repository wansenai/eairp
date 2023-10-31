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
public enum OperatorCodeEnum {

    ADD_OPERATOR_SUCCESS("O0001", "经手人添加成功"),

    ADD_OPERATOR_ERROR("O0500", "经手人添加失败"),

    UPDATE_OPERATOR_SUCCESS("O0002", "经手人修改成功"),

    UPDATE_OPERATOR_ERROR("O0501", "经手人修改失败"),

    DELETE_OPERATOR_SUCCESS("O0003", "经手人删除成功"),

    DELETE_OPERATOR_ERROR("O0502", "经手人删除失败"),

    UPDATE_OPERATOR_STATUS_SUCCESS("O0004", "经手人状态修改成功"),

    UPDATE_OPERATOR_STATUS_ERROR("O0503", "经手人状态修改失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    OperatorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
