package com.wansensoft.entities;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商/客户信息表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("supplier")
public class SystemSupplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 备注
     */
    private String remake;

    /**
     * 是否系统自带 0==系统 1==非系统
     */
    private Integer isSystem;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态（0-启用，1-停用）
     */
    private Boolean status;

    /**
     * 预收款
     */
    private BigDecimal advanceReceivable;

    /**
     * 期初应收
     */
    private BigDecimal beginAccountReceivable;

    /**
     * 期初应付
     */
    private BigDecimal beginAccountPayment;

    /**
     * 累计应收
     */
    private BigDecimal totalReceivable;

    /**
     * 累计应付
     */
    private BigDecimal totalPayment;

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
    private String bankNumber;

    /**
     * 账号
     */
    private String accountNumber;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 排序
     */
    private String sort;

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
