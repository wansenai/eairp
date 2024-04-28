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
package com.wansenai.service.system.impl

import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.wansenai.entities.role.SysRoleMenuRel
import com.wansenai.entities.system.SysMenu
import com.wansenai.vo.MenuVO
import com.wansenai.dto.menu.AddOrUpdateMenuDTO
import com.wansenai.mappers.role.SysRoleMenuRelMapper
import com.wansenai.mappers.system.SysMenuMapper
import com.wansenai.service.role.SysRoleMenuRelService
import com.wansenai.service.system.SysMenuService
import com.wansenai.utils.constants.CommonConstants
import com.wansenai.utils.enums.BaseCodeEnum
import com.wansenai.utils.enums.MenuCodeEnum
import com.wansenai.utils.response.Response
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.regex.Pattern

@Service
open class SysMenuServiceImpl(
    private val roleMenuRelService: SysRoleMenuRelService,
    private val userRoleRelService : com.wansenai.service.user.ISysUserRoleRelService,
    private val userService: com.wansenai.service.user.ISysUserService,
    private val roleMenuRelMapper: SysRoleMenuRelMapper,
) :ServiceImpl<SysMenuMapper, SysMenu>(), SysMenuService {

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

    override fun menuList(): Response<JSONObject> {
        val menuData = JSONObject()
        val menuVos = ArrayList<MenuVO>()

        val userId = userService.getCurrentUserId() ?: return Response.fail()
        val roleIds = userRoleRelService.queryByUserId(userId)
            .map { it.roleId }

        if (roleIds.isNotEmpty()) {
            val menusReals = roleMenuRelMapper.listByRoleId(roleIds)
            if (menusReals.isNotEmpty()) {
                val numberList = menusReals.map { it.menuId }
                    .flatMap { item ->
                        val pattern = Pattern.compile("\\d+")
                        val matcher = pattern.matcher(item)
                        val numbers = ArrayList<String>()
                        while (matcher.find()) {
                            numbers.add(matcher.group())
                        }
                        numbers
                    }
                    .distinct()
                    .toList()

                val menus = lambdaQuery()
                    .`in`(SysMenu::getId, numberList)
                    .eq(SysMenu::getDeleteFlag, CommonConstants.NOT_DELETED)
                    .list()

                if (menus.isNotEmpty()) {
                    menus.forEach { menu ->
                        val meta = getMetaJsonObject(menu)
                        val menuVoBuilder = MenuVO.builder()
                            .id(menu.id)
                            .name(menu.name)
                            .title(menu.title)
                            .titleEnglish(menu.titleEnglish)
                            .menuType(menu.menuType)
                            .path(menu.path)
                            .component(menu.component)
                            .icon(menu.icon)
                            .sort(menu.sort)
                            .redirect(menu.redirect)
                            .createTime(menu.createTime)
                            .status(menu.status)
                            .hideMenu(menu.hideMenu)
                            .blank(menu.blank)
                            .ignoreKeepAlive(menu.ignoreKeepAlive)
                            .meta(meta)

                        if (menu.parentId != null) {
                            menuVoBuilder.parentId(menu.parentId)
                        }
                        val menuVo = menuVoBuilder.build()
                        menuVos.add(menuVo)
                    }
                    menuVos.sortBy { it.sort }
                }
                menuData["total"] = menuVos.size
                menuData["data"] = menuVos
            }
        }
        return Response.responseData(menuData)
    }

    private fun getMetaJsonObject(menu: SysMenu): JSONObject {
        val meta = JSONObject()
        meta["title"] = menu.title
        meta["icon"] = menu.icon
        meta["hideBreadcrumb"] = menu.hideBreadcrumb
        meta["hideTab"] = menu.hideTab
        meta["carryParam"] = menu.carryParam
        meta["hideChildrenInMenu"] = menu.hideChildrenInMenu
        meta["affix"] = menu.affix
        meta["frameSrc"] = menu.frameSrc
        meta["realPath"] = menu.realPath
        meta["dynamicLevel"] = 20
        return meta
    }
}