package com.wansensoft.service.account;

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
@AccountResource
public class AccountComponent implements ICommonQuery {

    private final AccountServiceImpl accountServiceImpl;

    public AccountComponent(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    @Override
    public Object selectOne(Long id) throws Exception {
        return accountServiceImpl.getAccount(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getAccountList(map);
    }

    private List<?> getAccountList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String serialNo = StringUtil.getInfo(search, "serialNo");
        String remark = StringUtil.getInfo(search, "remark");
        String order = QueryUtils.order(map);
        return accountServiceImpl.select(name, serialNo, remark, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String serialNo = StringUtil.getInfo(search, "serialNo");
        String remark = StringUtil.getInfo(search, "remark");
        return accountServiceImpl.countAccount(name, serialNo, remark);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        return accountServiceImpl.insertAccount(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return accountServiceImpl.updateAccount(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return accountServiceImpl.deleteAccount(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return accountServiceImpl.batchDeleteAccount(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return accountServiceImpl.checkIsNameExist(id, name);
    }

}
