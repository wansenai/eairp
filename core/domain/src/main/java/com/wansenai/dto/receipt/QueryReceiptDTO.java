package com.wansenai.dto.receipt;

import lombok.Data;

@Data
public class QueryReceiptDTO {

    private Long id;

    private String type;

    private String subType;

    private String receiptNumber;

    private String productInfo;

    private String startDate;

    private String endDate;

    private Long page;

    private Long pageSize;
}
