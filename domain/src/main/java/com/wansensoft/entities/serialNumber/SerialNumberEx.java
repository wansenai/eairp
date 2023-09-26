package com.wansensoft.entities.serialNumber;

import lombok.Data;

@Data
public class SerialNumberEx extends SerialNumber{
    /**
     * 商品条码
     * */
    private String materialCode;
    /**
     * 商品名称
     * */
    private String materialName;
    /**
     * 创建者名称
     * */
    private String creatorName;
    /**
     * 更新者名称
     * */
    private String updaterName;
    /**单据编号*/
    private String depotHeadNumber;
    /**单据类型（出库入库）*/
    private String depotHeadType;

    private String depotName;

    private String createTimeStr;

    private String updateTimeStr;
}
