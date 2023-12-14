package com.wansenai.entities.basic;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 15791051662L;

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
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 手机
     */
    private String phoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 一季度应收账款
     */
    private BigDecimal firstQuarterAccountReceivable;

    /**
     * 二季度应收账款
     */
    private BigDecimal secondQuarterAccountReceivable;

    /**
     * 三季度应收账款
     */
    private BigDecimal thirdQuarterAccountReceivable;

    /**
     * 四季度应收账款
     */
    private BigDecimal fourthQuarterAccountReceivable;

    /**
     * 累计应收账款
     */
    private BigDecimal totalAccountReceivable;

    /**
     * 传真
     */
    private String fax;

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
    private String accountNumber;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 状态（0-启用，1-停用）默认启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

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
