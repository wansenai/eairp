package com.wansensoft.service.role

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.role.AddOrUpdateRoleDTO
import com.wansensoft.dto.role.RoleListDTO
import com.wansensoft.entities.role.SysRole
import com.wansensoft.mappers.role.SysRoleMapper
import com.wansensoft.utils.SnowflakeIdUtil
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.RoleCodeEnum
import com.wansensoft.utils.response.Response
import com.wansensoft.vo.RoleVO
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.time.LocalDateTime

@Service
open class KtSysRoleServiceImpl(private val roleMapper: SysRoleMapper)
    : ServiceImpl<SysRoleMapper, SysRole>(), KtSysRoleService {

    override fun roleList(): Response<List<RoleVO>> {
        val roles = ArrayList<RoleVO>()

        val sysRoles = lambdaQuery()
                .eq(SysRole::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list()
        sysRoles.forEach { item ->
            val roleVo = RoleVO()
            BeanUtils.copyProperties(item, roleVo)
            roles.add(roleVo)
        }

        return Response.responseData(roles)
    }

    override fun rolePageList(roleListDTO: RoleListDTO?): Response<Page<RoleVO>> {
        val rolePage = roleListDTO?.let { Page<SysRole>(it.page, it.pageSize) }
        val roleWrapper = LambdaQueryWrapper<SysRole>().apply {
            roleListDTO?.roleName?.let { eq(SysRole::getRoleName, it) }
            roleListDTO?.status?.let { eq(SysRole::getStatus, it) }
            eq(SysRole::getDeleteFlag, CommonConstants.NOT_DELETED)
        }

        val result = rolePage?.run {
            roleMapper.selectPage(this, roleWrapper)
            if (records.isNotEmpty()) {
                val listVo = records.map { role ->
                    RoleVO().apply {
                        BeanUtils.copyProperties(role, this)
                    }
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

        return if (updateResult) {
            Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_SUCCESS)
        } else {
            Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_STATUS_ERROR)
        }
    }

    override fun addOrUpdateRole(addOrUpdateRoleDTO: AddOrUpdateRoleDTO?): Response<String> {
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
                    Response.responseMsg(RoleCodeEnum.ADD_ROLE_ERROR)
                } else {
                    Response.responseMsg(RoleCodeEnum.ADD_ROLE_SUCCESS)
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
                    Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_ERROR)
                } else {
                    Response.responseMsg(RoleCodeEnum.UPDATE_ROLE_SUCCESS)
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

            return if (deleteResult) {
                Response.responseMsg(RoleCodeEnum.DELETE_ROLE_SUCCESS)
            } else {
                Response.responseMsg(RoleCodeEnum.DELETE_ROLE_ERROR)
            }
        }
        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }


}