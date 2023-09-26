package com.wansensoft.entities.person;

import lombok.Data;

@Data
public class Person {
    private Long id;

    private String type;

    private String name;

    private Boolean enabled;

    private String sort;

    private Long tenantId;

    private String deleteFlag;
}