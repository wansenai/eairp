package com.wansensoft.tenant;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.tenant.Tenant;
import com.wansensoft.entities.tenant.TenantEx;
import com.wansensoft.entities.tenant.TenantExample;

public interface TenantExampleService extends IService<TenantEx> {

    Tenant getTenantByTenantId(long tenantId);
}
