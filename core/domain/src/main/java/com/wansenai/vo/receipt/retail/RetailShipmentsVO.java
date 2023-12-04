package com.wansenai.vo.receipt.retail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import com.wansenai.utils.excel.ExcelExport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetailShipmentsVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ExcelExport(value = "会员")
    private String memberName;

    @ExcelExport(value = "单据编号")
    private String receiptNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "单据日期")
    private LocalDateTime receiptDate;

    @ExcelExport(value = "商品信息")
    private String productInfo;

    @ExcelExport(value = "操作人")
    private String operator;

    @ExcelExport(value = "商品数量")
    private Integer productNumber;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport(value = "金额合计")
    private BigDecimal totalPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport(value = "收款金额")
    private BigDecimal collectionAmount;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport(value = "找零金额")
    private BigDecimal backAmount;

    @ExcelExport(value = "状态", kv = "0-未审核;1-已审核")
    private Integer status;
}
