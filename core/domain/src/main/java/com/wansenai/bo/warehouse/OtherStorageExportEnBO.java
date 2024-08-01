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
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OtherStorageExportEnBO {

    private Long id;

    @ExcelExport(value = "Supplier")
    private String supplierName;

    @ExcelExport(value = "Receipt Number")
    private String receiptNumber;

    @ExcelExport(value = "Product Info")
    private String productInfo;

    @ExcelExport(value = "Receipt Date")
    private LocalDateTime receiptDate;

    @ExcelExport(value = "Quantity")
    private Integer productNumber;

    @ExcelExport(value = "Total Amount")
    private BigDecimal totalAmount;

    @ExcelExport(value = "Operator")
    private String operator;

    @ExcelExport(value = "Status", kv = "0-Unaudited;1-Audited;")
    private Integer status;
}
