/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
