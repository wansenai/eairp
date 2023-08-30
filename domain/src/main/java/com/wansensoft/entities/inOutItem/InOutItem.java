package com.wansensoft.entities.inOutItem;

import lombok.Data;

@Data
public class InOutItem {
    private Long id;

    private String name;

    private String type;

    private String remark;

    private Boolean enabled;

    private String sort;

    private Long tenantId;

    private String deleteFlag;
}