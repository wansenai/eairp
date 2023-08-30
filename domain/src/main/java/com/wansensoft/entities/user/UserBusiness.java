package com.wansensoft.entities.user;

import lombok.Data;

@Data
public class UserBusiness {
    private Long id;

    private String type;

    private String keyId;

    private String value;

    private String btnStr;

    private Long tenantId;

    private String deleteFlag;
}