package com.wansenai.utils.enums;

import lombok.Getter;

@Getter
public enum ProdcutCodeEnum {

    ADD_PRODUCT_CATEGORY_SUCCESS("P0000", "成功添加商品分类"),

    ADD_PRODUCT_CATEGORY_SUCCESS_EN("P0000", "Successfully add product category"),

    ADD_PRODUCT_CATEGORY_ERROR("P0500", "系统异常，添加商品分类失败"),

    ADD_PRODUCT_CATEGORY_ERROR_EN("P0500", "System exception, adding product category failed"),

    UPDATE_PRODUCT_CATEGORY_SUCCESS("P0001", "修改商品分类成功"),

    UPDATE_PRODUCT_CATEGORY_SUCCESS_EN("P0001", "Successfully modify product category"),

    UPDATE_PRODUCT_CATEGORY_ERROR("P0501", "系统异常，修改商品分类失败"),

    UPDATE_PRODUCT_CATEGORY_ERROR_EN("P0501", "System exception, failed to modify product category"),

    DELETE_PRODUCT_CATEGORY_SUCCESS("P0002", "成功删除商品分类"),

    DELETE_PRODUCT_CATEGORY_SUCCESS_EN("P0002", "Successfully delete product category"),

    DELETE_PRODUCT_CATEGORY_ERROR("P0502", "系统异常，删除商品分类失败"),

    DELETE_PRODUCT_CATEGORY_ERROR_EN("P0502", "System exception, failed to delete product category"),

    // Product Attribute Code
    PRODUCT_ATTRIBUTE_NAME_EXIST("P0506", "商品属性名称已存在"),

    PRODUCT_ATTRIBUTE_NAME_EXIST_EN("P0506", "Product attribute name already exists"),

    ADD_PRODUCT_ATTRIBUTE_SUCCESS("P0003", "成功添加商品属性"),

    ADD_PRODUCT_ATTRIBUTE_SUCCESS_EN("P0003", "Successfully add product attributes"),

    ADD_PRODUCT_ATTRIBUTE_ERROR("P0503", "系统异常，添加商品属性失败"),

    ADD_PRODUCT_ATTRIBUTE_ERROR_EN("P0503", "System exception, failed to add product attributes"),

    UPDATE_PRODUCT_ATTRIBUTE_SUCCESS("P0004", "成功修改商品属性"),

    UPDATE_PRODUCT_ATTRIBUTE_SUCCESS_EN("P0004", "Successfully modify product attributes"),

    UPDATE_PRODUCT_ATTRIBUTE_ERROR("P0504", "系统异常，修改商品属性失败"),

    UPDATE_PRODUCT_ATTRIBUTE_ERROR_EN("P0504", "System exception, failed to modify product attributes"),

    DELETE_PRODUCT_ATTRIBUTE_SUCCESS("P0005", "成功删除商品属性"),

    DELETE_PRODUCT_ATTRIBUTE_SUCCESS_EN("P0005", "Successfully delete product attributes"),

    DELETE_PRODUCT_ATTRIBUTE_ERROR("P0505", "系统异常，删除商品属性失败"),

    DELETE_PRODUCT_ATTRIBUTE_ERROR_EN("P0505", "System exception, failed to delete product attributes"),

    // Product Unit Code
    PRODUCT_COMPUTE_UNIT_EXIST("P0507", "商品计量单位已存在"),

    PRODUCT_COMPUTE_UNIT_EXIST_EN("P0507", "Product measurement unit already exists"),

    PRODUCT_UNIT_ADD_SUCCESS("P0006", "成功添加商品计量单位"),

    PRODUCT_UNIT_ADD_SUCCESS_EN("P0006", "Successfully add product measurement unit"),

    PRODUCT_UNIT_ADD_ERROR("P0506", "系统异常，添加商品计量单位失败"),

    PRODUCT_UNIT_ADD_ERROR_EN("P0506", "System exception, failed to add product measurement unit"),

    PRODUCT_UNIT_UPDATE_SUCCESS("P0007", "成功修改商品计量单位"),

