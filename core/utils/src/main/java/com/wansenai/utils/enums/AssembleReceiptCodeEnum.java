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

    ADD_ASSEMBLE_RECEIPT_SUCCESS("A0020", "成功添加组装单据"),

    ADD_ASSEMBLE_RECEIPT_SUCCESS_EN("A0020", "Successfully add assembly documents"),

    ADD_ASSEMBLE_RECEIPT_ERROR("A0520", "系统异常，添加组装单据失败"),

    ADD_ASSEMBLE_RECEIPT_ERROR_EN("A0520", "System exception, failed to add assembly documents"),

    UPDATE_ASSEMBLE_RECEIPT_SUCCESS("A0021", "成功修改组装单据"),

    UPDATE_ASSEMBLE_RECEIPT_SUCCESS_EN("A0021", "Successfully modify assembly documents"),

    UPDATE_ASSEMBLE_RECEIPT_ERROR("A0521", "系统异常，修改组装单据失败"),

    UPDATE_ASSEMBLE_RECEIPT_ERROR_EN("A0521", "System exception, failed to modify assembly documents"),

    DELETE_ASSEMBLE_RECEIPT_SUCCESS("A0022", "成功删除组装单据"),

    DELETE_ASSEMBLE_RECEIPT_SUCCESS_EN("A0022", "Successfully delete assembly documents"),

    DELETE_ASSEMBLE_RECEIPT_ERROR("A0522", "系统异常，删除组装单据失败"),

    DELETE_ASSEMBLE_RECEIPT_ERROR_EN("A0522", "System exception, failed to delete assembly documents");

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
