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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productPriceId;

    private Integer barCode;

    private String productUnit;

    private String multiAttribute;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal purchasePrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal retailPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal salesPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal lowSalesPrice;
}
