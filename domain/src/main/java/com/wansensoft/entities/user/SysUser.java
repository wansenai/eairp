/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansensoft.entities.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
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
    private String name;

    /**
     * 登录用户名
     */
    private String userName;

    /**
     *
     */
    private String avatar;

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
    private String phoneNumber;

    /**
     * 是否为管理者 0==管理者 1==员工
     */
    private Integer isManager;

    /**
     * 是否系统自带数据 
     */
    private Integer isSystem;

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
    private String wechatOpenId;

    /**
     * 租户id
     */
    private Long tenantId;


}
