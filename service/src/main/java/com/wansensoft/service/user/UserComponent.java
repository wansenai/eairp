package com.wansensoft.service.user;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.service.ICommonQuery;
import com.wansensoft.utils.Constants;
import com.wansensoft.utils.QueryUtils;
import com.wansensoft.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@UserResource
public class UserComponent implements ICommonQuery {

    private final UserServiceImpl userServiceImpl;

    public UserComponent(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return userServiceImpl.getUser(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getUserList(map);
    }

    private List<?> getUserList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String userName = StringUtil.getInfo(search, "userName");
        String loginName = StringUtil.getInfo(search, "loginName");
        String order = QueryUtils.order(map);
        String filter = QueryUtils.filter(map);
        return userServiceImpl.select(userName, loginName, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String userName = StringUtil.getInfo(search, "userName");
        String loginName = StringUtil.getInfo(search, "loginName");
        return userServiceImpl.countUser(userName, loginName);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return userServiceImpl.insertUser(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return userServiceImpl.updateUser(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return userServiceImpl.deleteUser(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return userServiceImpl.batchDeleteUser(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return userServiceImpl.checkIsNameExist(id, name);
    }

}
