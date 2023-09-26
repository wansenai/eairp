package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotHeadVo4Body {

    private Long id;

    private String info;

    private String rows;

    private BigDecimal preTotalPrice;
}