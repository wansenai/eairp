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
package com.wansenai.dto.receipt.purchase;

import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.PurchaseDataBO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PurchaseOrderDTO {

    private Long id;

    private Long supplierId;

    private String receiptNumber;

    private String receiptDate;

    private BigDecimal discountRate;

    private BigDecimal discountAmount;

    private BigDecimal discountLastAmount;

    private BigDecimal deposit;

    private Long accountId;

    private List<Long> operatorIds;

    private List<Long> multipleAccountAmounts;

    private List<Long> multipleAccountIds;

    private List<PurchaseDataBO> tableData;

    private List<FileDataBO> files;

    private Integer status;

    private String remark;
}
