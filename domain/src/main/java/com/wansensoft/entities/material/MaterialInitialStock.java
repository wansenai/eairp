package com.wansensoft.entities.material;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialInitialStock {
    private Long id;

    private Long materialId;

    private Long depotId;

    private BigDecimal number;

    private BigDecimal lowSafeStock;

    private BigDecimal highSafeStock;

    private Long tenantId;

    private String deleteFlag;
}