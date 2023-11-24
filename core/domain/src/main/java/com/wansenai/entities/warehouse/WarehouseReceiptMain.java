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
package com.wansenai.entities.warehouse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("warehouse_receipt_main")
public class WarehouseReceiptMain {

    @Serial
    private static final long serialVersionUID = 4554191615716239L;

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
     * 关联人id(客户/供应商)
     */
    private Long relatedPersonId;

    /**
     * 类型(入库/出库/调拨/组装/拆卸)
     */
    private String type;

    /**
     * 初始化单据编号
     */
    private String initReceiptNumber;

    /**
     * 单据编号
     */
    private String receiptNumber;

    /**
     * 单据日期
     */
    private LocalDateTime receiptDate;

    /**
     * 合计金额
     */
    private BigDecimal totalAmount;

    /**
     * 商品总数量
     */
    private Integer totalProductNumber;

    /**
     * 商品数量
     */
    private String productInfo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文件id 多个逗号分隔
     */
    private String fileId;

    /**
     * 状态，0未审核、1已审核 默认0
     */
    private Integer status;

    /**
     * 单据来源，0-pc，1-手机
     */
    private Integer source;

    /**
     * 关联单据
     */
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
