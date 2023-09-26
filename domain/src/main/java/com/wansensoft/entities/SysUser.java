package com.wansensoft.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 用户姓名--例如张三
     */
    private String username;

    /**
     * 登录用户名
     */
    private String loginName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 是否经理，0否，1是
     */
    private Boolean leaderFlag;

    /**
     * 职位
     */
    private String position;

    /**
     * 所属部门
     */
    private String department;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenum;

    /**
     * 是否为管理者 0==管理者 1==员工
     */
    private Integer ismanager;

    /**
     * 是否系统自带数据 
     */
    private Integer isystem;

    /**
     * 状态，0：正常，1：删除，2封禁
     */
    private Integer status;

    /**
     * 用户描述信息
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 微信绑定
     */
    private String weixinOpenId;

    /**
     * 租户id
     */
    private Long tenantId;


}
