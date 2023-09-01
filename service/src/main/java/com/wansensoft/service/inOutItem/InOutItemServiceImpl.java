package com.wansensoft.service.inOutItem;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.account.AccountItem;
import com.wansensoft.entities.inOutItem.InOutItem;
import com.wansensoft.entities.inOutItem.InOutItemExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.account.AccountItemMapperEx;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.log.LogServiceImpl;
import com.wansensoft.service.user.UserService;
import com.wansensoft.service.user.UserServiceImpl;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.utils.constants.ExceptionConstants;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.mappers.inOutItem.InOutItemMapper;
import com.wansensoft.mappers.inOutItem.InOutItemMapperEx;
import com.wansensoft.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InOutItemServiceImpl extends ServiceImpl<InOutItemMapper, InOutItem> implements InOutItemService{
    private Logger logger = LoggerFactory.getLogger(InOutItemServiceImpl.class);

    private final InOutItemMapper inOutItemMapper;
    private final InOutItemMapperEx inOutItemMapperEx;
    private final UserService userService;
    private final LogService logService;
    private final AccountItemMapperEx accountItemMapperEx;

    public InOutItemServiceImpl(InOutItemMapper inOutItemMapper, InOutItemMapperEx inOutItemMapperEx, UserService userService, LogService logService, AccountItemMapperEx accountItemMapperEx) {
        this.inOutItemMapper = inOutItemMapper;
        this.inOutItemMapperEx = inOutItemMapperEx;
        this.userService = userService;
        this.logService = logService;
        this.accountItemMapperEx = accountItemMapperEx;
    }

    public InOutItem getInOutItem(long id) {
        InOutItem result=null;
        try{
            result=inOutItemMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<InOutItem> getInOutItemListByIds(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<InOutItem> list = new ArrayList<>();
        try{
            InOutItemExample example = new InOutItemExample();
            example.createCriteria().andIdIn(idList);
            list = inOutItemMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<InOutItem> getInOutItem() {
        InOutItemExample example = new InOutItemExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<InOutItem> list=null;
        try{
            list=inOutItemMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<InOutItem> select(String name, String type, String remark, int offset, int rows) {
        List<InOutItem> list=null;
        try{
            list=inOutItemMapperEx.selectByConditionInOutItem(name, type, remark, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countInOutItem(String name, String type, String remark) {
        Long result=null;
        try{
            result=inOutItemMapperEx.countsByInOutItem(name, type, remark);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertInOutItem(JSONObject obj, HttpServletRequest request) {
        InOutItem inOutItem = JSONObject.parseObject(obj.toJSONString(), InOutItem.class);
        int result=0;
        try{
            inOutItem.setEnabled(true);
            result=inOutItemMapper.insertSelective(inOutItem);
            logService.insertLog("收支项目",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + inOutItem.getName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateInOutItem(JSONObject obj, HttpServletRequest request) {
        InOutItem inOutItem = JSONObject.parseObject(obj.toJSONString(), InOutItem.class);
        int result=0;
        try{
            result=inOutItemMapper.updateByPrimaryKeySelective(inOutItem);
            logService.insertLog("收支项目",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + inOutItem.getName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteInOutItem(Long id, HttpServletRequest request) {
        return batchDeleteInOutItemByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteInOutItem(String ids, HttpServletRequest request) {
        return batchDeleteInOutItemByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteInOutItemByIds(String ids) {
        int result = 0;
        String [] idArray=ids.split(",");
        //校验财务子表	jsh_accountitem
        List<AccountItem> accountItemList=null;
        try{
            accountItemList=accountItemMapperEx.getAccountItemListByInOutItemIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(accountItemList!=null&&accountItemList.size()>0){
            logger.error("异常码[{}],异常提示[{}],参数,InOutItemIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        //校验通过执行删除操作
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<InOutItem> list = getInOutItemListByIds(ids);
        for(InOutItem inOutItem: list){
            sb.append("[").append(inOutItem.getName()).append("]");
        }
        logService.insertLog("收支项目", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo= userService.getCurrentUser();
        try{
            result=inOutItemMapperEx.batchDeleteInOutItemByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        InOutItemExample example = new InOutItemExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<InOutItem> list = null;
        try{
            list=inOutItemMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }

        return list==null?0:list.size();
    }

    public List<InOutItem> findBySelect(String type) {
        InOutItemExample example = new InOutItemExample();
        if (type.equals("in")) {
            example.createCriteria().andTypeEqualTo("收入").andEnabledEqualTo(true)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        } else if (type.equals("out")) {
            example.createCriteria().andTypeEqualTo("支出").andEnabledEqualTo(true)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        } else {
            example.createCriteria().andEnabledEqualTo(true)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        }
        example.setOrderByClause("sort asc, id desc");
        List<InOutItem> list = null;
        try{
            list=inOutItemMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchSetStatus(Boolean status, String ids) {
        logService.insertLog("收支项目",
                BusinessConstants.LOG_OPERATION_TYPE_ENABLED,
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        List<Long> inOutItemIds = StringUtil.strToLongList(ids);
        InOutItem inOutItem = new InOutItem();
        inOutItem.setEnabled(status);
        InOutItemExample example = new InOutItemExample();
        example.createCriteria().andIdIn(inOutItemIds);
        int result=0;
        try{
            result = inOutItemMapper.updateByExampleSelective(inOutItem, example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
