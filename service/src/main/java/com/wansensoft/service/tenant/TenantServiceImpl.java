package com.wansensoft.service.tenant;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.tenant.Tenant;
import com.wansensoft.entities.tenant.TenantEx;
import com.wansensoft.entities.tenant.TenantExample;
import com.wansensoft.mappers.user.UserMapperEx;
import com.wansensoft.service.user.UserServiceImpl;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.mappers.tenant.TenantMapper;
import com.wansensoft.mappers.tenant.TenantMapperEx;
import com.wansensoft.service.log.LogServiceImpl;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService{
    private Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantMapper tenantMapper;
    private final TenantMapperEx tenantMapperEx;
    private final UserMapperEx userMapperEx;
    private final UserServiceImpl userServiceImpl;
    private final LogServiceImpl logServiceImpl;

    @Value("${tenant.userNumLimit}")
    private Integer userNumLimit;

    @Value("${tenant.tryDayLimit}")
    private Integer tryDayLimit;

    public TenantServiceImpl(TenantMapper tenantMapper, TenantMapperEx tenantMapperEx, UserMapperEx userMapperEx, UserServiceImpl userServiceImpl, LogServiceImpl logServiceImpl) {
        this.tenantMapper = tenantMapper;
        this.tenantMapperEx = tenantMapperEx;
        this.userMapperEx = userMapperEx;
        this.userServiceImpl = userServiceImpl;
        this.logServiceImpl = logServiceImpl;
    }

    public Tenant getTenant(long id) {
        Tenant result=null;
        try{
            result=tenantMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<Tenant> getTenant() {
        TenantExample example = new TenantExample();
        List<Tenant> list=null;
        try{
            list=tenantMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<TenantEx> select(String loginName, String type, String enabled, String remark, int offset, int rows) {
        List<TenantEx> list= new ArrayList<>();
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userServiceImpl.getCurrentUser().getLoginName())) {
                list = tenantMapperEx.selectByConditionTenant(loginName, type, enabled, remark, offset, rows);
                if (null != list) {
                    for (TenantEx tenantEx : list) {
                        tenantEx.setCreateTimeStr(Tools.getCenternTime(tenantEx.getCreateTime()));
                        tenantEx.setExpireTimeStr(Tools.getCenternTime(tenantEx.getExpireTime()));
                    }
                }
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countTenant(String loginName, String type, String enabled, String remark) {
        Long result=null;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userServiceImpl.getCurrentUser().getLoginName())) {
                result = tenantMapperEx.countsByTenant(loginName, type, enabled, remark);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertTenant(JSONObject obj, HttpServletRequest request) {
        Tenant tenant = JSONObject.parseObject(obj.toJSONString(), Tenant.class);
        int result=0;
        try{
            tenant.setCreateTime(new Date());
            if(tenant.getUserNumLimit()==null) {
                tenant.setUserNumLimit(userNumLimit); //默认用户限制数量
            }
            if(tenant.getExpireTime()==null) {
                tenant.setExpireTime(Tools.addDays(new Date(), tryDayLimit)); //租户允许试用的天数
            }
            result = tenantMapper.insertSelective(tenant);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateTenant(JSONObject obj, HttpServletRequest request) {
        Tenant tenant = JSONObject.parseObject(obj.toJSONString(), Tenant.class);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userServiceImpl.getCurrentUser().getLoginName())) {
                //如果租户下的用户限制数量为1，则将该租户之外的用户全部禁用
                if (1 == tenant.getUserNumLimit()) {
                    userMapperEx.disableUserByLimit(tenant.getTenantId());
                }
                result = tenantMapper.updateByPrimaryKeySelective(tenant);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteTenant(Long id, HttpServletRequest request) {
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userServiceImpl.getCurrentUser().getLoginName())) {
                result = tenantMapper.deleteByPrimaryKey(id);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteTenant(String ids, HttpServletRequest request) {
        List<Long> idList = StringUtil.strToLongList(ids);
        TenantExample example = new TenantExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userServiceImpl.getCurrentUser().getLoginName())) {
                result = tenantMapper.deleteByExample(example);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        TenantExample example = new TenantExample();
        example.createCriteria().andIdNotEqualTo(id).andLoginNameEqualTo(name);
        List<Tenant> list=null;
        try{
            list= tenantMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public Tenant getTenantByTenantId(long tenantId) {
        Tenant tenant = new Tenant();
        TenantExample example = new TenantExample();
        example.createCriteria().andTenantIdEqualTo(tenantId);
        List<Tenant> list = tenantMapper.selectByExample(example);
        if(!list.isEmpty()) {
            tenant = list.get(0);
        }
        return tenant;
    }

    public int batchSetStatus(Boolean status, String ids) {
        int result=0;
        try{
            if(BusinessConstants.DEFAULT_MANAGER.equals(userServiceImpl.getCurrentUser().getLoginName())) {
                String statusStr = "";
                if (status) {
                    statusStr = "批量启用";
                } else {
                    statusStr = "批量禁用";
                }
                logServiceImpl.insertLog("用户",
                        new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(ids).append("-").append(statusStr).toString(),
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
                List<Long> idList = StringUtil.strToLongList(ids);
                Tenant tenant = new Tenant();
                tenant.setEnabled(status);
                TenantExample example = new TenantExample();
                example.createCriteria().andIdIn(idList);
                result = tenantMapper.updateByExampleSelective(tenant, example);
            }
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
}
