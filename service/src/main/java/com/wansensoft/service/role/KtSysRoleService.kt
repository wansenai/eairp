package com.wansensoft.service.role

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansensoft.dto.role.AddOrUpdateRoleDTO
import com.wansensoft.dto.role.RoleListDTO
import com.wansensoft.dto.role.RolePermissionDTO
import com.wansensoft.entities.role.SysRole
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.RoleVO

interface KtSysRoleService : IService<SysRole> {

    fun roleList() : Response<List<RoleVO>>

    fun rolePageList(roleListDTO: RoleListDTO?) : Response<Page<RoleVO>>

    fun updateStatus(id: String?, status: Int?) : Response<String>

    fun addOrUpdateRole(addOrUpdateRoleDTO : AddOrUpdateRoleDTO?) : Response<String>

    fun deleteRole(id: String?): Response<String>

    fun rolePermission(rolePermissionDTO: RolePermissionDTO): Response<String>
}