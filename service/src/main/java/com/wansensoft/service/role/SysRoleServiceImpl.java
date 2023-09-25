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
package com.wansensoft.service.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.role.RoleListDTO;
import com.wansensoft.entities.role.SysRole;
import com.wansensoft.mappers.role.SysRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.utils.enums.BaseCodeEnum;
import com.wansensoft.utils.enums.RoleCodeEnum;
import com.wansensoft.utils.enums.test;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper roleMapper;

    public SysRoleServiceImpl(SysRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Response<List<RoleVO>> roleList() {
        var roles = new ArrayList<RoleVO>();

        var sysRoles = lambdaQuery().list();
        sysRoles.forEach(item -> {
            var roleVo = new RoleVO();
            BeanUtils.copyProperties(item, roleVo);
            roles.add(roleVo);
        });

        return Response.responseData(roles);
    }

    @Override
    public Response<Page<RoleVO>> rolePageList(RoleListDTO roleListDTO) {
        var result = new Page<RoleVO>();
        var listVo = new ArrayList<RoleVO>();

        Page<SysRole> rolePage = new Page<>(roleListDTO.getPage(), roleListDTO.getPageSize());
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(StringUtils.hasText(roleListDTO.getRoleName()), SysRole::getRoleName, roleListDTO.getRoleName());
        roleWrapper.eq(roleListDTO.getStatus() != null, SysRole::getStatus, roleListDTO.getStatus());

        roleMapper.selectPage(rolePage, roleWrapper);
        if(!rolePage.getRecords().isEmpty()) {
            rolePage.getRecords().forEach(role -> {
                var roleVo = new RoleVO();
                BeanUtils.copyProperties(role, roleVo);
                listVo.add(roleVo);
            });
        }
        result.setRecords(listVo);
        result.setTotal(rolePage.getTotal());
        result.setSize(rolePage.getSize());
        result.setPages(rolePage.getPages());

        return Response.responseData(result);
    }

    @Override
    public Response<String> updateStatus(String id, Integer status) {
        if(!StringUtils.hasText(id) && status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var updateResult = lambdaUpdate()
                .eq(SysRole::getId, id)
                .set(SysRole::getStatus, status)
                .update();

        if(!updateResult) {
            return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_ERROR);
        }

        return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_SUCCESS);
    }
}
