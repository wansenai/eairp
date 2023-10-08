package com.wansensoft.utils.enums;

import lombok.Getter;

@Getter
public enum ProdcutCodeEnum {

    ADD_PRODUCT_CATEGORY_SUCCESS("P0000", "添加商品分类成功"),

    ADD_PRODUCT_CATEGORY_ERROR("P0500", "添加商品分类失败"),

    UPDATE_PRODUCT_CATEGORY_SUCCESS("P0001", "修改商品分类成功"),

    UPDATE_PRODUCT_CATEGORY_ERROR("P0501", "修改商品分类失败"),

    DELETE_PRODUCT_CATEGORY_SUCCESS("P0002", "删除商品分类成功"),

    DELETE_PRODUCT_CATEGORY_ERROR("P0502", "删除商品分类失败"),

    // Product Attribute Code
    PRODUCT_ATTRIBUTE_NAME_EXIST("P0506", "商品属性名称已存在"),

    ADD_PRODUCT_ATTRIBUTE_SUCCESS("P0003", "添加商品属性成功"),

    ADD_PRODUCT_ATTRIBUTE_ERROR("P0503", "添加商品属性失败"),

    UPDATE_PRODUCT_ATTRIBUTE_SUCCESS("P0004", "修改商品属性成功"),

    UPDATE_PRODUCT_ATTRIBUTE_ERROR("P0504", "修改商品属性失败"),

    DELETE_PRODUCT_ATTRIBUTE_SUCCESS("P0005", "删除商品属性成功"),

    DELETE_PRODUCT_ATTRIBUTE_ERROR("P0505", "删除商品属性失败");


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
