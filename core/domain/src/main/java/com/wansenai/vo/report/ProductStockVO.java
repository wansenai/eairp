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
package com.wansenai.vo.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductStockVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long warehouseId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productBarcode;

    private String warehouseName;

    private String productName;

    private String productCategoryName;

    private String productStandard;

    private String productModel;

    private String productColor;

    private String productUnit;

    private String warehouseShelves;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal productWeight;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal unitPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal initialStock;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal currentStock;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal stockAmount;
}
