package com.wansensoft.service.functions;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.function.Function;
import com.wansensoft.entities.function.FunctionEx;
import com.wansensoft.entities.function.FunctionExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.function.FunctionMapper;
import com.wansensoft.mappers.function.FunctionMapperEx;
import com.wansensoft.service.CommonService;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.systemConfig.SystemConfigService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
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
public class FunctionServiceImpl extends ServiceImpl<FunctionMapper, Function> implements FunctionService{
    private Logger logger = LoggerFactory.getLogger(FunctionServiceImpl.class);

    private final FunctionMapper functionsMapper;
    private final FunctionMapperEx functionMapperEx;
    private final CommonService commonService;
    private final SystemConfigService systemConfigService;
    private final LogService logService;

    public FunctionServiceImpl(FunctionMapper functionsMapper, FunctionMapperEx functionMapperEx, CommonService commonService, SystemConfigService systemConfigService, LogService logService) {
        this.functionsMapper = functionsMapper;
        this.functionMapperEx = functionMapperEx;
        this.commonService = commonService;
        this.systemConfigService = systemConfigService;
        this.logService = logService;
    }

    public Function getFunction(long id) {
        Function result=null;
        try{
            result=functionsMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<Function> getFunctionListByIds(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<Function> list = new ArrayList<>();
        try{
            FunctionExample example = new FunctionExample();
            example.createCriteria().andIdIn(idList);
            list = functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Function> getFunction() {
        FunctionExample example = new FunctionExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Function> list=null;
        try{
            list=functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<FunctionEx> select(String name, String type, int offset, int rows) {
        List<FunctionEx> list=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                list = functionMapperEx.selectByConditionFunction(name, type, offset, rows);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countFunction(String name, String type) {
        Long result=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = functionMapperEx.countsByFunction(name, type);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertFunction(JSONObject obj, HttpServletRequest request) {
        Function functions = JSONObject.parseObject(obj.toJSONString(), Function.class);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                functions.setState(false);
                functions.setType("电脑版");
                result = functionsMapper.insertSelective(functions);
                logService.insertLog("功能",
                        BusinessConstants.LOG_OPERATION_TYPE_ADD + functions.getName(), request);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateFunction(JSONObject obj, HttpServletRequest request) {
        Function functions = JSONObject.parseObject(obj.toJSONString(), Function.class);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = functionsMapper.updateByPrimaryKeySelective(functions);
                logService.insertLog("功能",
                        BusinessConstants.LOG_OPERATION_TYPE_EDIT + functions.getName(), request);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteFunction(Long id, HttpServletRequest request) {
        return batchDeleteFunctionByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteFunction(String ids, HttpServletRequest request) {
        return batchDeleteFunctionByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteFunctionByIds(String ids) {
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<Function> list = getFunctionListByIds(ids);
        for(Function functions: list){
            sb.append("[").append(functions.getName()).append("]");
        }
        User userInfo= commonService.getCurrentUser();
        String [] idArray=ids.split(",");
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(commonService.getCurrentUser().getLoginName())) {
                result = functionMapperEx.batchDeleteFunctionByIds(new Date(), userInfo == null ? null : userInfo.getId(), idArray);
                logService.insertLog("功能", sb.toString(),
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        FunctionExample example = new FunctionExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Function> list=null;
        try{
            list = functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public int checkIsNumberExist(Long id, String number) {
        FunctionExample example = new FunctionExample();
        example.createCriteria().andIdNotEqualTo(id).andNumberEqualTo(number).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Function> list=null;
        try{
            list = functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public List<Function> getRoleFunction(String pNumber) {
        FunctionExample example = new FunctionExample();
        example.createCriteria().andEnabledEqualTo(true).andParentNumberEqualTo(pNumber)
                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        example.setOrderByClause("Sort");
        List<Function> list=null;
        try{
            list = functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Function> findRoleFunction(String pNumber) {
        List<Function> list=null;
        try{
            boolean multiLevelApprovalFlag = systemConfigService.getMultiLevelApprovalFlag();
            FunctionExample example = new FunctionExample();
            FunctionExample.Criteria criteria = example.createCriteria();
            criteria.andEnabledEqualTo(true).andParentNumberEqualTo(pNumber)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            if("0".equals(pNumber)) {
                if(!multiLevelApprovalFlag) {
                    criteria.andUrlNotEqualTo("/workflow");
                }
            }
            example.setOrderByClause("Sort");
            list =functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Function> findByIds(String functionsIds) {
        List<Long> idList = StringUtil.strToLongList(functionsIds);
        FunctionExample example = new FunctionExample();
        example.createCriteria().andEnabledEqualTo(true).andIdIn(idList).andPushBtnIsNotNull().andPushBtnNotEqualTo("")
                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        example.setOrderByClause("Sort asc");
        List<Function> list=null;
        try{
            list =functionsMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }
}
