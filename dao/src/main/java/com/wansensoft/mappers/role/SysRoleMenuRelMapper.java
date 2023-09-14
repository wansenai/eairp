package com.wansensoft.mappers.role;

import com.wansensoft.entities.role.SysRoleMenuRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
public interface SysRoleMenuRelMapper extends BaseMapper<SysRoleMenuRel> {

    List<SysRoleMenuRel> listByRoleId(@Param("roleIds") List<Long> roleIds);
}
