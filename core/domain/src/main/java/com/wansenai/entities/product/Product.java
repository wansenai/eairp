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
package com.wansenai.entities.product;

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
 * 产品表
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 79165168544983L;

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
     * 产品类型id
     */
    private Long productCategoryId;

    /**
     * 名称
     */
    private String productName;

    /**
     * 制造商
     */
    private String productManufacturer;

    /**
     * 型号
     */
    private String productModel;

    /**
     * 规格
     */
    private String productStandard;

    /**
     * 颜色
     */
    private String productColor;

    /**
     * 计量单位Id
     */
    private Long productUnitId;

    /**
     * 单位-单个
     */
    private String productUnit;

    /**
     * 保质期天数
     */
    private Integer productExpiryNum;

    /**
     * 基础重量(kg)
     */
    private BigDecimal productWeight;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启用 0-禁用  1-启用
     */
    private Integer status;

    /**
     * 自定义1
     */
    private String otherFieldOne;

    /**
     * 自定义2
     */
    private String otherFieldTwo;

    /**
     * 自定义3
     */
    private String otherFieldThree;

    /**
     * 是否开启序列号，0否，1是
     */
    private Integer enableSerialNumber;

    /**
     * 是否开启批号，0否，1是
     */
    private Integer enableBatchNumber;

    /**
     * 仓位货架
     */
    private String warehouseShelves;

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
