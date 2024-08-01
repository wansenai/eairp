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
public class OtherStorageExportBO {

    private Long id;

    @ExcelExport(value = "供应商")
    private String supplierName;

    @ExcelExport(value = "单据编号")
    private String receiptNumber;

    @ExcelExport(value = "商品信息")
    private String productInfo;

    @ExcelExport(value = "单据日期")
    private LocalDateTime receiptDate;

    @ExcelExport(value = "商品数量")
    private Integer productNumber;

    @ExcelExport(value = "金额合计")
    private BigDecimal totalAmount;

    @ExcelExport(value = "操作员")
    private String operator;

    @ExcelExport(value = "状态", kv = "0-未审核;1-已审核")
    private Integer status;
}
