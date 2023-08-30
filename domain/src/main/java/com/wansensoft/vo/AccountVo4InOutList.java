package com.wansensoft.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVo4InOutList {

    private Long accountId;

    private String number;

    private String type;

    private String fromType;

    private String supplierName;

    private BigDecimal changeAmount;

    private BigDecimal balance;

    private String operTime;

    private String aList;

    private String amList;

    private Long tenantId;
}