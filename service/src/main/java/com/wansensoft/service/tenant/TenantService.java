package com.wansensoft.service.tenant;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.tenant.Tenant;
import com.wansensoft.entities.tenant.TenantEx;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TenantService extends IService<Tenant> {

    Tenant getTenant(long id);

    List<Tenant> getTenant();

    List<TenantEx> select(String loginName, String type, String enabled, String remark, int offset, int rows);

    Long countTenant(String loginName, String type, String enabled, String remark);

    int insertTenant(JSONObject obj, HttpServletRequest request);

    int updateTenant(JSONObject obj, HttpServletRequest request);

    int deleteTenant(Long id, HttpServletRequest request);

    int batchDeleteTenant(String ids, HttpServletRequest request);

    int checkIsNameExist(Long id, String name);

    Tenant getTenantByTenantId(long tenantId);

    int batchSetStatus(Boolean status, String ids);
}
