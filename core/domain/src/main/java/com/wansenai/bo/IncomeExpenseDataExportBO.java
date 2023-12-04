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
public class IncomeExpenseDataExportBO {

    @ExcelExport(value = "单据编号")
    private String receiptNumber;

    @ExcelExport(value = "往来单位/人员")
    private String relatedPerson;

    @ExcelExport(value = "收入/支出项目")
    private String incomeExpenseName;

    @ExcelExport(value = "金额")
    private BigDecimal incomeExpenseAmount;

    @ExcelExport(value = "备注")
    private String remark;
}
