package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description
 */
@Data
public class DepotEx extends Depot{
    //负责人名字
    private String principalName;

    private BigDecimal initStock;

    private BigDecimal currentStock;

    private BigDecimal lowSafeStock;

    private BigDecimal highSafeStock;

}
