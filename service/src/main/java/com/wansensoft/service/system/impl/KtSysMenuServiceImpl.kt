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
package com.wansensoft.service.system.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansensoft.dto.menu.AddOrUpdateMenuDTO
import com.wansensoft.entities.role.SysRoleMenuRel
import com.wansensoft.entities.system.SysMenu
import com.wansensoft.mappers.system.SysMenuMapper
import com.wansensoft.service.role.KtSysRoleMenuRelService
import com.wansensoft.service.system.KtSysMenuService
import com.wansensoft.utils.constants.CommonConstants
import com.wansensoft.utils.enums.BaseCodeEnum
import com.wansensoft.utils.enums.MenuCodeEnum
import com.wansensoft.utils.response.Response
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
open class KtSysMenuServiceImpl(
    private val roleMenuRelService: KtSysRoleMenuRelService,
) :ServiceImpl<SysMenuMapper, SysMenu>(), KtSysMenuService {

    @Transactional
    override fun addOrSaveMenu(addOrUpdateMenuDTO: AddOrUpdateMenuDTO?): Response<String> {
        addOrUpdateMenuDTO?.let { dto ->
            if (dto.id == null) {
                val menu = SysMenu().apply{
                    name = dto.name
                    title = dto.title
                    icon = dto.icon
                    parentId = dto.parentId
                    menuType = dto.menuType
                    path = dto.path
                    component = dto.component
                    status = dto.status
                    sort = dto.sort
                    hideMenu = dto.hideMenu
                    ignoreKeepAlive = dto.ignoreKeepAlive
                    blank = dto.blank
                    createTime = LocalDateTime.now()
                }
                val saveResult = save(menu);
                if (!saveResult) {
                    return Response.responseMsg(MenuCodeEnum.ADD_MENU_ERROR)
                } else {
                    // Add this menu to the administrator by default
                    val menuIds = StringBuilder()
                    menuIds.append(roleMenuRelService.getById(0).menuId)
                    menuIds.append("[" + menu.id + "]")
                    roleMenuRelService.lambdaUpdate()
                        .eq(SysRoleMenuRel::getRoleId, 0)
                        .set(SysRoleMenuRel::getMenuId, menuIds.toString())
                        .update()
                    return Response.responseMsg(MenuCodeEnum.ADD_MENU_SUCCESS)
                }
            } else {
                // update
                val updateResult = lambdaUpdate().apply {
                    eq(SysMenu::getId, dto.id)
                    set(SysMenu::getName, dto.name)
                    set(SysMenu::getTitle, dto.title)
                    set(SysMenu::getIcon, dto.icon)
                    set(SysMenu::getParentId, dto.parentId)
                    set(SysMenu::getMenuType, dto.menuType)
                    set(SysMenu::getPath, dto.path)
                    set(SysMenu::getComponent, dto.component)
                    set(SysMenu::getStatus, dto.status)
                    set(SysMenu::getSort, dto.sort)
                    set(SysMenu::getHideMenu, dto.hideMenu)
                    set(SysMenu::getIgnoreKeepAlive, dto.ignoreKeepAlive)
                    set(SysMenu::getBlank, dto.blank)
                    set(SysMenu::getUpdateTime, LocalDateTime.now())
                }.update()

                if (!updateResult) {
                    return Response.responseMsg(MenuCodeEnum.UPDATE_MENU_ERROR)
                } else {
                    return Response.responseMsg(MenuCodeEnum.UPDATE_MENU_SUCCESS)
                }
            }
        }
        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }

    override fun deleteMenu(id: Int?): Response<String> {
        id?.let {
            val deleteResult = lambdaUpdate()
                .eq(SysMenu::getId, id)
                .set(SysMenu::getDeleteFlag, CommonConstants.DELETED)
                .update()

            return if (deleteResult) {
                Response.responseMsg(MenuCodeEnum.DELETE_MENU_SUCCESS)
            } else {
                Response.responseMsg(MenuCodeEnum.DELETE_MENU_ERROR)
            }
        }
        return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL)
    }
}