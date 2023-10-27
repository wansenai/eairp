package com.wansenai.service.user.impl;

import com.wansenai.service.user.ISysUserRoleRelService;
import com.wansenai.entities.user.SysUserRoleRel;
import com.wansenai.mappers.user.SysUserRoleRelMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysUserRoleRelServiceImpl extends ServiceImpl<SysUserRoleRelMapper, SysUserRoleRel> implements ISysUserRoleRelService {

    @Override
    public List<SysUserRoleRel> queryByUserId(long userId) {
        return lambdaQuery()
                .eq(SysUserRoleRel::getUserId, userId)
                .list();
    }

    @Override
    public List<SysUserRoleRel> queryBatchByUserIds(List<Long> userIds) {
        return lambdaQuery()
                .in(SysUserRoleRel::getUserId, userIds)
                .list();
    }
}
