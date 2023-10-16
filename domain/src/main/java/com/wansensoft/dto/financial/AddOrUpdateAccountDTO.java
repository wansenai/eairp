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
package com.wansensoft.dto.financial;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AddOrUpdateAccountDTO {

    private Long id;

    /**
     * 名称
     */
    private String accountName;

    /**
     * 账户编号
     */
    private String accountNumber;

    /**
     * 期初金额
     */
    private BigDecimal initialAmount;

    /**
     * 当前余额
     */
    private BigDecimal currentAmount;

    /**
     * 是否默认
     */
    private Integer isDefault;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

}
