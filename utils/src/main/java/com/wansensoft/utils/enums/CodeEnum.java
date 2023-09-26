package com.wansensoft.utils.enums;

public enum CodeEnum {

    // 一级基本宏观状态码
    SUCCESS("A0000", "系统执行成功"),
    ERROR("B0001", "系统执行出错"),
    // 用户类代码
    REGISTER_SUCCESS("A0001", "用户注册成功"),
    USER_EXISTS("A0011", "用户名已存在"),
    USER_LOGOUT("A0012", "账户注销成功"),
    QUERY_DATA_EMPTY("A0404", "查询数据不存在"),
    PARAMETER_NULL("C0001","请求必填参数为空"),
    NOT_PERMISSION("D0000", "没有权限");

    /**
     * 响应状态码
     */
    private String code;

    /**
     * 响应提示
     */
    private String msg;

    CodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
