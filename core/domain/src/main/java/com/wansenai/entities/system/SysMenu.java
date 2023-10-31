/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wansenai.entities.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 功能模块表
 * </p>
 */
@TableName("sys_menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 标题（菜单显示）
     */
    @TableField("title")
    private String title;

    /**
     * 父级菜单id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 类型
     */
    @TableField("menu_type")
    private Integer menuType;

    /**
     * 链接
     */
    @TableField("path")
    private String path;

    /**
     * 组件
     */
    @TableField("component")
    private String component;

    /**
     * 重定向地址
     */
    @TableField("redirect")
    private String redirect;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 状态（0-启用，1-停用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 隐藏路由不在菜单显示
     */
    @TableField("hide_menu")
    private Integer hideMenu;


    @TableField("blank")
    private Integer blank;

    /**
     * 隐藏该路由在面包屑上面的显示
     */
    @TableField("hide_breadcrumb")
    private Integer hideBreadcrumb;

    /**
     * 是否忽略KeepAlive缓存
     */
    @TableField("ignore_keep_alive")
    private Integer ignoreKeepAlive;

    /**
     * 隐藏路由不在标签页显示
     */
    @TableField("hide_tab")
    private Integer hideTab;

    /**
     * 如果该路由会携带参数，且需要在tab页上面显示。则需要设置为true
     */
    @TableField("carry_param")
    private Integer carryParam;

    /**
     * 隐藏所有子菜单
     */
    @TableField("hide_children_in_menu")
    private Integer hideChildrenInMenu;

    /**
     * 是否固定标签
     */
    @TableField("affix")
    private Integer affix;

    /**
     * 内嵌iframe的地址
     */
    @TableField("frameSrc")
    private String frameSrc;

    /**
     * 动态路由的实际Path, 即去除路由的动态部分;
     */
    @TableField("realPath")
    private String realPath;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 删除标记，0未删除，1删除
     */
    @TableField("delete_flag")
    private Integer deleteFlag;
}
