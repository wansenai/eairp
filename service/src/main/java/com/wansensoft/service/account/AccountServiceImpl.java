package com.wansensoft.service.account;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.account.*;
import com.wansensoft.entities.depot.DepotHead;
import com.wansensoft.entities.depot.DepotHeadExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.account.*;
import com.wansensoft.mappers.depot.DepotHeadMapper;
import com.wansensoft.mappers.depot.DepotHeadMapperEx;
import com.wansensoft.service.CommonService;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.systemConfig.SystemConfigService;
import com.wansensoft.service.user.UserService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.utils.constants.ExceptionConstants;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.utils.Tools;
import com.wansensoft.vo.AccountVo4InOutList;
import com.wansensoft.vo.AccountVo4List;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountMapper accountMapper;
    private final AccountMapperEx accountMapperEx;
    private final DepotHeadMapper depotHeadMapper;
    private final DepotHeadMapperEx depotHeadMapperEx;
    private final AccountHeadMapper accountHeadMapper;
    private final AccountHeadMapperEx accountHeadMapperEx;
    private final AccountItemMapper accountItemMapper;
    private final AccountItemMapperEx accountItemMapperEx;
    private final LogService logService;
    private final CommonService commonService;
    private final SystemConfigService systemConfigService;

    public AccountServiceImpl(AccountMapper accountMapper, AccountMapperEx accountMapperEx, DepotHeadMapper depotHeadMapper, DepotHeadMapperEx depotHeadMapperEx, AccountHeadMapper accountHeadMapper, AccountHeadMapperEx accountHeadMapperEx, AccountItemMapper accountItemMapper, AccountItemMapperEx accountItemMapperEx, LogService logService, CommonService commonService, SystemConfigService systemConfigService) {
        this.accountMapper = accountMapper;
        this.accountMapperEx = accountMapperEx;
        this.depotHeadMapper = depotHeadMapper;
        this.depotHeadMapperEx = depotHeadMapperEx;
        this.accountHeadMapper = accountHeadMapper;
        this.accountHeadMapperEx = accountHeadMapperEx;
        this.accountItemMapper = accountItemMapper;
        this.accountItemMapperEx = accountItemMapperEx;
        this.logService = logService;
        this.commonService = commonService;
        this.systemConfigService = systemConfigService;
    }

    public Account getAccount(long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    public List<Account> getAccountListByIds(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<Account> list = new ArrayList<>();
        try{
            AccountExample example = new AccountExample();
            example.createCriteria().andIdIn(idList);
            list = accountMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Account> getAccount() {
        List<Account> list=null;
        try{
            AccountExample example = new AccountExample();
            example.createCriteria().andEnabledEqualTo(true).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            example.setOrderByClause("sort asc, id desc");
            list=accountMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Account> getAccountByParam(String name, String serialNo) {
        List<Account> list=null;
        try{
            list=accountMapperEx.getAccountByParam(name, serialNo);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<AccountVo4List> select(String name, String serialNo, String remark, int offset, int rows) {
        List<AccountVo4List> resList = new ArrayList<>();
        List<AccountVo4List> list=null;
        try{
            list = accountMapperEx.selectByConditionAccount(name, serialNo, remark, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        String timeStr = Tools.getCurrentMonth();
        Boolean forceFlag = systemConfigService.getForceApprovalFlag();
        if (null != list) {
            for (AccountVo4List al : list) {
                DecimalFormat df = new DecimalFormat(".##");
                BigDecimal thisMonthAmount = getAccountSum(al.getId(), timeStr, "month", forceFlag).add(getAccountSumByHead(al.getId(), timeStr, "month", forceFlag))
                        .add(getAccountSumByDetail(al.getId(), timeStr, "month", forceFlag)).add(getManyAccountSum(al.getId(), timeStr, "month", forceFlag));
                String thisMonthAmountFmt = "0";
                if ((thisMonthAmount.compareTo(BigDecimal.ZERO))!=0) {
                    thisMonthAmountFmt = df.format(thisMonthAmount);
                }
                al.setThisMonthAmount(thisMonthAmountFmt);  //本月发生额
                BigDecimal currentAmount = getAccountSum(al.getId(), "", "month", forceFlag).add(getAccountSumByHead(al.getId(), "", "month", forceFlag))
                        .add(getAccountSumByDetail(al.getId(), "", "month", forceFlag)).add(getManyAccountSum(al.getId(), "", "month", forceFlag)) .add(al.getInitialAmount()) ;
                al.setCurrentAmount(currentAmount);
                resList.add(al);
            }
        }
        return resList;
    }

    public Long countAccount(String name, String serialNo, String remark) {
        Long result=null;
        try{
            result=accountMapperEx.countsByAccount(name, serialNo, remark);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertAccount(JSONObject obj, HttpServletRequest request) {
        Account account = JSONObject.parseObject(obj.toJSONString(), Account.class);
        if(account.getInitialAmount() == null) {
            account.setInitialAmount(BigDecimal.ZERO);
        }
        List<Account> accountList = getAccountByParam(null, null);
        if(accountList.isEmpty()) {
            account.setIsDefault(true);
        } else {
            account.setIsDefault(false);
        }
        account.setEnabled(true);
        int result=0;
        try{
            result = accountMapper.insertSelective(account);
            logService.insertLog("账户",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + account.getName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAccount(JSONObject obj, HttpServletRequest request) {
        Account account = JSONObject.parseObject(obj.toJSONString(), Account.class);
        int result=0;
        try{
            result = accountMapper.updateByPrimaryKeySelective(account);
            logService.insertLog("账户",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(account.getName()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteAccount(Long id, HttpServletRequest request) {
        return batchDeleteAccountByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAccount(String ids, HttpServletRequest request) {
        return batchDeleteAccountByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAccountByIds(String ids) {
        int result=0;
        String [] idArray=ids.split(",");
        //校验财务主表	jsh_accounthead
        List<AccountHead> accountHeadList=null;
        try{
            accountHeadList = accountHeadMapperEx.getAccountHeadListByAccountIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(accountHeadList!=null&& !accountHeadList.isEmpty()){
            logger.error("异常码[{}],异常提示[{}],参数,AccountIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        //校验财务子表	jsh_accountitem
        List<AccountItem> accountItemList=null;
        try{
            accountItemList = accountItemMapperEx.getAccountItemListByAccountIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(accountItemList!=null&& !accountItemList.isEmpty()){
            logger.error("异常码[{}],异常提示[{}],参数,AccountIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        //校验单据主表	jsh_depot_head
        List<DepotHead> depotHeadList =null;
        try{
            depotHeadList = depotHeadMapperEx.getDepotHeadListByAccountIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(depotHeadList!=null&& !depotHeadList.isEmpty()){
            logger.error("异常码[{}],异常提示[{}],参数,AccountIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        //记录日志
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<Account> list = getAccountListByIds(ids);
        for(Account account: list){
            sb.append("[").append(account.getName()).append("]");
        }
        logService.insertLog("账户", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo = commonService.getCurrentUser();
        //校验通过执行删除操作
        try{
            result = accountMapperEx.batchDeleteAccountByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        AccountExample example = new AccountExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Account> list=null;
        try{
            list = accountMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public List<Account> findBySelect() {
        AccountExample example = new AccountExample();
        example.createCriteria().andEnabledEqualTo(true).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        example.setOrderByClause("sort asc, id desc");
        List<Account> list=null;
        try{
            list = accountMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    /**
     * 单个账户的金额求和-入库和出库
     *
     * @param id
     * @return
     */
    public BigDecimal getAccountSum(Long id, String timeStr, String type, Boolean forceFlag) {
        BigDecimal accountSum = BigDecimal.ZERO;
        try {
            DepotHeadExample example = new DepotHeadExample();
            DepotHeadExample.Criteria criteria = example.createCriteria();
            if (!timeStr.equals("")) {
                Date bTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                Date eTime = StringUtil.getDateByString(Tools.lastDayOfMonth(timeStr) + BusinessConstants.DAY_LAST_TIME, null);
                Date mTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                if (type.equals("month")) {
                    criteria.andAccountIdEqualTo(id).andPayTypeNotEqualTo("预付款")
                    .andOperTimeGreaterThanOrEqualTo(bTime).andOperTimeLessThanOrEqualTo(eTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                } else if (type.equals("date")) {
                    criteria.andAccountIdEqualTo(id).andPayTypeNotEqualTo("预付款")
                    .andOperTimeLessThanOrEqualTo(mTime).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                }
            } else {
                criteria.andAccountIdEqualTo(id).andPayTypeNotEqualTo("预付款")
                        .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            }
            List<DepotHead> dataList=null;
            try{
                if(forceFlag) {
                    criteria.andStatusEqualTo("1");
                }
                dataList = depotHeadMapper.selectByExample(example);
            }catch(Exception e){
                JshException.readFail(logger, e);
            }
            if (dataList != null) {
                for (DepotHead depotHead : dataList) {
                    if(depotHead.getChangeAmount()!=null) {
                        accountSum = accountSum .add(depotHead.getChangeAmount()) ;
                    }
                }
            }
        } catch (DataAccessException e) {
            logger.error(">>>>>>>>>查找进销存信息异常", e);
        }
        return accountSum;
    }

    /**
     * 单个账户的金额求和-收入、支出、转账的单据表头的合计
     *
     * @param id
     * @return
     */
    public BigDecimal getAccountSumByHead(Long id, String timeStr, String type, Boolean forceFlag) {
        BigDecimal accountSum = BigDecimal.ZERO;
        try {
            AccountHeadExample example = new AccountHeadExample();
            AccountHeadExample.Criteria criteria = example.createCriteria();
            if (!timeStr.equals("")) {
                Date bTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                Date eTime = StringUtil.getDateByString(Tools.lastDayOfMonth(timeStr) + BusinessConstants.DAY_LAST_TIME, null);
                Date mTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                if (type.equals("month")) {
                    criteria.andAccountIdEqualTo(id)
                            .andBillTimeGreaterThanOrEqualTo(bTime).andBillTimeLessThanOrEqualTo(eTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                } else if (type.equals("date")) {
                    criteria.andAccountIdEqualTo(id)
                            .andBillTimeLessThanOrEqualTo(mTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                }
            } else {
                criteria.andAccountIdEqualTo(id)
                        .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            }
            List<AccountHead> dataList=null;
            try{
                if(forceFlag) {
                    criteria.andStatusEqualTo("1");
                }
                dataList = accountHeadMapper.selectByExample(example);
            }catch(Exception e){
                JshException.readFail(logger, e);
            }
            if (dataList != null) {
                for (AccountHead accountHead : dataList) {
                    if(accountHead.getChangeAmount()!=null) {
                        accountSum = accountSum.add(accountHead.getChangeAmount());
                    }
                }
            }
        } catch (DataAccessException e) {
            logger.error(">>>>>>>>>查找进销存信息异常", e);
        }
        return accountSum;
    }

    /**
     * 单个账户的金额求和-收款、付款、转账、收预付款的单据明细的合计
     *
     * @param id
     * @return
     */
    public BigDecimal getAccountSumByDetail(Long id, String timeStr, String type, Boolean forceFlag) {
        BigDecimal accountSum =BigDecimal.ZERO ;
        try {
            AccountHeadExample example = new AccountHeadExample();
            AccountHeadExample.Criteria criteria = example.createCriteria();
            if (!timeStr.equals("")) {
                Date bTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                Date eTime = StringUtil.getDateByString(Tools.lastDayOfMonth(timeStr) + BusinessConstants.DAY_LAST_TIME, null);
                Date mTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                if (type.equals("month")) {
                    criteria.andBillTimeGreaterThanOrEqualTo(bTime).andBillTimeLessThanOrEqualTo(eTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                } else if (type.equals("date")) {
                    criteria.andBillTimeLessThanOrEqualTo(mTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                }
            }
            List<AccountHead> dataList=null;
            try{
                if(forceFlag) {
                    criteria.andStatusEqualTo("1");
                }
                dataList = accountHeadMapper.selectByExample(example);
            }catch(Exception e){
                JshException.readFail(logger, e);
            }
            if (dataList != null) {
                String ids = "";
                for (AccountHead accountHead : dataList) {
                    ids = ids + accountHead.getId() + ",";
                }
                if (!ids.equals("")) {
                    ids = ids.substring(0, ids.length() - 1);
                }
                AccountItemExample exampleAi = new AccountItemExample();
                if (!ids.equals("")) {
                    List<Long> idList = StringUtil.strToLongList(ids);
                    exampleAi.createCriteria().andAccountIdEqualTo(id).andHeaderIdIn(idList)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                    List<AccountItem> dataListOne = accountItemMapper.selectByExample(exampleAi);
                    if (dataListOne != null) {
                        for (AccountItem accountItem : dataListOne) {
                            if(accountItem.getEachAmount()!=null) {
                                accountSum = accountSum.add(accountItem.getEachAmount());
                            }
                        }
                    }
                }
            }
        } catch (DataAccessException e) {
            logger.error(">>>>>>>>>查找进销存信息异常", e);
        } catch (Exception e) {
            logger.error(">>>>>>>>>异常信息：", e);
        }
        return accountSum;
    }

    /**
     * 单个账户的金额求和-多账户的明细合计
     *
     * @param id
     * @return
     */
    public BigDecimal getManyAccountSum(Long id, String timeStr, String type, Boolean forceFlag) {
        BigDecimal accountSum = BigDecimal.ZERO;
        try {
            DepotHeadExample example = new DepotHeadExample();
            DepotHeadExample.Criteria criteria = example.createCriteria();
            if (!timeStr.equals("")) {
                Date bTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                Date eTime = StringUtil.getDateByString(Tools.lastDayOfMonth(timeStr) + BusinessConstants.DAY_LAST_TIME, null);
                Date mTime = StringUtil.getDateByString(Tools.firstDayOfMonth(timeStr) + BusinessConstants.DAY_FIRST_TIME, null);
                if (type.equals("month")) {
                    criteria.andAccountIdListLike("%" +id.toString() + "%")
                            .andOperTimeGreaterThanOrEqualTo(bTime).andOperTimeLessThanOrEqualTo(eTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                } else if (type.equals("date")) {
                    criteria.andAccountIdListLike("%" +id.toString() + "%")
                            .andOperTimeLessThanOrEqualTo(mTime)
                            .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
                }
            } else {
                example.createCriteria().andAccountIdListLike("%" +id.toString() + "%")
                        .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            }
            List<DepotHead> dataList=null;
            try{
                if(forceFlag) {
                    criteria.andStatusEqualTo("1");
                }
                dataList = depotHeadMapper.selectByExample(example);
            }catch(Exception e){
                JshException.readFail(logger, e);
            }
            if (dataList != null) {
                for (DepotHead depotHead : dataList) {
                    String accountIdList = depotHead.getAccountIdList();
                    String accountMoneyList = depotHead.getAccountMoneyList();
                    if(StringUtil.isNotEmpty(accountIdList) && StringUtil.isNotEmpty(accountMoneyList)) {
                        accountIdList = accountIdList.replace("[", "").replace("]", "").replace("\"", "");
                        accountMoneyList = accountMoneyList.replace("[", "").replace("]", "").replace("\"", "");
                        String[] aList = accountIdList.split(",");
                        String[] amList = accountMoneyList.split(",");
                        for (int i = 0; i < aList.length; i++) {
                            if (aList[i].toString().equals(id.toString())) {
                                if(amList!=null && amList.length>0) {
                                    accountSum = accountSum.add(new BigDecimal(amList[i]));
                                }
                            }
                        }
                    }
                }
            }
        } catch (DataAccessException e) {
            logger.error(">>>>>>>>>查找信息异常", e);
        }
        return accountSum;
    }

    public List<AccountVo4InOutList> findAccountInOutList(Long accountId, Integer offset, Integer rows) {
        List<AccountVo4InOutList> list=null;
        try{
            list = accountMapperEx.findAccountInOutList(accountId, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public int findAccountInOutListCount(Long accountId) {
        int result=0;
        try{
            result = accountMapperEx.findAccountInOutListCount(accountId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateIsDefault(Long accountId) {
        int result=0;
        try{
            //全部取消默认
            Account allAccount = new Account();
            allAccount.setIsDefault(false);
            AccountExample allExample = new AccountExample();
            allExample.createCriteria();
            accountMapper.updateByExampleSelective(allAccount, allExample);
            //给指定账户设为默认
            Account account = new Account();
            account.setIsDefault(true);
            AccountExample example = new AccountExample();
            example.createCriteria().andIdEqualTo(accountId);
            accountMapper.updateByExampleSelective(account, example);
            logService.insertLog("账户",BusinessConstants.LOG_OPERATION_TYPE_EDIT+accountId,
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            result = 1;
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public Map<Long,String> getAccountMap() {
        List<Account> accountList = getAccount();
        Map<Long,String> accountMap = new HashMap<>();
        for(Account account : accountList){
            accountMap.put(account.getId(), account.getName());
        }
        return accountMap;
    }

    public String getAccountStrByIdAndMoney(Map<Long,String> accountMap, String accountIdList, String accountMoneyList) {
        StringBuffer sb = new StringBuffer();
        List<Long> idList = StringUtil.strToLongList(accountIdList);
        List<BigDecimal> moneyList = StringUtil.strToBigDecimalList(accountMoneyList);
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            BigDecimal money =  moneyList.get(i).abs();
            sb.append(accountMap.get(id) + "(" + money + "元) ");
        }
        return sb.toString();
    }

    public Map<String, Object> getStatistics(String name, String serialNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Account> list = getAccountByParam(name, serialNo);
            String timeStr = Tools.getCurrentMonth();
            BigDecimal allMonthAmount = BigDecimal.ZERO;
            BigDecimal allCurrentAmount = BigDecimal.ZERO;
            Boolean forceFlag = systemConfigService.getForceApprovalFlag();
            if (null != list) {
                for (Account a : list) {
                    BigDecimal monthAmount = getAccountSum(a.getId(), timeStr, "month", forceFlag).add(getAccountSumByHead(a.getId(), timeStr, "month", forceFlag))
                            .add(getAccountSumByDetail(a.getId(), timeStr, "month", forceFlag)).add(getManyAccountSum(a.getId(), timeStr, "month", forceFlag));
                    BigDecimal currentAmount = getAccountSum(a.getId(), "", "month", forceFlag).add(getAccountSumByHead(a.getId(), "", "month", forceFlag))
                            .add(getAccountSumByDetail(a.getId(), "", "month", forceFlag)).add(getManyAccountSum(a.getId(), "", "month", forceFlag)).add(a.getInitialAmount());
                    allMonthAmount = allMonthAmount.add(monthAmount);
                    allCurrentAmount = allCurrentAmount.add(currentAmount);
                }
            }
            map.put("allCurrentAmount", priceFormat(allCurrentAmount));  //当前总金额
            map.put("allMonthAmount", priceFormat(allMonthAmount));  //本月发生额
        } catch (Exception e) {
            JshException.readFail(logger, e);
        }
        return map;
    }

    /**
     * 价格格式化
     * @param price
     * @return
     */
    private String priceFormat(BigDecimal price) {
        String priceFmt = "0";
        DecimalFormat df = new DecimalFormat(".##");
        if ((price.compareTo(BigDecimal.ZERO))!=0) {
            priceFmt = df.format(price);
        }
        return priceFmt;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchSetStatus(Boolean status, String ids) {
        logService.insertLog("账户",
                BusinessConstants.LOG_OPERATION_TYPE_ENABLED,
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        List<Long> accountIds = StringUtil.strToLongList(ids);
        Account account = new Account();
        account.setEnabled(status);
        AccountExample example = new AccountExample();
        example.createCriteria().andIdIn(accountIds);
        int result=0;
        try{
            result = accountMapper.updateByExampleSelective(account, example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
