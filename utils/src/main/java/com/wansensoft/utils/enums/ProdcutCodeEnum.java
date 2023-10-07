package com.wansensoft.utils.enums;

import lombok.Getter;

@Getter
public enum ProdcutCodeEnum {

    ADD_PRODUCT_CATEGORY_SUCCESS("P0000", "添加产品分类成功"),

    ADD_PRODUCT_CATEGORY_ERROR("P0500", "添加产品分类失败"),

    UPDATE_PRODUCT_CATEGORY_SUCCESS("P0001", "修改产品分类成功"),

    UPDATE_PRODUCT_CATEGORY_ERROR("P0501", "修改产品分类失败"),

    DELETE_PRODUCT_CATEGORY_SUCCESS("P0002", "删除产品分类成功"),

    DELETE_PRODUCT_CATEGORY_ERROR("P0502", "删除产品分类失败");

    private final String code;

    /**
     * 响应提示
     */
    private final String msg;

    ProdcutCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
