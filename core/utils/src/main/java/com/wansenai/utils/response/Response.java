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
package com.wansenai.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wansenai.utils.enums.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 793034041048451317L;

    private String msg;

    private String code;

    private T data;

    public static <T> Response<T> success() {
        return responseMsg(BaseCodeEnum.SUCCESS);
    }

    public static <T> Response<T> fail() {
        return responseMsg(BaseCodeEnum.ERROR);
    }

    public static <T> Response<T> responseMsg(BaseCodeEnum baseCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(baseCodeEnum.getCode());
        baseResponse.setMsg(baseCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(UserCodeEnum userCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(userCodeEnum.getCode());
        baseResponse.setMsg(userCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(RoleCodeEnum roleCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(roleCodeEnum.getCode());
        baseResponse.setMsg(roleCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(DeptCodeEnum deptCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(deptCodeEnum.getCode());
        baseResponse.setMsg(deptCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(MenuCodeEnum menuCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(menuCodeEnum.getCode());
        baseResponse.setMsg(menuCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(ProdcutCodeEnum prodcutCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(prodcutCodeEnum.getCode());
        baseResponse.setMsg(prodcutCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(SupplierCodeEnum supplierCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(supplierCodeEnum.getCode());
        baseResponse.setMsg(supplierCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(CustomerCodeEnum customerCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(customerCodeEnum.getCode());
        baseResponse.setMsg(customerCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(MemberCodeEnum memberCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(memberCodeEnum.getCode());
        baseResponse.setMsg(memberCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(WarehouseCodeEnum warehouseCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(warehouseCodeEnum.getCode());
        baseResponse.setMsg(warehouseCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(FinancialCodeEnum financialCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(financialCodeEnum.getCode());
        baseResponse.setMsg(financialCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(OperatorCodeEnum operatorCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(operatorCodeEnum.getCode());
        baseResponse.setMsg(operatorCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(RetailCodeEnum retailCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(retailCodeEnum.getCode());
        baseResponse.setMsg(retailCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(SaleCodeEnum saleCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(saleCodeEnum.getCode());
        baseResponse.setMsg(saleCodeEnum.getMsg());
        return baseResponse;
    }

    public static <T> Response<T> responseMsg(PurchaseCodeEnum purchaseCodeEnum) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(purchaseCodeEnum.getCode());
        baseResponse.setMsg(purchaseCodeEnum.getMsg());
        return baseResponse;
    }


    public static <T> Response<T> responseData(T data) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(BaseCodeEnum.SUCCESS.getCode());
        baseResponse.setData(data);
        return baseResponse;
    }

    public static <T> Response<T> responseData(String code, T data) {
        Response<T> baseResponse = new Response<T>();
        baseResponse.setCode(code);
        baseResponse.setData(data);
        return baseResponse;
    }
}
