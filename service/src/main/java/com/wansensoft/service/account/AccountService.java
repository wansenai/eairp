package com.wansensoft.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.account.Account;

import java.util.Map;

public interface AccountService extends IService<Account> {

    Map<Long,String> getAccountMap();

    String getAccountStrByIdAndMoney(Map<Long,String> accountMap, String accountIdList, String accountMoneyList);
}
