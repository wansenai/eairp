package com.wansensoft.entities.product;

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
 * 多单位表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_unit")
public class ProductUnit implements Serializable {

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
     * 名称，支持多单位
     */
    private String name;

    /**
     * 基础单位
     */
    private String basicUnit;

    /**
     * 副单位
     */
    private String otherUnit;

    /**
     * 副单位2
     */
    private String otherUnitTwo;

    /**
     * 副单位3
     */
    private String otherUnitThree;

    /**
     * 比例
     */
    private BigDecimal ratio;

    /**
     * 比例2
     */
    private BigDecimal ratioTwo;

    /**
     * 比例3
     */
    private BigDecimal ratioThree;

    /**
     * 启用
     */
    private Boolean status;

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
