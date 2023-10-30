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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productCategoryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productUnitId;

    private Integer productBarcode;

    private String productName;

    private String productStandard;

    private String productModel;

    private String productColor;

    private String productCategoryName;

    private String extendInfo;

    private String productUnit;

    private BigDecimal productStock;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal purchasePrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal retailPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal salePrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal lowPrice;

    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
