package com.wansensoft.entities.material;

import lombok.Data;

import java.util.Date;

@Data
public class MaterialCategory {
    private Long id;

    private String name;

    private Short categoryLevel;

    private Long parentId;

    private String sort;

    private String serialNo;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Long tenantId;

    private String deleteFlag;
}