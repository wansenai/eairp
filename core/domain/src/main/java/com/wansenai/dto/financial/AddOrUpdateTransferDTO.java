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
package com.wansenai.dto.financial;

import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.TransferAccountBO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddOrUpdateTransferDTO {

    private Long id;

    private String receiptDate;

    private String receiptNumber;

    private Long financialPersonId;

    private Long paymentAccountId;

    private BigDecimal paymentAmount;

    private String remark;

    private Integer status;

    private List<TransferAccountBO> tableData;

    private List<FileDataBO> files;
}
