package com.wansensoft.entities.material;

import lombok.Data;

@Data
public class MaterialAttribute {
    private Long id;

    private String attributeName;

    private String attributeValue;

    private Long tenantId;

    private String deleteFlag;
}