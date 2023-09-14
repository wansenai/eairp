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
