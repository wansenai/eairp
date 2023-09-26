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
 * 产品价格扩展
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product_extend_price")
public class ProductExtendPrice implements Serializable {

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
     * 商品id
     */
    private Long productId;

    /**
     * 商品条码
     */
    private String productBarCode;

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
     * 是否为默认单位，1是，0否
     */
    private Boolean defaultFlag;

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
