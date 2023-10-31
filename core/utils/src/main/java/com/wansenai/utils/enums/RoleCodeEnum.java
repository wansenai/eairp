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
public enum RoleCodeEnum {

    ADD_ROLE_SUCCESS("A0004", "添加角色成功"),

    ADD_ROLE_ERROR("A0207", "添加角色失败"),

    UPDATE_ROLE_STATUS_SUCCESS("A0005", "角色状态修改成功"),

    UPDATE_ROLE_STATUS_ERROR("A0208", "角色状态修改失败"),

    UPDATE_ROLE_SUCCESS("A0006", "修改角色资料成功"),

    UPDATE_ROLE_ERROR("A0209", "修改角色资料失败"),

    DELETE_ROLE_SUCCESS("A0007", "删除角色成功"),

    DELETE_ROLE_ERROR("A0210", "删除角色失败"),

    ROLE_PERMISSION_MENU_SUCCESS("A0008", "赋予角色菜单权限成功"),

    ROLE_PERMISSION_MENU_ERROR("A0211", "赋予角色菜单权限失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    RoleCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
