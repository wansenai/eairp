package com.wansensoft.entities.log;

import lombok.Data;

import java.util.Date;

@Data
public class Log {
    private Long id;

    private Long userId;

    private String operation;

    private String clientIp;

    private Date createTime;

    private Byte status;

    private String content;

    private Long tenantId;
}