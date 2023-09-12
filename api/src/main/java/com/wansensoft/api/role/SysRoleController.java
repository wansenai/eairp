package com.wansensoft.api.role;


import com.wansensoft.service.system.ISysMenuService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.MenuVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("sysRole")
public class SysRoleController {

    private final ISysMenuService menuService;

    public SysRoleController(ISysMenuService menuService) {
        this.menuService = menuService;
    }


    @GetMapping("menu")
    public String queryMenu() {
        return menuService.test();
    }
}
