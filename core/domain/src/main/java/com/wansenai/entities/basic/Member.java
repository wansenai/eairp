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
@TableName("member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    @Serial
    private static final long serialVersionUID = 79156235842L;

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
     * 会员卡号 这里不是会员的id
     */
    private String memberNumber;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 手机
     */
    private String phoneNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 预付款
     * 数据依赖于收预付款单据。
     */
    private BigDecimal advancePayment;

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
