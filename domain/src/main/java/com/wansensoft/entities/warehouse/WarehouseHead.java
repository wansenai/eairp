package com.wansensoft.entities.warehouse;

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
 * 单据主表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("warehouse_head")
public class WarehouseHead implements Serializable {

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
     * 主类型 (出库/入库)
     */
    private String type;

    /**
     * 子类型（采购订单/采购退货/销售订单/组装单/拆卸单）
     */
    private String subType;

    /**
     * 初始票据号
     */
    private String initBillNumber;

    /**
     * 票据号
     */
    private String billNumber;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 账户id
     */
    private Long accountId;

    /**
     * 变动金额(收款/付款)
     */
    private BigDecimal changeAmount;

    /**
     * 找零金额
     */
    private BigDecimal backAmount;

    /**
     * 合计金额
     */
    private BigDecimal totalPrice;

    /**
     * 付款类型(现金、记账等)
     */
    private String payType;

    /**
     * 单据类型
     */
    private String billType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 业务员（可以多个）
     */
    private String salesMan;

    /**
     * 多账户ID列表
     */
    private String accountIdList;

    /**
     * 多账户金额列表
     */
    private String accountMoneyList;

    /**
     * 优惠率
     */
    private BigDecimal discount;

    /**
     * 优惠金额
     */
    private BigDecimal discountMoney;

    /**
     * 优惠后金额
     */
    private BigDecimal discountLastMoney;

    /**
     * 销售或采购费用合计
     */
    private BigDecimal otherMoney;

    /**
     * 订金
     */
    private BigDecimal deposit;

    /**
     * 状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中
     */
    private Boolean status;

    /**
     * 采购状态，0未采购、2完成采购、3部分采购
     */
    private Boolean purchaseStatus;

    /**
     * 单据来源，0-pc，1-手机
     */
    private Boolean source;

    /**
     * 关联订单号
     */
    private String correlationNumber;

    /**
     * 操作时间（出/入库时间）
     */
    private LocalDateTime operateTime;

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
