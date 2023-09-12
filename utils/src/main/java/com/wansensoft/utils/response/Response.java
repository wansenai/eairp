package com.wansensoft.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wansensoft.utils.enums.CodeEnum;

import java.io.Serial;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 793034041048451317L;

    private String msg;

    private String code;

    private T data;

    public static <T> Response<T> success() {
        return responseMsg(CodeEnum.SUCCESS);
    }

    public static <T> Response<T> fail() {
        return responseMsg(CodeEnum.ERROR);
    }

    public static <T> Response<T> responseMsg(CodeEnum codeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(codeEnum.getCode());
        baseResponse.setMsg(codeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(String code, String msg) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(code);
        baseResponse.setMsg(msg);
        return baseResponse;
    }

    public static <T> Response<T> responseData(T data) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(CodeEnum.SUCCESS.getCode());
        baseResponse.setData(data);
        return baseResponse;
    }

    public static <T> Response<T> responseData(String code, T data) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(code);
        baseResponse.setData(data);
        return baseResponse;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
