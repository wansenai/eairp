package com.wansensoft.vo;

import com.wansensoft.entities.material.MaterialExtend;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialExtendVo4List extends MaterialExtend {

    private String supplier;

    private String originPlace;

    private String unit;

    private String brandName;

    private BigDecimal guaranteePeriod;

    private BigDecimal memberDecimal;
}
