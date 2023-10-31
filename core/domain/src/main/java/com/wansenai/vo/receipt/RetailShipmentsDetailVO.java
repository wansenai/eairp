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
package com.wansenai.vo.receipt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.ShipmentsDataBO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RetailShipmentsDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime receiptDate;

    private String receiptNumber;

    private String receiptType;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal collectionAmount;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal receiptAmount;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal backAmount;

    private String remark;

    private List<ShipmentsDataBO> shipmentsDataBO;

    private List<FileDataBO> fileDataBO;
}
