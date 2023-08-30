package com.wansensoft.entities.tenant;

import lombok.Data;

import java.util.Date;

@Data
public class Tenant {
    private Long id;

    private Long tenantId;

    private String loginName;

    private Integer userNumLimit;

    private String type;

    private Boolean enabled;

    private Date createTime;

    private Date expireTime;

    private String remark;
}