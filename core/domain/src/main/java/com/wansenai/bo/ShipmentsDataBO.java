package com.wansenai.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShipmentsDataBO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long warehouseId;

    private String barcode;

    private Long productId;

    private Integer productNumber;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal unitPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal amount;

    private String remark;
}
