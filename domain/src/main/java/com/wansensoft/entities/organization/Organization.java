package com.wansensoft.entities.organization;

import lombok.Data;

import java.util.Date;

@Data
public class Organization {
    private Long id;

    private String orgNo;

    private String orgAbr;

    private Long parentId;

    private String sort;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Long tenantId;

    private String deleteFlag;
}