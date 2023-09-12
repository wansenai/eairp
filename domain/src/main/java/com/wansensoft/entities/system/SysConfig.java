package com.wansensoft.entities.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_config")
public class SysConfig implements Serializable {

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
     * 公司名称
     */
    private String companyName;

    /**
     * 公司联系人
     */
    private String companyContact;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司电话
     */
    private String companyPhone;

    /**
     * 公司传真
     */
    private String companyFax;

    /**
     * 公司邮编
     */
    private String companyPostCode;

    /**
     * 销售协议
     */
    private String saleAgreement;

    /**
     * 仓库启用标记，0未启用，1启用
     */
    private Boolean warehouseStatus;

    /**
     * 客户启用标记，0未启用，1启用
     */
    private Boolean customerStatus;

    /**
     * 负库存启用标记，0未启用，1启用
     */
    private Boolean minusStockStatus;

    /**
     * 以销定购启用标记，0未启用，1启用
     */
    private Boolean purchaseBySaleStatus;

    /**
     * 多级审核启用标记，0未启用，1启用
     */
    private Boolean multiLevelApprovalStatus;

    /**
     * 流程类型，可多选
     */
    private String processType;

    /**
     * 强审核启用标记，0未启用，1启用
     */
    private Boolean forceApprovalStatus;

    /**
     * 更新单价启用标记，0未启用，1启用
     */
    private Boolean updateUnitPriceStatus;

    /**
     * 超出关联单据启用标记，0未启用，1启用
     */
    private Boolean overLinkBillStatus;

    /**
     * 删除标记，0未删除，1删除
     */
    private Boolean deleteFlag;


}
