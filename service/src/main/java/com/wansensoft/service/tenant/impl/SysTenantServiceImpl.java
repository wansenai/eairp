package com.wansensoft.service.tenant.impl;

import com.wansensoft.service.tenant.ISysTenantService;
import com.wansensoft.entities.SysTenant;
import com.wansensoft.mappers.tenant.SysTenantMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

}
