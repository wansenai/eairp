package com.wansensoft.vo.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansensoft.bo.BigDecimalSerializerBO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productCategoryId;

    private String productName;

    private String productStandard;

    private String productModel;

    private String productColor;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal productWeight;

    private Integer productExpiryNum;

    private String productCategoryName;

    private Integer enableSerialNumber;

    private Integer enableBatchNumber;

    private String warehouseShelves;

    private List<ProductPriceVO> priceList;

    // 扩展信息
    private String productManufacturer;

    private String otherFieldOne;

    private String otherFieldTwo;

    private String otherFieldThree;

    // 库存信息
    private List<ProductStockVO> stockList;

    // 图片信息
    private List<ProductImageVO> imageList;
}
