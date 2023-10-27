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