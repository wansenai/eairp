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
package com.wansenai.vo.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wansenai.bo.BigDecimalSerializerBO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SupplierBillVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long supplierId;

    private String supplierName;

    private String contactName;

    private String contactPhone;

    private String email;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal firstQuarterPayment;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal secondQuarterPayment;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal thirdQuarterPayment;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal fourthQuarterPayment;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal totalPayment;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal totalArrears;

    @JsonSerialize(using = BigDecimalSerializerBO.class)
    private BigDecimal remainingPaymentArrears;
}
