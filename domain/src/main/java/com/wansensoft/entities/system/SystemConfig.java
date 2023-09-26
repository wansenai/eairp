package com.wansensoft.entities.system;

import lombok.Data;

@Data
public class SystemConfig {
    private Long id;

    private String companyName;

    private String companyContacts;

    private String companyAddress;

    private String companyTel;

    private String companyFax;

    private String companyPostCode;

    private String saleAgreement;

    private String depotFlag;

    private String customerFlag;

    private String minusStockFlag;

    private String purchaseBySaleFlag;

    private String multiLevelApprovalFlag;

    private String multiBillType;

    private String forceApprovalFlag;

    private String updateUnitPriceFlag;

    private String overLinkBillFlag;

    private Long tenantId;

    private String deleteFlag;
}