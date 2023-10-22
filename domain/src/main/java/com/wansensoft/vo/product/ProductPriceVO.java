package com.wansensoft.vo.product;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansensoft.bo.BigDecimalSerializerBO;
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
