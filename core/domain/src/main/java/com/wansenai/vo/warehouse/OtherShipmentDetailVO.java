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
package com.wansenai.vo.warehouse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wansenai.bo.FileDataBO;
import com.wansenai.bo.StorageShipmentStockBO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OtherShipmentDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    private String customerName;

    private String receiptNumber;

    private String receiptDate;

    private String remark;

    private Integer status;

    private List<StorageShipmentStockBO> tableData;

    private List<FileDataBO> files;
}
