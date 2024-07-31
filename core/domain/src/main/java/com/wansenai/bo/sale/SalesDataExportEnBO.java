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
package com.wansenai.bo.sale;

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
public class SalesDataExportEnBO {

    @ExcelExport(value = "Customer")
    private String customerName;

    @ExcelExport(value = "Receipt Number")
    private String receiptNumber;

    @ExcelExport("Warehouse")
    private String warehouseName;

    @ExcelExport("Barcode")
    private String barCode;

    @ExcelExport("Name")
    private String productName;

    @ExcelExport("Standard")
    private String productStandard;

    @ExcelExport("Model")
    private String productModel;

    @ExcelExport("Color")
    private String productColor;

    @ExcelExport("Stock")
    private Integer stock;

    @ExcelExport("Unit")
    private String productUnit;

    @ExcelExport("Quantity")
    private Integer productNumber;

    @ExcelExport("Unit price")
    private BigDecimal unitPrice;

    @ExcelExport("Amount")
    private BigDecimal amount;

    @ExcelExport("Tax Rate (%)")
    private BigDecimal taxRate;

    @ExcelExport("Tax Amount")
    private BigDecimal taxAmount;

    @ExcelExport("Tax Total Price")
    private BigDecimal taxTotalPrice;

    @ExcelExport("Remark")
    private String remark;
}
