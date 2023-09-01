package com.wansensoft.service.orgaUserRel;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.organization.OrgaUserRel;
import com.wansensoft.entities.organization.OrgaUserRelExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.organization.OrgaUserRelMapper;
import com.wansensoft.mappers.organization.OrgaUserRelMapperEx;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.organization.OrganizationService;
import com.wansensoft.service.organization.OrganizationServiceImpl;
import com.wansensoft.service.user.UserService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.service.redis.RedisService;
import com.wansensoft.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description
 */
@Service
public class OrgaUserRelServiceImpl extends ServiceImpl<OrgaUserRelMapper, OrgaUserRel> implements OrgaUserRelService{
    private Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrgaUserRelMapper orgaUserRelMapper;
    private final OrgaUserRelMapperEx orgaUserRelMapperEx;
    private final RedisService redisService;
    private final OrganizationService organizationService;
    private final LogService logService;
    private final UserService userService;

    public OrgaUserRelServiceImpl(OrgaUserRelMapper orgaUserRelMapper, OrgaUserRelMapperEx orgaUserRelMapperEx, RedisService redisService, OrganizationService organizationService, LogService logService, UserService userService) {
        this.orgaUserRelMapper = orgaUserRelMapper;
        this.orgaUserRelMapperEx = orgaUserRelMapperEx;
        this.redisService = redisService;
        this.organizationService = organizationService;
        this.logService = logService;
        this.userService = userService;
    }

    public OrgaUserRel getOrgaUserRel(long id) {
        return orgaUserRelMapper.selectByPrimaryKey(id);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertOrgaUserRel(JSONObject obj, HttpServletRequest request) {
        OrgaUserRel orgaUserRel = JSONObject.parseObject(obj.toJSONString(), OrgaUserRel.class);
        int result=0;
        try{
            result=orgaUserRelMapper.insertSelective(orgaUserRel);
            logService.insertLog("用户与机构关系", BusinessConstants.LOG_OPERATION_TYPE_ADD, request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateOrgaUserRel(JSONObject obj, HttpServletRequest request) {
        OrgaUserRel orgaUserRel = JSONObject.parseObject(obj.toJSONString(), OrgaUserRel.class);
        int result=0;
        try{
            result=orgaUserRelMapper.updateByPrimaryKeySelective(orgaUserRel);
            logService.insertLog("用户与机构关系",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + orgaUserRel.getId(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteOrgaUserRel(Long id, HttpServletRequest request) {
        int result=0;
        try{
            result=orgaUserRelMapper.deleteByPrimaryKey(id);
            logService.insertLog("用户与机构关系",
                    BusinessConstants.LOG_OPERATION_TYPE_DELETE + id, request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteOrgaUserRel(String ids, HttpServletRequest request) {
        List<Long> idList = StringUtil.strToLongList(ids);
        OrgaUserRelExample example = new OrgaUserRelExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            result=orgaUserRelMapper.deleteByExample(example);
            logService.insertLog("用户与机构关系", "批量删除,id集:" + ids, request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    /**
     * description:
     *  新增机构用户关联关系,反显id
     * @Param: orgaUserRel
     * @return void
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public OrgaUserRel addOrgaUserRel(OrgaUserRel orgaUserRel) {
        Date date = new Date();
        User userInfo = userService.getCurrentUser();
        //创建时间
        if(orgaUserRel.getCreateTime()==null){
            orgaUserRel.setCreateTime(date);
        }
        //创建人
        if(orgaUserRel.getCreator()==null){
            orgaUserRel.setCreator(userInfo==null?null:userInfo.getId());
        }
        //更新时间
        if(orgaUserRel.getUpdateTime()==null){
            orgaUserRel.setUpdateTime(date);
        }
        //更新人
        if(orgaUserRel.getUpdater()==null){
            orgaUserRel.setUpdater(userInfo==null?null:userInfo.getId());
        }
        orgaUserRel.setDeleteFlag(BusinessConstants.DELETE_FLAG_EXISTS);
        int result=0;
        try{
            result=orgaUserRelMapperEx.addOrgaUserRel(orgaUserRel);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        if(result>0){
            return orgaUserRel;
        }
        return null;
    }

    /**
     * description:
     *  更新机构用户关联关系
     * @Param: orgaUserRel
     * @return void
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public OrgaUserRel updateOrgaUserRel(OrgaUserRel orgaUserRel) {
        User userInfo = userService.getCurrentUser();
        //更新时间
        if(orgaUserRel.getUpdateTime()==null){
            orgaUserRel.setUpdateTime(new Date());
        }
        //更新人
        if(orgaUserRel.getUpdater()==null){
            orgaUserRel.setUpdater(userInfo==null?null:userInfo.getId());
        }
        int result=0;
        try{
            result=orgaUserRelMapperEx.updateOrgaUserRel(orgaUserRel);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        if(result>0){
            return orgaUserRel;
        }
        return null;
    }

    /**
     * 根据用户id获取用户id列表
     * @param userId
     * @return
     * @throws Exception
     */
    public String getUserIdListByUserId(Long userId) {
        String users = "";
        OrgaUserRelExample example = new OrgaUserRelExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<OrgaUserRel> list = orgaUserRelMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            OrgaUserRel our = list.get(0);
            List<Long> userIdList = getUserIdListByOrgId(our.getOrgaId());
            for(Long u: userIdList){
                users = users + u + ",";
            }
            if(!users.isEmpty()){
                users = users.substring(0,users.length()-1);
            }
        }
        return users;
    }

    /**
     * 根据组织id获取所属的用户id列表（包含组织的递归）
     * @param orgId
     * @return
     */
    public List<Long> getUserIdListByOrgId(Long orgId) {
        List<Long> orgIdList = organizationService.getOrgIdByParentId(orgId);
        List<Long> userIdList = new ArrayList<Long>();
        OrgaUserRelExample example = new OrgaUserRelExample();
        if(orgIdList!=null && !orgIdList.isEmpty()) {
            example.createCriteria().andOrgaIdIn(orgIdList).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        } else {
            example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        }
        List<OrgaUserRel> list = orgaUserRelMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            for(OrgaUserRel our: list) {
                userIdList.add(our.getUserId());
            }
        }
        return  userIdList;
    }
}
