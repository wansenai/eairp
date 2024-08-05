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
package com.wansenai.service.role.impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.dto.role.RoleListDTO
import com.wansenai.entities.role.SysRole
import com.wansenai.entities.role.SysRoleMenuRel
import com.wansenai.vo.RoleVO
import com.wansenai.dto.role.AddOrUpdateRoleDTO
import com.wansenai.dto.role.RolePermissionDTO
import com.wansenai.mappers.role.SysRoleMapper
import com.wansenai.mappers.role.SysRoleMenuRelMapper
import com.wansenai.service.BaseService
import com.wansenai.service.role.SysRoleMenuRelService
import com.wansenai.service.role.SysRoleService
import com.wansenai.utils.SnowflakeIdUtil
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.RoleCodeEnum
import com.wansenai.utils.response.Response
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class SysRoleServiceImpl(
    private val baseService: BaseService,
    private val roleMapper: SysRoleMapper,
    private val roleMenuRelMapper: SysRoleMenuRelMapper,
    private val roleMenuRelService: SysRoleMenuRelService,
) : ServiceImpl<SysRoleMapper, SysRole>(), SysRoleService {

    override fun roleList(): Response<List<RoleVO>> {
        val roles = ArrayList<RoleVO>()

        val sysRoles = lambdaQuery()
            .eq(SysRole::getDeleteFlag, CommonConstants.NOT_DELETED)
            .list()
            // not return platform admin role
            .filter { it.id != 0L }
        sysRoles.forEach { item ->
            val roleVo = RoleVO()
            BeanUtils.copyProperties(item, roleVo)
            roles.add(roleVo)
        }

        return Response.responseData(roles)
    }


    /**
     * 分页查询角色列表
     *
     * @param roleListDTO 角色列表查询条件
     * @return 角色列表
     */
    override fun rolePageList(roleListDTO: RoleListDTO?): Response<Page<RoleVO>> {
        val rolePage = roleListDTO?.let { Page<SysRole>(it.page, it.pageSize) }
        val roleWrapper = LambdaQueryWrapper<SysRole>().apply {
            roleListDTO?.roleName?.let { eq(SysRole::getRoleName, it) }
            roleListDTO?.status?.let { eq(SysRole::getStatus, it) }
            eq(SysRole::getDeleteFlag, CommonConstants.NOT_DELETED)
            orderByDesc(SysRole::getCreateTime)
        }

        val result = rolePage?.run {
            roleMapper.selectPage(this, roleWrapper)
            if (records.isNotEmpty()) {
                val listVo = records.map { role ->
                    RoleVO().apply {
                        BeanUtils.copyProperties(role, this)
                    }
                }
                listVo.forEach { roleVo ->
                    val roleMenuRelList = roleMenuRelMapper.selectList(
                        LambdaQueryWrapper<SysRoleMenuRel>()
                            .eq(SysRoleMenuRel::getRoleId, roleVo.id)
                    )
                    val menuList = ArrayList<Int>()
                    roleMenuRelList.forEach { roleMenuRel ->
                        val menuId = roleMenuRel.menuId
                        val regex = "\\d+".toRegex()
                        val matchResult = regex.findAll(menuId)
                        matchResult.forEach { match ->
                            menuList.add(match.value.toInt())
                        }
                    }
                    roleVo.menuIds = menuList
                }
                Page<RoleVO>().apply {
                    records = listVo
                    total = this@run.total
                    pages = this@run.pages
                    size = this@run.size
                }
            } else {
                Page<RoleVO>()
            }
        } ?: Page<RoleVO>()

        return Response.responseData(result)
    }

    override fun updateStatus(id: String?, status: Int?): Response<String> {
        if (id.isNullOrBlank() || status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }
        val updateResult = lambdaUpdate()
            .eq(SysRole::getId, id)
            .set(SysRole::getStatus, status)
            .update()
        val systemLanguage = baseService.currentUserSystemLanguage
        return if (updateResult) {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_SUCCESS)
            } else {
                Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_SUCCESS_EN)
            }
        } else {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_ERROR)
            } else {
                Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_ERROR_EN)
            }
        }
    }

    override fun addOrUpdateRole(addOrUpdateRoleDTO: AddOrUpdateRoleDTO?): Response<String> {
        val systemLanguage = baseService.currentUserSystemLanguage
        return addOrUpdateRoleDTO?.let { dto ->
            if (dto.id == null) {
                // add
                val sysRole = SysRole().apply {
                    id = SnowflakeIdUtil.nextId()
                    roleName = dto.roleName
                    type = dto.type
                    priceLimit = dto.priceLimit
                    status = dto.status
                    description = dto.description
                    createTime = LocalDateTime.now()
                }

                val saveResult = save(sysRole)
                if (!saveResult) {
                    if (systemLanguage == "zh_CN") {
                        return Response.responseMsg(RoleCodeEnum.ADD_ROLE_ERROR)
                    } else {
                        return Response.responseMsg(RoleCodeEnum.ADD_ROLE_ERROR_EN)
                    }
                } else {
                    if (systemLanguage == "zh_CN") {
                        return Response.responseMsg(RoleCodeEnum.ADD_ROLE_SUCCESS)
                    } else {
                        return Response.responseMsg(RoleCodeEnum.ADD_ROLE_SUCCESS_EN)
                    }
                }
            } else {
                // update
                val updateResult = lambdaUpdate().apply {
                    eq(SysRole::getId, dto.id)
                    set(SysRole::getRoleName, dto.roleName)
                    set(SysRole::getType, dto.type)
                    set(SysRole::getStatus, dto.status)
                    set(SysRole::getPriceLimit, dto.priceLimit)
                    set(SysRole::getDescription, dto.description)
                    set(SysRole::getUpdateTime, LocalDateTime.now())
                }.update()

                if (!updateResult) {
                    if (systemLanguage == "zh_CN") {
                        return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_ERROR)
                    } else {
                        return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_ERROR_EN)
                    }
                } else {
                    if (systemLanguage == "zh_CN") {
                        return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_SUCCESS)
                    } else {
                        return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_SUCCESS_EN)
                    }
                }
            }
        } ?: Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun deleteRole(id: String?): Response<String> {
        id?.let { roleId ->
            if (roleId.isBlank()) {
                return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
            }
            val deleteResult = lambdaUpdate()
                .eq(SysRole::getId, roleId)
                .set(SysRole::getDeleteFlag, CommonConstants.DELETED)
                .update()

            val systemLanguage = baseService.currentUserSystemLanguage
            return if (deleteResult) {
                if (systemLanguage == "zh_CN") {
                    Response.responseMsg(RoleCodeEnum.DELETE_ROLE_SUCCESS)
                } else {
                    Response.responseMsg(RoleCodeEnum.DELETE_ROLE_SUCCESS_EN)
                }
            } else {
                if (systemLanguage == "zh_CN") {
                    Response.responseMsg(RoleCodeEnum.DELETE_ROLE_ERROR)
                } else {
                    Response.responseMsg(RoleCodeEnum.DELETE_ROLE_ERROR_EN)
                }
            }
        }
        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    // 帮我优化rolePermission方法

    override fun rolePermission(rolePermissionDTO: RolePermissionDTO): Response<String> {
        val roleId = rolePermissionDTO.id
        val menuIds = rolePermissionDTO.menuIds
        if (roleId == null || menuIds.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }
        // 先删除原有的角色权限关系
        roleMenuRelService.lambdaUpdate()
            .eq(SysRoleMenuRel::getRoleId, roleId)
            .remove()

        val roleMenuRel = SysRoleMenuRel()
        val menuIdStr = menuIds.joinToString(separator = "") { "[${it}]" }
        roleMenuRel.menuId = menuIdStr
        roleMenuRel.roleId = roleId
        // 进行saveOrUpdate操作
        val saveBatchResult = roleMenuRelService.saveOrUpdate(roleMenuRel);
        val systemLanguage = baseService.currentUserSystemLanguage
        return if (saveBatchResult) {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(RoleCodeEnum.ROLE_PERMISSION_MENU_SUCCESS)
            } else {
                Response.responseMsg(RoleCodeEnum.ROLE_PERMISSION_MENU_SUCCESS_EN)
            }
        } else {
            if (systemLanguage == "zh_CN") {
                Response.responseMsg(RoleCodeEnum.ROLE_PERMISSION_MENU_ERROR)
            } else {
                Response.responseMsg(RoleCodeEnum.ROLE_PERMISSION_MENU_ERROR_EN)
            }
        }
    }
}