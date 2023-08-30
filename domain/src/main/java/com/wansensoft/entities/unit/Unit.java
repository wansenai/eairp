package com.wansensoft.entities.unit;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Unit {
    private Long id;

    private String name;

    private String basicUnit;

    private String otherUnit;

    private String otherUnitTwo;

    private String otherUnitThree;

    private BigDecimal ratio;

    private BigDecimal ratioTwo;

    private BigDecimal ratioThree;

    private Boolean enabled;

    private Long tenantId;

    private String deleteFlag;
}