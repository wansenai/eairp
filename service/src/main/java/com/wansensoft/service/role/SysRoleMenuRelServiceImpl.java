package com.wansensoft.service.role;

import com.wansensoft.entities.role.SysRoleMenuRel;
import com.wansensoft.mappers.role.SysRoleMenuRelMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysRoleMenuRelServiceImpl extends ServiceImpl<SysRoleMenuRelMapper, SysRoleMenuRel> implements ISysRoleMenuRelService {

    @Override
    public List<SysRoleMenuRel> listByRoleId(Long roleId) {
        return lambdaQuery()
                .eq(SysRoleMenuRel::getRoleId, roleId)
                .list();
    }
}
