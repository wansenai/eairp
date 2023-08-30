package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepotHead {
    private Long id;

    private String type;

    private String subType;

    private String defaultNumber;

    private String number;

    private Date createTime;

    private Date operTime;

    private Long organId;

    private Long creator;

    private Long accountId;

    private BigDecimal changeAmount;

    private BigDecimal backAmount;

    private BigDecimal totalPrice;

    private String payType;

    private String billType;

    private String remark;

    private String fileName;

    private String salesMan;

    private String accountIdList;

    private String accountMoneyList;

    private BigDecimal discount;

    private BigDecimal discountMoney;

    private BigDecimal discountLastMoney;

    private BigDecimal otherMoney;

    private BigDecimal deposit;

    private String status;

    private String purchaseStatus;

    private String source;

    private String linkNumber;

    private Long tenantId;

    private String deleteFlag;
}