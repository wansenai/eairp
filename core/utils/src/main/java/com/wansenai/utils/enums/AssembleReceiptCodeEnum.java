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
package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum AssembleReceiptCodeEnum {

    ADD_ASSEMBLE_RECEIPT_SUCCESS("A0020", "添加组装单据成功"),

    ADD_ASSEMBLE_RECEIPT_ERROR("A0520", "添加组装单据失败"),

    UPDATE_ASSEMBLE_RECEIPT_SUCCESS("A0021", "修改组装单据成功"),

    UPDATE_ASSEMBLE_RECEIPT_ERROR("A0521", "修改组装单据失败"),

    DELETE_ASSEMBLE_RECEIPT_SUCCESS("A0022", "删除组装单据成功"),

    DELETE_ASSEMBLE_RECEIPT_ERROR("A0522", "删除组装单据失败");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    AssembleReceiptCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
