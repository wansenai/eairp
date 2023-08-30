package com.wansensoft.vo;

import com.wansensoft.entities.account.AccountItem;
import lombok.Data;

@Data
public class AccountItemVo4List extends AccountItem {

    private String accountName;

    private String inOutItemName;

    private String billNumber;
}