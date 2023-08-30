package com.wansensoft.entities.depot;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepotItemVo4DetailByTypeAndMId {

    private String number;

    private String barCode;

    private String materialName;

    private String type;

    private String subType;

    private BigDecimal bnum;

    private String depotName;

    private Date otime;
}