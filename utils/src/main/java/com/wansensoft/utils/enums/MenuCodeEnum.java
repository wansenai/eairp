package com.wansensoft.utils.enums;

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
