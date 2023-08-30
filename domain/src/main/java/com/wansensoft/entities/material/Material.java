package com.wansensoft.entities.material;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Material {
    private Long id;

    private Long categoryId;

    private String name;

    private String mfrs;

    private String model;

    private String standard;

    private String color;

    private String unit;

    private String remark;

    private String imgName;

    private Long unitId;

    private Integer expiryNum;

    private BigDecimal weight;

    private Boolean enabled;

    private String otherField1;

    private String otherField2;

    private String otherField3;

    private String enableSerialNumber;

    private String enableBatchNumber;

    private String position;

    private Long tenantId;

    private String deleteFlag;
}