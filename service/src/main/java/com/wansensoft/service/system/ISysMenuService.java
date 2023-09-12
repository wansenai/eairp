package com.wansensoft.service.system;

import com.wansensoft.entities.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.MenuVo;

import java.util.List;

/**
 * <p>
 * 功能模块表 服务类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
public interface ISysMenuService extends IService<SysMenu> {

    Response<List<MenuVo>> menuList();

    String test();
}
