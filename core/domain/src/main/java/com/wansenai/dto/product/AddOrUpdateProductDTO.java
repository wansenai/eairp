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
package com.wansenai.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class AddOrUpdateProductDTO {

    private Long productId;

    private String productName;

    private String productStandard;

    private String productModel;

    private String productUnit;

    private Long productUnitId;

    private String productColor;

    private Double productWeight;

    private Integer productExpiryNum;

    private Long productCategoryId;

    private String enableSerialNumber;

    private String enableBatchNumber;

    private String warehouseShelves;

    private String remark;

    // 扩展字段
    private String productManufacturer;

    private String otherFieldOne;

    private String otherFieldTwo;

    private String otherFieldThree;

    // 对应多个商品价格集合
    private List<ProductPriceDTO> priceList;

    // 多仓库库存集合
    private List<ProductStockDTO> stockList;

    // 产品图片集合
    private List<ProductImageDTO> imageList;
}
