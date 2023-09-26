package com.wansensoft.service.accountHead;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wansensoft.entities.account.AccountHead;
import com.wansensoft.entities.account.AccountHeadVo4ListEx;
import com.wansensoft.entities.account.AccountItem;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AccountHeadService extends IService<AccountHead> {

    AccountHead getAccountHead(long id);

    List<AccountHead> getAccountHeadListByIds(String ids);

    List<AccountHead> getAccountHead();

    List<AccountHeadVo4ListEx> select(String type, String roleType, String billNo, String beginTime, String endTime,
                                      Long organId, Long creator, Long handsPersonId, Long accountId, String status,
                                      String remark, String number, int offset, int rows);

    Long countAccountHead(String type, String roleType, String billNo, String beginTime, String endTime,
                          Long organId, Long creator, Long handsPersonId, Long accountId, String status,
                          String remark, String number);

    int insertAccountHead(JSONObject obj, HttpServletRequest request);

    int updateAccountHead(JSONObject obj, HttpServletRequest request);

    int deleteAccountHead(Long id, HttpServletRequest request);

    int batchDeleteAccountHead(String ids, HttpServletRequest request);

    int batchDeleteAccountHeadByIds(String ids);

    int checkIsBillNoExist(Long id, String billNo);

    int batchSetStatus(String status, String accountHeadIds);

    void addAccountHeadAndDetail(String beanJson, String rows, HttpServletRequest request);

    void updateAccountHeadAndDetail(String beanJson, String rows, HttpServletRequest request);

    List<AccountHeadVo4ListEx> getDetailByNumber(String billNo);

    List<AccountItem> getFinancialBillNoByBillIdList(List<Long> idList);

    List<AccountHead> getFinancialBillNoByBillId(Long billId);
}
