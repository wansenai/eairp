package com.wansensoft.dto.depot;

import com.wansensoft.dto.PageSizeDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 零售出库查询数据请求Dto
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class RetailOutboundDto extends PageSizeDto {

    private String type;

    private String subType;

    private String roleType;

    private String hasDebt;

    private String status;

    private String purchaseStatus;

    private String number;

    private String linkNumber;

    private String beginTime;

    private String endTime;

    private String materialParam;

    private Long organId;

    private Long creator;

    private Long depotId;

    private Long accountId;

    private String remark;

    private int offset;

    private int rows;

    private String token;
}
