package com.wansensoft.service.role;

import com.wansensoft.entities.role.SysRoleMenuRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
public interface ISysRoleMenuRelService extends IService<SysRoleMenuRel> {

    List<SysRoleMenuRel> listByRoleId(Long roleId);
}
