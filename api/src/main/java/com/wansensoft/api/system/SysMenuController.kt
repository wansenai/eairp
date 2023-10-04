package com.wansensoft.api.system

import com.wansensoft.dto.menu.AddOrUpdateMenuDTO
import com.wansensoft.service.system.SysMenuService
import com.wansensoft.utils.response.Response
import org.springframework.web.bind.annotation.*


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