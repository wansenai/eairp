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
package com.wansenai.bo.financial;

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
public class PaymentDataExportEnBO {

    @ExcelExport(value = "Supplier")
    private String supplierName;

    @ExcelExport(value = "Payment Receipt Number")
    private String receiptNumber;

    @ExcelExport(value = "Purchase Receipt Number")
    private String purchaseReceiptNumber;

    @ExcelExport(value = "Payment Arrears")
    private BigDecimal paymentArrears;

    @ExcelExport(value = "Prepaid Arrears")
    private BigDecimal  prepaidArrears;

    @ExcelExport(value = "This Payment Amount")
    private BigDecimal thisPaymentAmount;

    @ExcelExport(value = "Remark")
    private String remark;
}
