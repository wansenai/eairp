package com.wansensoft.entities.material;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialInitialStockWithMaterial {

    private Long materialId;

    private BigDecimal number;
}