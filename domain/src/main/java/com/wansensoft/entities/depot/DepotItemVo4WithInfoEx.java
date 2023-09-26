package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotItemVo4WithInfoEx extends DepotItem{

    private Long MId;

    private String MName;

    private String MModel;

    private String MaterialUnit;

    private String MColor;

    private String MStandard;

    private String MMfrs;

    private String MOtherField1;

    private String MOtherField2;

    private String MOtherField3;

    private String enableSerialNumber;

    private String enableBatchNumber;

    private String DepotName;

    private String AnotherDepotName;

    private Long UnitId;

    private String unitName;

    private Integer ratio;

    private String otherUnit;

    private BigDecimal presetPriceOne;

    private String priceStrategy;

    private BigDecimal purchaseDecimal;

    private String barCode;

    private BigDecimal weight;

    private String imgName;
}