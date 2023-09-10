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
 * 财务主表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("financial_main")
public class FinancialMain implements Serializable {

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
    private Boolean receiptSource;

    /**
     * 单据日期
     */
    private LocalDateTime receiptTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 状态，0未审核、1已审核、9审核中
     */
    private Boolean status;

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
    private Boolean deleteFlag;


}
