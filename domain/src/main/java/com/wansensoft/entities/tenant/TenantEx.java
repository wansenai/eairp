package com.wansensoft.entities.tenant;

import lombok.Data;

@Data
public class TenantEx extends Tenant{

    private String createTimeStr;

    private String expireTimeStr;

    private Integer userCount;
}