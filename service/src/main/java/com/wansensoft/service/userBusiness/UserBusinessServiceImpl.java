package com.wansensoft.service.userBusiness;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.user.User;
import com.wansensoft.entities.user.UserBusiness;
import com.wansensoft.entities.user.UserBusinessExample;
import com.wansensoft.mappers.user.UserBusinessMapper;
import com.wansensoft.mappers.user.UserBusinessMapperEx;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.user.UserServiceImpl;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserBusinessServiceImpl extends ServiceImpl<UserBusinessMapper, UserBusiness> implements UserBusinessService{
    private Logger logger = LoggerFactory.getLogger(UserBusinessServiceImpl.class);

    private final UserBusinessMapper userBusinessMapper;
    private final UserBusinessMapperEx userBusinessMapperEx;
    private final LogService logService;
    private final UserServiceImpl userServiceImpl;

    public UserBusinessServiceImpl(UserBusinessMapper userBusinessMapper, UserBusinessMapperEx userBusinessMapperEx, LogService logService, UserServiceImpl userServiceImpl) {
        this.userBusinessMapper = userBusinessMapper;
        this.userBusinessMapperEx = userBusinessMapperEx;
        this.logService = logService;
        this.userServiceImpl = userServiceImpl;
    }

    public UserBusiness getUserBusiness(long id) {
        UserBusiness result=null;
        try{
            result=userBusinessMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<UserBusiness> getUserBusiness() {
        UserBusinessExample example = new UserBusinessExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<UserBusiness> list=null;
        try{
            list=userBusinessMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertUserBusiness(JSONObject obj, HttpServletRequest request) {
        UserBusiness userBusiness = JSONObject.parseObject(obj.toJSONString(), UserBusiness.class);
        int result=0;
        try{
            String token = "";
            if(request!=null) {
                token = request.getHeader("X-Access-Token");
                Long tenantId = Tools.getTenantIdByToken(token);
                if(tenantId!=0L) {
                    userBusiness.setTenantId(tenantId);
                }
            }
            String value = userBusiness.getValue();
            String newValue = value.replaceAll(",","\\]\\[");
            newValue = newValue.replaceAll("\\[0\\]","").replaceAll("\\[\\]","");
            userBusiness.setValue(newValue);
            result=userBusinessMapper.insertSelective(userBusiness);
            logService.insertLog("关联关系", BusinessConstants.LOG_OPERATION_TYPE_ADD, request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateUserBusiness(JSONObject obj, HttpServletRequest request) {
        UserBusiness userBusiness = JSONObject.parseObject(obj.toJSONString(), UserBusiness.class);
        int result=0;
        try{
            String value = userBusiness.getValue();
            String newValue = value.replaceAll(",","\\]\\[");
            newValue = newValue.replaceAll("\\[0\\]","").replaceAll("\\[\\]","");
            userBusiness.setValue(newValue);
            result=userBusinessMapper.updateByPrimaryKeySelective(userBusiness);
            logService.insertLog("关联关系", BusinessConstants.LOG_OPERATION_TYPE_EDIT, request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteUserBusiness(Long id, HttpServletRequest request) {
        return batchDeleteUserBusinessByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteUserBusiness(String ids, HttpServletRequest request) {
        return batchDeleteUserBusinessByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteUserBusinessByIds(String ids) {
        logService.insertLog("关联关系",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(ids).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo= userServiceImpl.getCurrentUser();
        String [] idArray=ids.split(",");
        int result=0;
        try{
            result=  userBusinessMapperEx.batchDeleteUserBusinessByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        return 1;
    }

    public List<UserBusiness> getBasicData(String keyId, String type) {
        List<UserBusiness> list=null;
        try{
            UserBusinessExample example = new UserBusinessExample();
            example.createCriteria().andKeyIdEqualTo(keyId).andTypeEqualTo(type)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            list= userBusinessMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<UserBusiness> getListBy(String keyId, String type) {
        List<UserBusiness> list=null;
        try{
            UserBusinessExample example = new UserBusinessExample();
            example.createCriteria().andKeyIdEqualTo(keyId).andTypeEqualTo(type)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            list= userBusinessMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public String getUBValueByTypeAndKeyId(String type, String keyId) {
        String ubValue = "";
        List<UserBusiness> ubList = getBasicData(keyId, type);
        if(ubList!=null && ubList.size()>0) {
            ubValue = ubList.get(0).getValue();
        }
        return ubValue;
    }

    public Long checkIsValueExist(String type, String keyId) {
        UserBusinessExample example = new UserBusinessExample();
        example.createCriteria().andTypeEqualTo(type).andKeyIdEqualTo(keyId)
                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<UserBusiness> list=null;
        try{
            list= userBusinessMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        Long id = null;
        if(list!=null&& !list.isEmpty()) {
            id = list.get(0).getId();
        }
        return id;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateBtnStr(String keyId, String type, String btnStr) {
        logService.insertLog("关联关系",
                BusinessConstants.LOG_OPERATION_TYPE_EDIT + "角色的按钮权限",
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        UserBusiness userBusiness = new UserBusiness();
        userBusiness.setBtnStr(btnStr);
        UserBusinessExample example = new UserBusinessExample();
        example.createCriteria().andKeyIdEqualTo(keyId).andTypeEqualTo(type);
        int result=0;
        try{
            result=  userBusinessMapper.updateByExampleSelective(userBusiness, example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
