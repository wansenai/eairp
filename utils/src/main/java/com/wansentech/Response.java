/*
 * Copyright 2023 wansentech.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansentech;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wansentech.enums.CodeEnum;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private String msg;

    private String code;

    private T data;

    public static <T> Response<T> success() {
        return response(CodeEnum.SUCCESS);
    }

    public static <T> Response<T> error() {
        return response(CodeEnum.ERROR);
    }

    public static <T> Response<T> response(CodeEnum codeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(codeEnum.getCode());
        baseResponse.setMsg(codeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> response(String code, String msg) {
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
