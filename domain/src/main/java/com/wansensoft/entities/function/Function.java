package com.wansensoft.entities.function;

import lombok.Data;

@Data
public class Function {
    private Long id;

    private String number;

    private String name;

    private String parentNumber;

    private String url;

    private String component;

    private Boolean state;

    private String sort;

    private Boolean enabled;

    private String type;

    private String pushBtn;

    private String icon;

    private String deleteFlag;
}