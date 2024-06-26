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
package com.wansenai.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.dto.tenant.AddOrUpdateTenantDTO;
import com.wansenai.dto.tenant.TenantListDTO;
import com.wansenai.entities.SysDepartment;
import com.wansenai.entities.role.SysRole;
import com.wansenai.entities.role.SysRoleMenuRel;
import com.wansenai.entities.user.SysUser;
import com.wansenai.entities.user.SysUserDeptRel;
import com.wansenai.entities.user.SysUserRoleRel;
import com.wansenai.mappers.role.SysRoleMapper;
import com.wansenai.mappers.role.SysRoleMenuRelMapper;
import com.wansenai.mappers.system.SysDepartmentMapper;
import com.wansenai.service.tenant.ISysTenantService;
import com.wansenai.entities.tenant.SysTenant;
import com.wansenai.mappers.tenant.SysTenantMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.service.user.ISysUserDeptRelService;
import com.wansenai.service.user.ISysUserRoleRelService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.service.user.impl.SysUserServiceImpl;
import com.wansenai.utils.CommonTools;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.utils.TimeUtil;
import com.wansenai.utils.constants.CommonConstants;
import com.wansenai.utils.constants.MenuConstants;
import com.wansenai.utils.constants.UserConstants;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.TenantCodeEnum;
import com.wansenai.utils.enums.UserCodeEnum;
import com.wansenai.utils.response.Response;
import com.wansenai.vo.TenantInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 租户 服务实现类
 * </p>
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    private final SysTenantMapper tenantMapper;

    private final SysUserServiceImpl sysUserServiceImpl;

    private final ISysUserService sysUserService;

    private final ISysUserRoleRelService userRoleRelService;

    private final ISysUserDeptRelService userDeptRelService;

    private final SysDepartmentMapper departmentMapper;

    private final SysRoleMapper roleMapper;

    private final SysRoleMenuRelMapper roleMenuRelMapper;

    public SysTenantServiceImpl(SysTenantMapper tenantMapper, SysUserServiceImpl sysUserServiceImpl, ISysUserService sysUserService, ISysUserRoleRelService userRoleRelService, ISysUserDeptRelService userDeptRelService, SysDepartmentMapper departmentMapper, SysRoleMapper roleMapper, SysRoleMenuRelMapper roleMenuRelMapper) {
        this.tenantMapper = tenantMapper;
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.sysUserService = sysUserService;
        this.userRoleRelService = userRoleRelService;
        this.userDeptRelService = userDeptRelService;
        this.departmentMapper = departmentMapper;
        this.roleMapper = roleMapper;
        this.roleMenuRelMapper = roleMenuRelMapper;
    }

    @Override
    public Response<Page<TenantInfoVO>> tenantList(TenantListDTO tenantListDTO) {
        var result = new Page<TenantInfoVO>();
        var tenantList = new ArrayList<TenantInfoVO>();

        Page<SysTenant> page = new Page<>(tenantListDTO.getPage(), tenantListDTO.getPageSize());
        LambdaQueryWrapper<SysTenant> query = new LambdaQueryWrapper<>();
        query.eq(StringUtils.hasText(tenantListDTO.getTenantName()), SysTenant::getName, tenantListDTO.getTenantName());
        query.eq(tenantListDTO.getType() != null, SysTenant::getType, tenantListDTO.getType());
        query.eq(tenantListDTO.getStatus() != null, SysTenant::getStatus, tenantListDTO.getStatus());
        query.orderByDesc(SysTenant::getCreateTime);
        tenantMapper.selectPage(page, query);

        page.getRecords().forEach(tenant -> {
            System.out.println(tenant.getTenantId());
            var userInfo = sysUserServiceImpl.lambdaQuery()
                    .eq(SysUser::getTenantId, tenant.getTenantId())
                    .eq(SysUser::getId, tenant.getUserId())
                    .one();

            tenantList.add(TenantInfoVO.builder()
                    .id(tenant.getId())
                    .username(userInfo.getUserName())
                    .password(userInfo.getPassword())
                    .email(userInfo.getEmail())
                    .phoneNumber(userInfo.getPhoneNumber())
                    .roleId(userRoleRelService.queryByUserId(userInfo.getId()).stream()
                                    .map(SysUserRoleRel::getRoleId)
                                    .toList())
                    .deptId(userDeptRelService.queryByUserId(userInfo.getId()).stream()
                                    .map(SysUserDeptRel::getDeptId)
                                    .toList())
                    .tenantName(tenant.getName())
                    .tenantId(tenant.getTenantId())
                    .userNumLimit(tenant.getUserNumLimit())
                    .type(tenant.getType())
                    .status(tenant.getStatus())
                    .remark(tenant.getRemark())
                    .createTime(tenant.getCreateTime())
                    .expireTime(tenant.getExpireTime())
                    .build());
        });
        result.setRecords(tenantList);
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());

        return Response.responseData(result);
    }

    @Override
    @Transactional
    public Response<String> addOrUpdate(AddOrUpdateTenantDTO addOrUpdateTenantDTO) {
        if (addOrUpdateTenantDTO.getId() == null) {
            // New add tenant
            if (sysUserServiceImpl.checkUserNameExist(addOrUpdateTenantDTO.getUsername())) {
                return Response.responseMsg(UserCodeEnum.USER_NAME_EXISTS);
            }

            if (sysUserServiceImpl.checkPhoneNumberExist(addOrUpdateTenantDTO.getPhoneNumber())) {
                return Response.responseMsg(UserCodeEnum.PHONE_EXISTS);
            }
            var tenantId = SnowflakeIdUtil.nextId();
            var password = "";
            if (StringUtils.hasText(addOrUpdateTenantDTO.getPassword())) {
                password = CommonTools.md5Encryp(addOrUpdateTenantDTO.getPassword());
            } else {
                password = CommonTools.md5Encryp(UserConstants.DEFAULT_PASSWORD);
            }

            var user = SysUser.builder()
                    .id(tenantId)
                    .userName(addOrUpdateTenantDTO.getUsername())
                    .password(password)
                    .name(addOrUpdateTenantDTO.getTenantName())
                    .phoneNumber(addOrUpdateTenantDTO.getPhoneNumber())
                    .email(addOrUpdateTenantDTO.getEmail())
                    .createBy(tenantId)
                    .createTime(LocalDateTime.now())
                    .description(addOrUpdateTenantDTO.getRemark())
                    .tenantId(tenantId)
                    .build();

            var saveUserResult = sysUserService.save(user);

            // Query System Role copy -> tenant default add role
            var systemDefaultRole = roleMapper.selectById(1);

            var role = SysRole.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .tenantId(tenantId)
                    .roleName("管理员")
                    .type("全部数据")
                    .description(systemDefaultRole.getDescription() + tenantId)
                    .createBy(0L)
                    .createTime(LocalDateTime.now())
                    .status(systemDefaultRole.getStatus())
                    .build();
            var saveRoleResult = roleMapper.insert(role);

            var userRoleRel = SysUserRoleRel.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .roleId(role.getId())
                    .userId(tenantId)
                    .tenantId(tenantId)
                    .createBy(tenantId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveUserRoleRealResult = userRoleRelService.save(userRoleRel);

            var roleMenuRel = SysRoleMenuRel.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .tenantId(tenantId)
                    .roleId(role.getId())
                    .menuId(MenuConstants.DEFAULT_TENANT_ROLE_MENU)
                    .createBy(tenantId)
                    .createTime(LocalDateTime.now())
                    .build();
            roleMenuRelMapper.insert(roleMenuRel);

            var department = SysDepartment.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .tenantId(tenantId)
                    .number("DT0001")
                    .name("默认部门")
                    .remark("Default Department")
                    .sort("0")
                    .status(CommonConstants.NOT_DELETED)
                    .build();
            var saveDepartmentResult = departmentMapper.insert(department);

            var userDeptRel = SysUserDeptRel.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .deptId(department.getId())
                    .userId(tenantId)
                    .tenantId(tenantId)
                    .createBy(tenantId)
                    .createTime(LocalDateTime.now())
                    .build();
            var saveUserDeptRealResult = userDeptRelService.save(userDeptRel);

            if (saveUserResult && saveUserRoleRealResult && saveUserDeptRealResult && saveRoleResult > 0 && saveDepartmentResult > 0) {
                SysTenant tenant = new SysTenant();
                tenant.setId(tenantId);
                tenant.setName(addOrUpdateTenantDTO.getTenantName());
                tenant.setTenantId(tenantId);
                tenant.setUserId(tenantId);
                tenant.setUserNumLimit(addOrUpdateTenantDTO.getUserNumLimit());
                tenant.setType(addOrUpdateTenantDTO.getType());
                tenant.setStatus(CommonConstants.NOT_DELETED);
                tenant.setRemark(addOrUpdateTenantDTO.getRemark());
                tenant.setCreateTime(LocalDateTime.now());
                tenant.setExpireTime(TimeUtil.parse(addOrUpdateTenantDTO.getExpireTime()));
                var result = tenantMapper.insert(tenant);
                if (result > 0) {
                    return Response.responseMsg(TenantCodeEnum.TENANT_ADD_SUCCESS);
                }
                return Response.responseMsg(TenantCodeEnum.TENANT_ADD_ERROR);
            }
        } else {
            var updateUserResult = sysUserServiceImpl.lambdaUpdate()
                    .eq(SysUser::getId, addOrUpdateTenantDTO.getId())
                    .set(StringUtils.hasText(addOrUpdateTenantDTO.getTenantName()), SysUser::getName, addOrUpdateTenantDTO.getTenantName())
                    .set(StringUtils.hasText(addOrUpdateTenantDTO.getPhoneNumber()), SysUser::getPhoneNumber, addOrUpdateTenantDTO.getPhoneNumber())
                    .set(StringUtils.hasText(addOrUpdateTenantDTO.getEmail()), SysUser::getEmail, addOrUpdateTenantDTO.getEmail())
                    .set(StringUtils.hasText(addOrUpdateTenantDTO.getRemark()), SysUser::getRemark, addOrUpdateTenantDTO.getRemark())
                    .update();
            if (updateUserResult) {
                var updateTenantResult = lambdaUpdate()
                        .eq(SysTenant::getId, addOrUpdateTenantDTO.getId())
                        .set(StringUtils.hasText(addOrUpdateTenantDTO.getTenantName()), SysTenant::getName, addOrUpdateTenantDTO.getTenantName())
                        .set(addOrUpdateTenantDTO.getUserNumLimit() != null, SysTenant::getUserNumLimit, addOrUpdateTenantDTO.getUserNumLimit())
                        .set(addOrUpdateTenantDTO.getType() != null, SysTenant::getType, addOrUpdateTenantDTO.getType())
                        .set(addOrUpdateTenantDTO.getStatus() != null, SysTenant::getStatus, addOrUpdateTenantDTO.getStatus())
                        .set(StringUtils.hasText(addOrUpdateTenantDTO.getRemark()), SysTenant::getRemark, addOrUpdateTenantDTO.getRemark())
                        .set(StringUtils.hasLength(addOrUpdateTenantDTO.getExpireTime()), SysTenant::getExpireTime,
                                addOrUpdateTenantDTO.getExpireTime() != null ? TimeUtil.parse(addOrUpdateTenantDTO.getExpireTime()) : null)
                        .set(SysTenant::getUpdateTime, LocalDateTime.now())
                        .update();
                if (updateTenantResult) {
                    return Response.responseMsg(TenantCodeEnum.TENANT_INFO_UPDATE_SUCCESS);
                }
                return Response.responseMsg(TenantCodeEnum.TENANT_INFO_UPDATE_ERROR);
            }
        }
        return Response.success();
    }

    @Override
    public Response<String> checkAddUser() {
        var userNum = sysUserServiceImpl.lambdaQuery()
                .eq(SysUser::getTenantId, sysUserService.getCurrentTenantId())
                .count();

        var tenant = tenantMapper.selectById(sysUserService.getCurrentTenantId());
        if (tenant.getUserNumLimit() != 0 && userNum >= tenant.getUserNumLimit()) {
            return Response.responseMsg(TenantCodeEnum.TENANT_USER_NUM_LIMIT);
        }
        return Response.success();
    }

    @Override
    public Boolean checkTenantExpire(Long tenantId) {
        var tenant = tenantMapper.selectById(tenantId);
        return tenant.getExpireTime().isBefore(LocalDateTime.now());
    }

    @Override
    public Response<String> update(AddOrUpdateTenantDTO updateDTO) {
        if (updateDTO.getId() == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var updateResult = lambdaUpdate()
                .eq(SysTenant::getId, updateDTO.getId())
                .set(StringUtils.hasText(updateDTO.getTenantName()), SysTenant::getName, updateDTO.getTenantName())
                .set(updateDTO.getUserNumLimit() != null, SysTenant::getUserNumLimit, updateDTO.getUserNumLimit())
                .set(updateDTO.getType() != null, SysTenant::getType, updateDTO.getType())
                .set(updateDTO.getStatus() != null, SysTenant::getStatus, updateDTO.getStatus())
                .set(StringUtils.hasText(updateDTO.getRemark()), SysTenant::getRemark, updateDTO.getRemark())
                .set(StringUtils.hasLength(updateDTO.getExpireTime()), SysTenant::getExpireTime,
                        updateDTO.getExpireTime() != null ? TimeUtil.parse(updateDTO.getExpireTime()) : null)
                .set(SysTenant::getUpdateTime, LocalDateTime.now())
                .update();

        // 如果修改的是状态，需要修改用户状态
        if (updateDTO.getStatus() != null) {
            // 查询出该租户下的所有用户 并修改状态
            var userIds = sysUserServiceImpl.lambdaQuery()
                    .eq(SysUser::getTenantId, updateDTO.getId())
                    .list()
                    .stream()
                    .map(SysUser::getId)
                    .toList();
            sysUserServiceImpl.lambdaUpdate()
                    .in(SysUser::getId, userIds)
                    .set(SysUser::getStatus, updateDTO.getStatus())
                    .update();
        }

        if (updateResult) {
            return Response.responseMsg(TenantCodeEnum.TENANT_INFO_UPDATE_SUCCESS);
        }
        return Response.responseMsg(TenantCodeEnum.TENANT_INFO_UPDATE_ERROR);
    }

    /**
     *  Only the data in the tenant table will be physically deleted here,
     *  but the user data under the tenant will be logically deleted for data retention
     * @param tenantId tenant id
     * @return Delete result
     */
    @Override
    public Response<String> delete(String tenantId) {
        var userIds = sysUserServiceImpl.lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .list()
                .stream()
                .map(SysUser::getId)
                .toList();
        sysUserServiceImpl.lambdaUpdate()
                .in(SysUser::getId, userIds)
                .set(SysUser::getDeleteFlag, CommonConstants.DELETED)
                .update();

        var deleteResult = tenantMapper.deleteById(tenantId);
        if (deleteResult > 0) {
            return Response.responseMsg(TenantCodeEnum.TENANT_DELETE_SUCCESS);
        }
        return Response.responseMsg(TenantCodeEnum.TENANT_DELETE_ERROR);
    }
}
