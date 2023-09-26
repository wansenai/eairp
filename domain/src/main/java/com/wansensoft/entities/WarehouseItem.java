package com.wansensoft.entities;

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
 * 单据子表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("warehouse_item")
public class WarehouseItem implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 表头Id
     */
    private Long headerId;

    /**
     * 商品Id
     */
    private Long productId;

    /**
     * 商品扩展id
     */
    private Long productExtendId;

    /**
     * 商品计量单位
     */
    private String productUnit;

    /**
     * 多属性
     */
    private String multiAttribute;

    /**
     * 数量
     */
    private BigDecimal operNumber;

    /**
     * 基础数量，如kg、瓶
     */
    private BigDecimal basicNumber;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 采购单价
     */
    private BigDecimal purchaseUnitPrice;

    /**
     * 含税单价
     */
    private BigDecimal taxUnitPrice;

    /**
     * 金额
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 调拨时，对方仓库Id
     */
    private Long anotherWarehouseId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private BigDecimal taxMoney;

    /**
     * 价税合计
     */
    private BigDecimal taxLastMoney;

    /**
     * 商品类型
     */
    private String productType;

    /**
     * 序列号列表
     */
    private String serialNumbersList;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 有效日期
     */
    private LocalDateTime effectiveDate;

    /**
     * 关联明细id
     */
    private Long correlationId;

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
    private Boolean deleteFlag;


}
