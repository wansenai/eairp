package com.wansensoft.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepotItemVo4Stock {

    private BigDecimal inTotal;

    private BigDecimal outTotal;

    private BigDecimal transfInTotal;

    private BigDecimal transfOutTotal;

    private BigDecimal assemInTotal;

    private BigDecimal assemOutTotal;

    private BigDecimal disAssemInTotal;

    private BigDecimal disAssemOutTotal;
}
