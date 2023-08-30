package com.wansensoft.entities.account;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountHead {
    private Long id;

    private String type;

    private Long organId;

    private Long handsPersonId;

    private Long creator;

    private BigDecimal changeAmount;

    private BigDecimal discountMoney;

    private BigDecimal totalPrice;

    private Long accountId;

    private String billNo;

    private Date billTime;

    private String remark;

    private String fileName;

    private String status;

    private String source;

    private Long tenantId;

    private String deleteFlag;
}