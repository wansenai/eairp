package com.wansensoft.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotHeadVo4InOutMCount {

    private Long MaterialId;

    private String barCode;

    private String mName;

    private String Model;

    private String standard;

    private String categoryName;

    private String materialUnit;

    private BigDecimal numSum;

    private BigDecimal priceSum;

    private Long tenantId;
}