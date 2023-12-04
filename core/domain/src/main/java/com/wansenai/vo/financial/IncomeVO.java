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
package com.wansenai.vo.financial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import com.wansenai.utils.excel.ExcelExport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // supplier or customer or member
    @ExcelExport(value = "往来单位/人员")
    private String name;

    @ExcelExport(value = "单据编号")
    private String receiptNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "单据日期")
    private LocalDateTime receiptDate;

    @ExcelExport(value = "财务人员")
    private String financialPerson;

    @ExcelExport(value = "收入账户")
    private String incomeAccountName;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport(value = "收入金额")
    private BigDecimal incomeAmount;

    @ExcelExport(value = "备注")
    private String remark;

    @ExcelExport(value = "状态", kv = "0-未审核;1-已审核")
    private Integer status;
}
