package com.wansensoft.utils.enums;

import lombok.Getter;

@Getter
public enum RoleCodeEnum {

    UPDATE_ROLE_STATUS_SUCCESS("A0005", "角色状态修改成功"),

    UPDATE_ROLE_STATUS_ERROR("A0208", "角色状态修改失败");

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
