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
package com.wansenai.api.system

import com.wansenai.dto.menu.AddOrUpdateMenuDTO
import com.wansenai.service.system.SysMenuService
import com.wansenai.utils.response.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/menu")
class SysMenuController(private val menuService: SysMenuService) {

    @PostMapping("addOrUpdate")
    fun addOrUpdate(@RequestBody addOrUpdateDeptDTO: AddOrUpdateMenuDTO?): Response<String> {
        return menuService.addOrSaveMenu(addOrUpdateDeptDTO)
    }

    @PostMapping("delete")
    fun deleteMenu(@RequestParam(value = "id", required = true) id: Int?): Response<String> {
        return menuService.deleteMenu(id)
    }
}
