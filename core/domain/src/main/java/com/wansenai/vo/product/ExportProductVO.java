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
