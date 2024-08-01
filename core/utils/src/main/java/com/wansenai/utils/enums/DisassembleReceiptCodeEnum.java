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
public enum DisassembleReceiptCodeEnum {

    ADD_DISASSEMBLE_RECEIPT_SUCCESS("D0001", "成功添加拆卸单据"),

    ADD_DISASSEMBLE_RECEIPT_SUCCESS_EN("D0001", "Successfully add disassembly documents"),

    ADD_DISASSEMBLE_RECEIPT_ERROR("D0500", "系统异常，添加拆卸单据失败"),

    ADD_DISASSEMBLE_RECEIPT_ERROR_EN("D0500", "System exception, failed to add disassembly documents"),

    UPDATE_DISASSEMBLE_RECEIPT_SUCCESS("D0002", "成功修改拆卸单据"),

    UPDATE_DISASSEMBLE_RECEIPT_SUCCESS_EN("D0002", "Successfully modify disassembly documents"),

    UPDATE_DISASSEMBLE_RECEIPT_ERROR("D0501", "系统异常，修改拆卸单据失败"),

    UPDATE_DISASSEMBLE_RECEIPT_ERROR_EN("D0501", "System exception, failed to modify disassembly documents"),

    DELETE_DISASSEMBLE_RECEIPT_SUCCESS("D0003", "成功删除拆卸单据"),

    DELETE_DISASSEMBLE_RECEIPT_SUCCESS_EN("D0003", "Successfully delete disassembly documents"),

    DELETE_DISASSEMBLE_RECEIPT_ERROR("D0502", "系统异常，删除拆卸单据失败"),

    DELETE_DISASSEMBLE_RECEIPT_ERROR_EN("D0502", "System exception, failed to delete disassembly documents");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    DisassembleReceiptCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
