package com.wansenai.bo;

import com.wansenai.utils.excel.ExcelExport;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 其他入库/出库导出BO (StorageShipmentStockExportBO) 两个BO类的字段一样，只是一个用于导出，一个用于导入
 */
@Data
@Builder
public class StorageShipmentStockExportBO {

    @ExcelExport(value = "单据编号")
    private String receiptNumber;

    @ExcelExport(value = "关联人员类型")
    private String relatedPersonType;

    @ExcelExport(value = "关联人员")
    private String relatedPerson;

    @ExcelExport(value = "仓库名称")
    private String warehouseName;

    @ExcelExport(value = "条码")
    private String barCode;

    @ExcelExport(value = "商品名称")
    private String productName;

    @ExcelExport(value = "规格")
    private String productStandard;

    @ExcelExport(value = "型号")
    private String productModel;

    @ExcelExport(value = "扩展信息")
    private String productExtendInfo;

    @ExcelExport(value = "库存")
    private Integer stock;

    @ExcelExport(value = "单位")
    private String productUnit;

    @ExcelExport(value = "数量")
    private Integer productNumber;

    @ExcelExport(value = "单价")
    private BigDecimal unitPrice;

    @ExcelExport(value = "金额")
    private BigDecimal amount;

    @ExcelExport(value = "备注")
    private String remark;
}
