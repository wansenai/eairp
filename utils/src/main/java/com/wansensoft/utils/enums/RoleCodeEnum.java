package com.wansensoft.utils.enums;

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
