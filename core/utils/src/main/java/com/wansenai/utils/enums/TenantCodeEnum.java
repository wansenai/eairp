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

    TENANT_ADD_SUCCESS("A0300", "成功添加租户"),

    TENANT_ADD_SUCCESS_EN("A0300", "Successfully add tenant"),

    TENANT_INFO_UPDATE_SUCCESS("A0301", "成功修改租户资料"),

    TENANT_INFO_UPDATE_SUCCESS_EN("A0301", "Successfully modify tenant info"),

    TENANT_INFO_UPDATE_ERROR("A0303", "系统异常，租户资料修改失败"),

    TENANT_INFO_UPDATE_ERROR_EN("A0303", "System exception, failed to modify tenant info"),

    TENANT_ADD_ERROR("A0302", "系统异常，租户添加失败"),

    TENANT_ADD_ERROR_EN("A0302", "System exception, failed to add tenant"),

    TENANT_USER_NUM_LIMIT("A0304", "当前租户已达到注册用户的最大数量"),

    TENANT_USER_NUM_LIMIT_EN("A0304", "The current tenant has reached the maximum number of registered users"),

    TENANT_EXPIRED("A0305", "当前租户已过期，请联系平台管理员续费"),

    TENANT_EXPIRED_EN("A0305", "The current tenant has expired. Please contact the platform administrator to renew"),

    TENANT_UNEXPIRED("A0306", "租户未过期"),

    TENANT_UNEXPIRED_EN("A0306", "Tenant not expired"),

    TENANT_DELETE_SUCCESS("A0307", "成功删除租户"),

    TENANT_DELETE_SUCCESS_EN("A0307", "Successfully delete tenant"),

    TENANT_DELETE_ERROR("A0308", "系统异常，租户删除失败"),

    TENANT_DELETE_ERROR_EN("A0308", "System exception, failed to delete tenant");

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
