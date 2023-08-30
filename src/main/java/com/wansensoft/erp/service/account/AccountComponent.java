package com.wansensoft.erp.service.account;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.erp.service.ICommonQuery;
import com.wansensoft.erp.utils.Constants;
import com.wansensoft.erp.utils.QueryUtils;
import com.wansensoft.erp.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "account_component")
@AccountResource
public class AccountComponent implements ICommonQuery {

    @Resource
    private AccountService accountService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return accountService.getAccount(id);
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
        return accountService.select(name, serialNo, remark, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String serialNo = StringUtil.getInfo(search, "serialNo");
        String remark = StringUtil.getInfo(search, "remark");
        return accountService.countAccount(name, serialNo, remark);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        return accountService.insertAccount(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return accountService.updateAccount(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return accountService.deleteAccount(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return accountService.batchDeleteAccount(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return accountService.checkIsNameExist(id, name);
    }

}
