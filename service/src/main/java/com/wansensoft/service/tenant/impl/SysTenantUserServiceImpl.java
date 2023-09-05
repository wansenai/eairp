package com.wansensoft.service.tenant.impl;

import com.wansensoft.service.tenant.ISysTenantUserService;
import com.wansensoft.entities.SysTenantUser;
import com.wansensoft.mappers.tenant.SysTenantUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysTenantUserServiceImpl extends ServiceImpl<SysTenantUserMapper, SysTenantUser> implements ISysTenantUserService {

}
