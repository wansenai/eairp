package com.wansensoft.entities.material;

import lombok.Data;

@Data
public class MaterialProperty {
    private Long id;

    private String nativeName;

    private Boolean enabled;

    private String sort;

    private String anotherName;

    private String deleteFlag;
}