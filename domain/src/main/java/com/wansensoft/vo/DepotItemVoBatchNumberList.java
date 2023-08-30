package com.wansensoft.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepotItemVoBatchNumberList {

    private String id;

    private String barCode;

    private String name;

    private String standard;

    private String model;

    private Long unitId;

    private String commodityUnit;

    private String batchNumber;

    private Date expirationDate;

    private String expirationDateStr;

    private BigDecimal totalNum;
}
