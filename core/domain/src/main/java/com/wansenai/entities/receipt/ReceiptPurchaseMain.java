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
package com.wansenai.entities.receipt;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("receipt_purchase_main")
public class ReceiptPurchaseMain implements Serializable {

    @Serial
    private static final long serialVersionUID = 7916561623533L;

    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    private Long supplierId;

    private Long accountId;

    private String type;

    private String subType;

    private String initReceiptNumber;

    private String receiptNumber;

    private LocalDateTime receiptDate;

    private BigDecimal changeAmount;

    private BigDecimal totalAmount;

    private BigDecimal discountRate;

    private BigDecimal discountAmount;

    private BigDecimal discountLastAmount;

    private BigDecimal arrearsAmount;

    private BigDecimal otherAmount;

    private BigDecimal deposit;

    private String fileId;

    private String multipleAccount;

    private String multipleAccountAmount;

    private String remark;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createBy;

    private Long updateBy;

    private Integer deleteFlag;
}
