package com.wansensoft.entities.role;

import lombok.Data;

@Data
public class Role {
    private Long id;

    private String name;

    private String type;

    private String priceLimit;

    private String value;

    private String description;

    private Boolean enabled;

    private String sort;

    private Long tenantId;

    private String deleteFlag;
}