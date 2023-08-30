package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Depot {
    private Long id;

    private String name;

    private String address;

    private BigDecimal warehousing;

    private BigDecimal truckage;

    private Integer type;

    private String sort;

    private String remark;

    private Long principal;

    private Boolean enabled;

    private Long tenantId;

    private String deleteFlag;

    private Boolean isDefault;
}