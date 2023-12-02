package com.wansenai.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.utils.excel.ExcelExport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentsDataBO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long warehouseId;

    @ExcelExport(value = "仓库名称")
    private String warehouseName;

    private String productCode;

    @ExcelExport(value = "条码")
    private String barCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ExcelExport(value = "商品名称")
    private String productName;

    @ExcelExport(value = "规格")
    private String productStandard;

    @ExcelExport(value = "型号")
    private String productModel;

    @ExcelExport(value = "颜色")
    private String productColor;

    @ExcelExport(value = "库存")
    private Integer stock;

    @ExcelExport(value = "单位")
    private String productUnit;

    @ExcelExport(value = "数量")
    private Integer productNumber;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport(value = "单价")
    private BigDecimal unitPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport(value = "金额")
    private BigDecimal amount;

    @ExcelExport(value = "备注")
    private String remark;
}
