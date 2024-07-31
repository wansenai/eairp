package com.wansenai.dto.receipt.sale;

import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.sale.SalesDataBO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRefundDTO {

    private Long id;

    private Long customerId;

    private String receiptNumber;

    private String receiptDate;

    private String otherReceipt;

    private BigDecimal refundOfferRate;

    private BigDecimal refundOfferAmount;

    private BigDecimal refundLastAmount;

    private BigDecimal otherAmount;

    private BigDecimal thisRefundAmount;

    private BigDecimal thisArrearsAmount;

    private Long accountId;

    private List<Long> multipleAccountAmounts;

    private List<Long> multipleAccountIds;

    private List<Long> operatorIds;

    private List<SalesDataBO> tableData;

    private List<FileDataBO> files;

    private Integer status;

    private String remark;
}
