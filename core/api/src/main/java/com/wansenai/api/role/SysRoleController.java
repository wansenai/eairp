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
package com.wansenai.api.role;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.role.AddOrUpdateRoleDTO;
import com.wansenai.dto.role.RoleListDTO;
import com.wansenai.dto.role.RolePermissionDTO;
import com.wansenai.service.role.SysRoleService;
import com.wansenai.service.system.SysMenuService;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.RoleVO;
import org.springframework.web.bind.annotation.*;

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

    private final SysMenuService menuService;

    private final SysRoleService sysRoleService;

    public SysRoleController(SysMenuService menuService, SysRoleService sysRoleService) {
        this.menuService = menuService;
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("menu")
    public Response<JSONObject> queryMenu() {
        return menuService.menuList();
    }

    @GetMapping("list")
    public Response<List<RoleVO>> getRoleList() {
        return sysRoleService.roleList();
    }

    @PostMapping("PageList")
    public Response<Page<RoleVO>> getRolePageList(@RequestBody RoleListDTO roleListDTO) {
        return sysRoleService.rolePageList(roleListDTO);
    }

    @PostMapping("updateStatus")
    public Response<String> updateStatus(@RequestParam(value = "id") String id, @RequestParam(value = "status") Integer status) {
        return sysRoleService.updateStatus(id, status);
    }

    @PostMapping("addOrUpdateRole")
    public Response<String> addOrUpdateRole(@RequestBody AddOrUpdateRoleDTO addOrUpdateRoleDTO) {
        return sysRoleService.addOrUpdateRole(addOrUpdateRoleDTO);
    }

    @PostMapping("deleteRole")
    public Response<String> deleteRole(@RequestParam(value = "id") String id) {
        return sysRoleService.deleteRole(id);
    }

    @PostMapping("permission")
    public Response<String> rolePermission(@RequestBody RolePermissionDTO rolePermissionDTO) {
        return sysRoleService.rolePermission(rolePermissionDTO);
    }
}
