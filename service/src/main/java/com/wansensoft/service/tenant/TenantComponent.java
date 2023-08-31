package com.wansensoft.service.tenant;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@TenantResource
public class TenantComponent implements ICommonQuery {

    private final TenantServiceImpl tenantServiceImpl;

    public TenantComponent(TenantServiceImpl tenantServiceImpl) {
        this.tenantServiceImpl = tenantServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return tenantServiceImpl.getTenant(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getTenantList(map);
    }

    private List<?> getTenantList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String loginName = StringUtil.getInfo(search, "loginName");
        String type = StringUtil.getInfo(search, "type");
        String enabled = StringUtil.getInfo(search, "enabled");
        String remark = StringUtil.getInfo(search, "remark");
        return tenantServiceImpl.select(loginName, type, enabled, remark, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String loginName = StringUtil.getInfo(search, "loginName");
        String type = StringUtil.getInfo(search, "type");
        String enabled = StringUtil.getInfo(search, "enabled");
        String remark = StringUtil.getInfo(search, "remark");
        return tenantServiceImpl.countTenant(loginName, type, enabled, remark);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return tenantServiceImpl.insertTenant(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return tenantServiceImpl.updateTenant(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return tenantServiceImpl.deleteTenant(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return tenantServiceImpl.batchDeleteTenant(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return tenantServiceImpl.checkIsNameExist(id, name);
    }

}
