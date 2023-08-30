package com.wansensoft.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotHeadVo4StatementAccount {

    private Long id;

    private String supplier;

    private String contacts;

    private String telephone;

    private String phoneNum;

    private String email;

    /**
     * 起始期初金额
     */
    private BigDecimal beginNeed;

    /**
     * 上期欠款金额
     */
    private BigDecimal preDebtMoney;

    /**
     * 上期退货的欠款金额
     */
    private BigDecimal preReturnDebtMoney;


    /**
     * 上期收付款
     */
    private BigDecimal preBackMoney;

    /**
     * 期初应收
     */
    private BigDecimal preNeed;

    /**
     * 本期欠款
     */
    private BigDecimal debtMoney;

    /**
     * 本期退货的欠款金额
     */
    private BigDecimal returnDebtMoney;

    /**
     * 本期收付款
     */
    private BigDecimal backMoney;

    /**
     * 期末应收
     */
    private BigDecimal allNeed;
}