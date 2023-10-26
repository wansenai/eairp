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
package com.wansensoft.entities.financial;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 财务主表
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("financial_main")
public class FinancialMain implements Serializable {

    @Serial
    private static final long serialVersionUID = 71651561123L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 机构id(收款/付款单位)
     */
    private Long organizationId;

    /**
     * 经手人id
     */
    private Long handsPersonId;

    /**
     * 账户(收款/付款)
     */
    private Long accountId;

    /**
     * 类型(支出/收入/收款/付款/转账)
     */
    private String type;

    /**
     * 变动金额(优惠/收款/付款/实付)
     */
    private BigDecimal changePrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountPrice;

    /**
     * 合计金额
     */
    private BigDecimal totalPrice;

    /**
     * 单据编号
     */
    private String receiptNumber;

    /**
     * 单据来源，0-pc，1-手机
     */
    private Integer receiptSource;

    /**
     * 单据日期
     */
    private LocalDateTime receiptTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件名称 多个用,号分隔
     */
    private String fileId;

    /**
     * 状态，0未审核、1已审核、9审核中
     */
    private Integer status;

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
