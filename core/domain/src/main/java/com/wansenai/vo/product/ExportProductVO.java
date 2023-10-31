/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wansenai.vo.product;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExportProductVO {

    private String productName;

    private String productStandard;

    private String productModel;

    private String productColor;

    private String productCategoryName;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal productWeight;

    private Integer productExpiryNum;

    private String productUnit;

    private Integer productBarcode;

    private String multiAttribute;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal purchasePrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal retailPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal salesPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal lowSalesPrice;

    private String warehouseShelves;

    private String productManufacturer;

    private String enableSerialNumber;

    private String enableBatchNumber;

    private String otherFieldOne;

    private String otherFieldTwo;

    private String otherFieldThree;

    private String remark;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal stock;
}
