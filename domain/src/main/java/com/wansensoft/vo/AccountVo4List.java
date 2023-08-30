package com.wansensoft.vo;

import com.wansensoft.entities.account.Account;
import lombok.Data;

@Data
public class AccountVo4List extends Account {

    private String thisMonthAmount;
}