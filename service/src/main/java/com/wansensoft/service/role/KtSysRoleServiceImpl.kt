package com.wansensoft.service.role

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.role.RoleListDTO
import com.wansensoft.entities.role.SysRole
import com.wansensoft.mappers.role.SysRoleMapper
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.RoleCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.RoleVO
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
open class KtSysRoleServiceImpl(private val roleMapper: SysRoleMapper)
    : ServiceImpl<SysRoleMapper, SysRole>(), KtSysRoleService {

    override fun roleList(): Response<List<RoleVO>> {
        val roles = ArrayList<RoleVO>()

        val sysRoles = lambdaQuery().list()
        sysRoles.forEach { item ->
            val roleVo = RoleVO()
            BeanUtils.copyProperties(item, roleVo)
            roles.add(roleVo)
        }

        return Response.responseData(roles)
    }

    override fun rolePageList(roleListDTO: RoleListDTO?): Response<Page<RoleVO>> {
        val result = Page<RoleVO>()
        val listVo = ArrayList<RoleVO>()

        val rolePage = roleListDTO?.let { Page<SysRole>(roleListDTO.page.toLong(), it.pageSize.toLong()) }
        val roleWrapper = LambdaQueryWrapper<SysRole>()

            roleWrapper.eq(StringUtils.hasText(roleListDTO!!.roleName), SysRole::getRoleName, roleListDTO.roleName)

        roleWrapper.eq(roleListDTO.status != null, SysRole::getStatus, roleListDTO.status)

        roleMapper.selectPage(rolePage, roleWrapper)
        if (rolePage!!.records.isNotEmpty()) {
            rolePage.records.forEach { role ->
                val roleVo = RoleVO()
                BeanUtils.copyProperties(role, roleVo)
                listVo.add(roleVo)
            }
        }
        result.records = listVo
        result.total = rolePage.total
        result.size = rolePage.size
        result.pages = rolePage.pages

        return Response.responseData(result)
    }

    override fun updateStatus(id: String?, status: Int?): Response<String> {
        if (!StringUtils.hasText(id) && status == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
        }

        val updateResult = lambdaUpdate()
                .eq(SysRole::getId, id)
                .set(SysRole::getStatus, status)
                .update()

        if (!updateResult) {
            return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_ERROR)
        }

        return Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_SUCCESS)
    }
}