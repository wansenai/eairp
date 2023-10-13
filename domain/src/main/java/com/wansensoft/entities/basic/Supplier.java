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
package com.wansensoft.entities.basic;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商/客户信息表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("supplier")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 是否系统自带 0==系统 1==非系统
     */
    private Integer isSystem;

    /**
     * 类型 (预留字段)
     */
    private String type;

    /**
     * 状态（0-启用，1-停用）默认启用
     */
    private Integer status;

    /**
     * 一季度应付账款
     */
    private BigDecimal firstQuarterAccountPayment;

    /**
     * 二季度应付账款
     */
    private BigDecimal secondQuarterAccountPayment;

    /**
     * 三季度应付账款
     */
    private BigDecimal thirdQuarterAccountPayment;

    /**
     * 四季度应付账款
     */
    private BigDecimal fourthQuarterAccountPayment;

    /**
     * 累计应付账款
     */
    private BigDecimal totalAccountPayment;

    /**
     * 传真
     */
    private String fax;

    /**
     * 手机
     */
    private String phoneNumber;

    /**
     * 地址
     */
    private String address;

    /**
     * 纳税人识别号
     */
    private String taxNumber;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 账号
     */
    private Long accountNumber;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记，0未删除，1删除
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
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
}