    PRODUCT_UNIT_UPDATE_SUCCESS_EN("P0007", "Successfully modify product measurement unit"),

    PRODUCT_UNIT_UPDATE_ERROR("P0507", "系统异常，修改商品计量单位失败"),

    PRODUCT_UNIT_UPDATE_ERROR_EN("P0507", "System exception, failed to modify product measurement unit"),

    PRODUCT_UNIT_DELETE_SUCCESS("P0008", "成功删除商品计量单位"),

    PRODUCT_UNIT_DELETE_SUCCESS_EN("P0008", "Successfully delete product measurement unit"),

    PRODUCT_UNIT_DELETE_ERROR("P0508", "系统异常，删除商品计量单位失败"),

    PRODUCT_UNIT_DELETE_ERROR_EN("P0508", "System exception, failed to delete product measurement unit"),

    UPDATE_PRODUCT_UNIT_STATUS_SUCCESS("P0009", "成功修改商品计量单位状态"),

    UPDATE_PRODUCT_UNIT_STATUS_SUCCESS_EN("P0009", "Successfully modify product measurement unit status"),

    UPDATE_PRODUCT_UNIT_STATUS_ERROR("P0509", "系统异常，修改商品计量单位状态失败"),

    UPDATE_PRODUCT_UNIT_STATUS_ERROR_EN("P0509", "System exception, failed to modify product measurement unit status"),

    // Product code
    PRODUCT_NAME_EXIST("P0510", "商品名称已存在，请重新输入"),

    PRODUCT_NAME_EXIST_EN("P0510", "Product name already exists, please reenter"),

    PRODUCT_BAR_CODE_EXIST("P0511", "商品条码已存在，请重新输入"),

    PRODUCT_BAR_CODE_EXIST_EN("P0511", "Product barcode already exists, please reenter"),

    PRODUCT_ADD_SUCCESS("P0010", "成功添加商品"),

    PRODUCT_ADD_SUCCESS_EN("P0010", "Successfully add product"),

    PRODUCT_ADD_ERROR("P0512", "系统异常，添加商品信息失败"),

    PRODUCT_ADD_ERROR_EN("P0512", "System exception, failed to add product information"),

    PRODUCT_UPDATE_SUCCESS("P0011", "成功修改商品信息"),

    PRODUCT_UPDATE_SUCCESS_EN("P0011", "Successfully modify product information"),

    PRODUCT_UPDATE_ERROR("P0513", "系统异常，修改商品失败"),

    PRODUCT_UPDATE_ERROR_EN("P0513", "System exception, failed to modify product information"),

    PRODUCT_DELETE_SUCCESS("P0012", "成功删除商品"),

    PRODUCT_DELETE_SUCCESS_EN("P0012", "Successfully delete product"),

    PRODUCT_DELETE_ERROR("P0514", "系统异常，删除商品失败"),

    PRODUCT_DELETE_ERROR_EN("P0514", "System exception, failed to delete product"),

    PRODUCT_STATUS_UPDATE_SUCCESS("P0013", "成功修改商品状态"),

    PRODUCT_STATUS_UPDATE_SUCCESS_EN("P0013", "Successfully modify product status"),

    PRODUCT_STATUS_UPDATE_ERROR("P0515", "系统异常，修改商品状态失败"),

    PRODUCT_STATUS_UPDATE_ERROR_EN("P0515", "System exception, failed to modify product status"),

    PRODUCT_BATCH_UPDATE_SUCCESS("P0014", "成功批量修改商品信息"),

    PRODUCT_BATCH_UPDATE_SUCCESS_EN("P0014", "Successfully batch modify product information"),

    PRODUCT_BATCH_UPDATE_ERROR("P0516", "系统异常，批量修改商品信息失败"),

    PRODUCT_BATCH_UPDATE_ERROR_EN("P0516", "System exception, failed to batch modify product information"),

    PRODUCT_BAR_CODE_NOT_DUPLICATED("P0517", "商品条码不能重复，请重新输入"),

    PRODUCT_BAR_CODE_NOT_DUPLICATED_EN("P0517", "Product barcode cannot be repeated, please reenter");

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
