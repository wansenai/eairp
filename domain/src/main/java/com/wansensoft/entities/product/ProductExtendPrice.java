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
package com.wansensoft.entities.product;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品价格扩展
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_extend_price")
public class ProductExtendPrice implements Serializable {

    @Serial
    private static final long serialVersionUID = 7891651633323L;

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
     * 商品id
     */
    private Long productId;

    /**
     * 商品条码
     */
    private Integer productBarCode;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 多属性
     */
    private String multiAttribute;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 零售价格
     */
    private BigDecimal retailPrice;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 最低售价
     */
    private BigDecimal lowPrice;

    /**
     * 是否为默认单位，1是，0否 默认0
     */
    private Integer defaultFlag;

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

    /**
     * 删除标记，0未删除，1删除
     */
    private Integer deleteFlag;


}
