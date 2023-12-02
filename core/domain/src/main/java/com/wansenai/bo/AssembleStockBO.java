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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.utils.excel.ExcelExport;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AssembleStockBO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ExcelExport("商品类型")
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long warehouseId;

    @ExcelExport("仓库")
    private String warehouseName;

    @ExcelExport("条码")
    private String barCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ExcelExport("商品名称")
    private String productName;

    @ExcelExport("规格")
    private String productStandard;

    @ExcelExport("型号")
    private String productModel;

    @ExcelExport("单位")
    private String productUnit;

    @ExcelExport("库存")
    private Integer stock;

    @ExcelExport("商品数量")
    private Integer productNumber;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport("单价")
    private BigDecimal unitPrice;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    @ExcelExport("金额")
    private BigDecimal amount;

    @ExcelExport("备注")
    private String remark;
}
