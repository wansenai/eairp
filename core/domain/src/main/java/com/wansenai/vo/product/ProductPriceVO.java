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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long barCode;

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
