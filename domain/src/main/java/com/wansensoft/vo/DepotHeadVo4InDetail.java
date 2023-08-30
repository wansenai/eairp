package com.wansensoft.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotHeadVo4InDetail {

    private String Number;

    private String barCode;

    private String MName;

    private String Model;

    private String standard;

    private BigDecimal UnitPrice;

    private String mUnit;

    private String newRemark;

    private BigDecimal OperNumber;

    private BigDecimal AllPrice;

    private BigDecimal taxRate;

    private BigDecimal taxMoney;

    private BigDecimal taxLastMoney;

    private String SName;

    private String DName;

    private String OperTime;

    private String NewType;

    private Long tenantId;
}