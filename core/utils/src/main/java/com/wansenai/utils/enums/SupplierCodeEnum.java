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
public enum SupplierCodeEnum {

    ADD_SUPPLIER_SUCCESS("S0001", "供应商添加成功"),

    ADD_SUPPLIER_ERROR("S0500", "供应商添加失败"),

    UPDATE_SUPPLIER_SUCCESS("S0002", "供应商修改成功"),

    UPDATE_SUPPLIER_ERROR("S0501", "供应商修改失败"),

    DELETE_SUPPLIER_SUCCESS("S0003", "供应商删除成功"),

    DELETE_SUPPLIER_ERROR("S0504", "供应商删除失败"),

    UPDATE_SUPPLIER_STATUS_SUCCESS("S0004", "供应商状态修改成功"),

    UPDATE_SUPPLIER_STATUS_ERROR("S0505", "供应商状态修改失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    SupplierCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
