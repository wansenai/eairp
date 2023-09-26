package com.wansensoft.entities.serialNumber;

import lombok.Data;

import java.util.Date;

@Data
public class SerialNumber {
    private Long id;

    private Long materialId;

    private Long depotId;

    private String serialNumber;

    private String isSell;

    private String remark;

    private String deleteFlag;

    private Date createTime;

    private Long creator;

    private Date updateTime;

    private Long updater;

    private String inBillNo;

    private String outBillNo;

    private Long tenantId;
}