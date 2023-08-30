package com.wansensoft.entities.msg;

import lombok.Data;

import java.util.Date;

@Data
public class Msg {
    private Long id;

    private String msgTitle;

    private String msgContent;

    private Date createTime;

    private String type;

    private Long userId;

    private String status;

    private Long tenantId;

    private String deleteFlag;
}