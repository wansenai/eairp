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
 * 产品表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
public class Product implements Serializable {

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
     * 产品类型id
     */
    private Long productCategoryId;

    /**
     * 名称
     */
    private String productName;

    /**
     * 制造商
     */
    private String productManufacturer;

    /**
     * 型号
     */
    private String productModel;

    /**
     * 规格
     */
    private String productStandard;

    /**
     * 颜色
     */
    private String productColor;

    /**
     * 计量单位Id
     */
    private Long productUnitId;

    /**
     * 单位-单个
     */
    private String productUnit;

    /**
     * 保质期天数
     */
    private Integer productExpiryNum;

    /**
     * 图片名称
     */
    private String productImgName;

    /**
     * 基础重量(kg)
     */
    private BigDecimal productWeight;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启用 0-禁用  1-启用
     */
    private Boolean status;

    /**
     * 自定义1
     */
    private String otherFieldOne;

    /**
     * 自定义2
     */
    private String otherFieldTwo;

    /**
     * 自定义3
     */
    private String otherFieldThree;

    /**
     * 是否开启序列号，0否，1是
     */
    private Boolean enableSerialNumber;

    /**
     * 是否开启批号，0否，1是
     */
    private Boolean enableBatchNumber;

    /**
     * 仓位货架
     */
    private String warehouseShelves;

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
