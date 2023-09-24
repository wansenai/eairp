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
package com.wansensoft.api.role;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.role.ISysRoleService;
import com.wansensoft.service.system.ISysMenuService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.RoleVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("sysRole")
public class SysRoleController {

    private final ISysMenuService menuService;

    private final ISysRoleService roleService;

    public SysRoleController(ISysMenuService menuService, ISysRoleService roleService) {
        this.menuService = menuService;
        this.roleService = roleService;
    }


    @GetMapping("menu")
    public Response<JSONObject> queryMenu() {
        return menuService.menuList();
    }

    @GetMapping("list")
    public Response<List<RoleVO>> getRoleList() {
        return roleService.roleList();
    }

}
