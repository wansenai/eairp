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
import lombok.*;
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
@TableName("receipt_main")
public class ReceiptMain implements Serializable {

    @Serial
    private static final long serialVersionUID = 514516533L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    private Long memberId;

    private Long accountId;

    private String type;

    private String subType;

    private String initReceiptNumber;

    private String receiptNumber;

    private BigDecimal changeAmount;

    private BigDecimal backAmount;

    private BigDecimal totalPrice;

    private String paymentType;

    private String remark;

    private String fileId;

    private String operatorId;

    private String multipleAccount;

    private String multipleAccountAmount;

    private BigDecimal discountRate;

    private BigDecimal discountAmount;

    private BigDecimal discountLastAmount;

    private BigDecimal otherAmount;

    private BigDecimal deposit;

    private Integer status;

    private Integer purchaseStatus;

    private Integer source;

    private String otherReceipt;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 删除标记，0未删除，1删除
     */
    private Integer deleteFlag;
}
