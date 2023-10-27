package com.wansenai.service.role

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.wansenai.dto.role.AddOrUpdateRoleDTO
import com.wansenai.dto.role.RoleListDTO
import com.wansenai.dto.role.RolePermissionDTO
import com.wansenai.entities.role.SysRole
import com.wansenai.vo.RoleVO
import com.wansenai.utils.response.Response

interface SysRoleService : IService<SysRole> {

    fun roleList() : Response<List<RoleVO>>

    fun rolePageList(roleListDTO: RoleListDTO?) : Response<Page<RoleVO>>

    fun updateStatus(id: String?, status: Int?) : Response<String>

    fun addOrUpdateRole(addOrUpdateRoleDTO : AddOrUpdateRoleDTO?) : Response<String>

    fun deleteRole(id: String?): Response<String>

    fun rolePermission(rolePermissionDTO: RolePermissionDTO): Response<String>
}