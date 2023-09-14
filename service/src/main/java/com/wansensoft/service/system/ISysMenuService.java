package com.wansensoft.service.system;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.system.SysMenu;
import com.wansensoft.utils.response.Response;

/**
 * <p>
 * 功能模块表 服务类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
public interface ISysMenuService extends IService<SysMenu> {

    Response<JSONObject> menuList();

    String test();
}
