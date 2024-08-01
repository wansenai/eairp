/*
 * Copyright 2024-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansenai.bo.warehouse;

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
public class AssembleStockDataExportEnBO {

    @ExcelExport("Receipt Number")
    private String receiptNumber;

    @ExcelExport("Product Type")
    private String type;

    @ExcelExport("Warehouse")
    private String warehouseName;

    @ExcelExport("BarCode")
    private String barCode;

    @ExcelExport("Product Name")
    private String productName;

    @ExcelExport("Standard")
    private String productStandard;

    @ExcelExport("Model")
    private String productModel;

    @ExcelExport("Unit")
    private String productUnit;

    @ExcelExport("Stock")
    private Integer stock;

    @ExcelExport("Quantity")
    private Integer productNumber;

    @ExcelExport("Unit Price")
    private BigDecimal unitPrice;

    @ExcelExport("Amount")
    private BigDecimal amount;

    @ExcelExport("Remark")
    private String remark;
}
