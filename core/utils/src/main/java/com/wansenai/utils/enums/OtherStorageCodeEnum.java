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
public enum OtherStorageCodeEnum {

    ADD_OTHER_STORAGE_STOCK_SUCCESS("S0016", "成功添加入库单据"),

    ADD_OTHER_STORAGE_STOCK_SUCCESS_EN("S0016", "Successfully add warehousing document"),

    ADD_OTHER_STORAGE_STOCK_ERROR("S0515", "系统异常，添加入库单据失败"),

    ADD_OTHER_STORAGE_STOCK_ERROR_EN("S0515", "System exception, failed to add warehousing document"),

    UPDATE_OTHER_STORAGE_STOCK_SUCCESS("S0017", "成功修改入库单据"),

    UPDATE_OTHER_STORAGE_STOCK_SUCCESS_EN("S0017", "Successfully modify warehousing document"),

    UPDATE_OTHER_STORAGE_STOCK_ERROR("S0516", "系统异常，修改入库单据失败"),

    UPDATE_OTHER_STORAGE_STOCK_ERROR_EN("S0516", "System exception, failed to modify warehousing document"),

    DELETE_OTHER_STORAGE_STOCK_SUCCESS("S0018", "成功删除入库单据"),

    DELETE_OTHER_STORAGE_STOCK_SUCCESS_EN("S0018", "Successfully delete warehousing document"),

    DELETE_OTHER_STORAGE_STOCK_ERROR("S0517", "系统异常，删除入库单据失败"),

    DELETE_OTHER_STORAGE_STOCK_ERROR_EN("S0517", "System exception, failed to delete warehousing document");

    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    OtherStorageCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
