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
public enum MenuCodeEnum {

    ADD_MENU_SUCCESS("A0012", "添加菜单成功"),

    ADD_MENU_ERROR("A0215", "添加菜单失败"),

    UPDATE_MENU_SUCCESS("A0013", "修改菜单成功"),

    UPDATE_MENU_ERROR("A0216", "修改菜单失败"),

    DELETE_MENU_SUCCESS("A0014", "删除菜单成功"),

    DELETE_MENU_ERROR("A0217", "删除菜单失败");


    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    MenuCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
