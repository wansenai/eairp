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
public enum TenantCodeEnum {

    TENANT_ADD_SUCCESS("A0300", "租户添加成功"),

    TENANT_INFO_UPDATE_SUCCESS("A0301", "租户资料修改成功"),

    TENANT_INFO_UPDATE_ERROR("A0303", "租户资料修改失败"),

    TENANT_ADD_ERROR("A0302", "租户添加失败"),

    TENANT_USER_NUM_LIMIT("A0304", "租户用户数量已达上限"),

    TENANT_EXPIRED("A0305", "该租户已过期，请联系平台管理员续费"),

    TENANT_UNEXPIRED("A0306", "租户未过期"),

    TENANT_DELETE_SUCCESS("A0307", "租户删除成功"),

    TENANT_DELETE_ERROR("A0308", "租户删除失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    TenantCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
