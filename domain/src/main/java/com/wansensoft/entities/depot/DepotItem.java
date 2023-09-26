package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepotItem {
    private Long id;

    private Long headerId;

    private Long materialId;

    private Long materialExtendId;

    private String materialUnit;

    private String sku;

    private BigDecimal operNumber;

    private BigDecimal basicNumber;

    private BigDecimal unitPrice;

    private BigDecimal purchaseUnitPrice;

    private BigDecimal taxUnitPrice;

    private BigDecimal allPrice;

    private String remark;

    private Long depotId;

    private Long anotherDepotId;

    private BigDecimal taxRate;

    private BigDecimal taxMoney;

    private BigDecimal taxLastMoney;

    private String materialType;

    private String snList;

    private String batchNumber;

    private Date expirationDate;

    private Long linkId;

    private Long tenantId;

    private String deleteFlag;
}