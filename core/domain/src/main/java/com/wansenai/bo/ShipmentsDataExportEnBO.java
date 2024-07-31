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
package com.wansenai.bo;

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
public class ShipmentsDataExportEnBO {

    @ExcelExport(value = "Member")
    private String memberName;

    @ExcelExport(value = "Receipt Number")
    private String receiptNumber;

    @ExcelExport(value = "Warehouse")
    private String warehouseName;

    @ExcelExport(value = "BarCode")
    private String barCode;

    @ExcelExport(value = "Product Name")
    private String productName;

    @ExcelExport(value = "Standard")
    private String productStandard;

    @ExcelExport(value = "Model")
    private String productModel;

    @ExcelExport(value = "Color")
    private String productColor;

    @ExcelExport(value = "Stock")
    private Integer stock;

    @ExcelExport(value = "Unit")
    private String productUnit;

    @ExcelExport(value = "Quantity")
    private Integer productNumber;

    @ExcelExport(value = "Unit Price")
    private BigDecimal unitPrice;

    @ExcelExport(value = "Amount")
    private BigDecimal amount;

    @ExcelExport(value = "Remark")
    private String remark;
}
