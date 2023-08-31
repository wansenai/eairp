package com.wansensoft.service.role;

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
@RoleResource
public class RoleComponent implements ICommonQuery {

    private final RoleServiceImpl roleServiceImpl;

    public RoleComponent(RoleServiceImpl roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return roleServiceImpl.getRole(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getRoleList(map);
    }

    private List<?> getRoleList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String description = StringUtil.getInfo(search, "description");
        return roleServiceImpl.select(name, description, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String description = StringUtil.getInfo(search, "description");
        return roleServiceImpl.countRole(name, description);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return roleServiceImpl.insertRole(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return roleServiceImpl.updateRole(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return roleServiceImpl.deleteRole(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return roleServiceImpl.batchDeleteRole(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return roleServiceImpl.checkIsNameExist(id, name);
    }

}
