package com.wansensoft.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotItemStockWarningCount {

    private Long MId;

    private String barCode;

    private String MName;

    private String MModel;

    private String MaterialUnit;

    private String MColor;

    private String MStandard;

    private String MMfrs;

    private String unitName;

    private String MaterialOther;

    private String MOtherField1;

    private String MOtherField2;

    private String MOtherField3;

    private String depotName;

    private BigDecimal currentNumber;

    private BigDecimal lowSafeStock;

    private BigDecimal highSafeStock;

    private BigDecimal lowCritical;

    private BigDecimal highCritical;
}
