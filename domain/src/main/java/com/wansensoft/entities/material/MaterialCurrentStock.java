package com.wansensoft.entities.material;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialCurrentStock {
    private Long id;

    private Long materialId;

    private Long depotId;

    private BigDecimal currentNumber;

    private Long tenantId;

    private String deleteFlag;
}