/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
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
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentDataExportBO {

    @ExcelExport(value = "供应商")
    private String supplierName;

    @ExcelExport(value = "付款单号")
    private String receiptNumber;

    @ExcelExport(value = "采购单据编号")
    private String purchaseReceiptNumber;

    @ExcelExport(value = "应付欠款")
    private BigDecimal paymentArrears;

    @ExcelExport(value = "已付欠款")
    private BigDecimal  prepaidArrears;

    @ExcelExport(value = "本次付款")
    private BigDecimal thisPaymentAmount;

    @ExcelExport(value = "备注")
    private String remark;
}
