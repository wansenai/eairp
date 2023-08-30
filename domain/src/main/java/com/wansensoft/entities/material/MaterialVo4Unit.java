package com.wansensoft.entities.material;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialVo4Unit extends Material{

    private String unitName;

    private BigDecimal ratio;

    private String categoryName;

    private String materialOther;

    private BigDecimal stock;

    private BigDecimal purchaseDecimal;

    private BigDecimal commodityDecimal;

    private BigDecimal wholesaleDecimal;

    private BigDecimal lowDecimal;

    private BigDecimal billPrice;

    private String mBarCode;

    private String commodityUnit;

    private Long meId;

    private BigDecimal initialStock;

    private BigDecimal currentStock;

    private BigDecimal currentStockPrice;

    private BigDecimal currentWeight;

    private String sku;

    private Long depotId;

    /**
     * 换算为大单位的库存
     */
    private String bigUnitStock;

    private String imgSmall;

    private String imgLarge;
}