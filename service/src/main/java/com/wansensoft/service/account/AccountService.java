package com.wansensoft.service.account;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.account.Account;
import com.wansensoft.vo.AccountVo4InOutList;
import com.wansensoft.vo.AccountVo4List;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountService extends IService<Account> {

    Account getAccount(long id);

    List<Account> getAccount();

    List<Account> getAccountByParam(String name, String serialNo);

    List<AccountVo4List> select(String name, String serialNo, String remark, int offset, int rows);

    Long countAccount(String name, String serialNo, String remark);

    int insertAccount(JSONObject obj, HttpServletRequest request);

    int updateAccount(JSONObject obj, HttpServletRequest request);

    int deleteAccount(Long id, HttpServletRequest request);

    int batchDeleteAccount(String ids, HttpServletRequest request);

    int batchDeleteAccountByIds(String ids);

    int checkIsNameExist(Long id, String name);

    List<Account> findBySelect();

    BigDecimal getAccountSum(Long id, String timeStr, String type, Boolean forceFlag);

    BigDecimal getAccountSumByHead(Long id, String timeStr, String type, Boolean forceFlag);

    BigDecimal getAccountSumByDetail(Long id, String timeStr, String type, Boolean forceFlag);

    BigDecimal getManyAccountSum(Long id, String timeStr, String type, Boolean forceFlag);

    List<AccountVo4InOutList> findAccountInOutList(Long accountId, Integer offset, Integer rows);

    int findAccountInOutListCount(Long accountId);

    int updateIsDefault(Long accountId);

    Map<Long,String> getAccountMap();

    String getAccountStrByIdAndMoney(Map<Long,String> accountMap, String accountIdList, String accountMoneyList);

    Map<String, Object> getStatistics(String name, String serialNo);

    int batchSetStatus(Boolean status, String ids);
}
