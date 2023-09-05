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
 * 财务子表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("financial_sub")
public class FinancialSub implements Serializable {

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
    private Long tableHeaderId;

    /**
     * 账户Id
     */
    private Long accountId;

    /**
     * 收支项目Id
     */
    private Long incomeExpenseId;

    /**
     * 单据id
     */
    private Long receiptId;

    /**
     * 应收欠款
     */
    private BigDecimal accountsReceivable;

    /**
     * 已收欠款
     */
    private BigDecimal accountsReceived;

    /**
     * 单项金额
     */
    private BigDecimal singleAmount;

    /**
     * 单据备注
     */
    private String remark;

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
