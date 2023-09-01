package com.wansensoft.service.accountItem;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.account.AccountItem;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.vo.AccountItemVo4List;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface AccountItemService extends IService<AccountItem> {

    AccountItem getAccountItem(long id);

    List<AccountItem> getAccountItem();

    List<AccountItemVo4List> getDetailList(Long headerId);

    Long countAccountItem(String name, Integer type, String remark);

    int insertAccountItem(JSONObject obj, HttpServletRequest request);

    int updateAccountItem(JSONObject obj, HttpServletRequest request);

    int deleteAccountItem(Long id, HttpServletRequest request);

    int batchDeleteAccountItem(String ids, HttpServletRequest request);

    int checkIsNameExist(Long id, String name);

    int insertAccountItemWithObj(AccountItem accountItem);

    int updateAccountItemWithObj(AccountItem accountItem);

    void saveDetials(String rows, Long headerId, String type, HttpServletRequest request);

    void deleteAccountItemHeadId(Long headerId);

    int batchDeleteAccountItemByIds(String ids);

    BigDecimal getEachAmountByBillId(Long billId);

    List<AccountItem> select(String name, Integer type, String remark, int offset, int rows);
}
