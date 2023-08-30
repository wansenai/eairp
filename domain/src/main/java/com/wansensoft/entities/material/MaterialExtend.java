package com.wansensoft.entities.material;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MaterialExtend {
    private Long id;

    private Long materialId;

    private String barCode;

    private String commodityUnit;

    private String sku;

    private BigDecimal purchaseDecimal;

    private BigDecimal commodityDecimal;

    private BigDecimal wholesaleDecimal;

    private BigDecimal lowDecimal;

    private String defaultFlag;

    private Date createTime;

    private String createSerial;

    private String updateSerial;

    private Long updateTime;

    private Long tenantId;

    private String deleteFlag;
}