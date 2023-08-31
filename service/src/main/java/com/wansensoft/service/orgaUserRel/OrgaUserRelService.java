package com.wansensoft.service.orgaUserRel;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.entities.organization.OrgaUserRel;
import com.wansensoft.entities.organization.OrgaUserRelExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.mappers.organization.OrgaUserRelMapper;
import com.wansensoft.mappers.organization.OrgaUserRelMapperEx;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.organization.OrganizationService;
import com.wansensoft.service.redis.RedisService;
import com.wansensoft.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Description
 */
@Service
public class OrgaUserRelService {
    private Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Resource
    private OrgaUserRelMapper orgaUserRelMapper;
    @Resource
    private OrgaUserRelMapperEx orgaUserRelMapperEx;
//    @Resource
//    private UserMapper userMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private LogService logService;

    public OrgaUserRel getOrgaUserRel(long id) throws Exception{
        return orgaUserRelMapper.selectByPrimaryKey(id);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertOrgaUserRel(JSONObject obj, HttpServletRequest request) throws Exception{
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
    public int updateOrgaUserRel(JSONObject obj, HttpServletRequest request) throws Exception{
        OrgaUserRel orgaUserRel = JSONObject.parseObject(obj.toJSONString(), OrgaUserRel.class);
        int result=0;
        try{
            result=orgaUserRelMapper.updateByPrimaryKeySelective(orgaUserRel);
            logService.insertLog("用户与机构关系",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(orgaUserRel.getId()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteOrgaUserRel(Long id, HttpServletRequest request)throws Exception {
        int result=0;
        try{
            result=orgaUserRelMapper.deleteByPrimaryKey(id);
            logService.insertLog("用户与机构关系",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteOrgaUserRel(String ids, HttpServletRequest request)throws Exception {
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
    public OrgaUserRel addOrgaUserRel(OrgaUserRel orgaUserRel) throws Exception{
        Date date = new Date();
        User userInfo = getCurrentUser();
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

    public User getCurrentUser()throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Long userId = Long.parseLong(redisService.getObjectFromSessionByKey(request,"userId").toString());
        return getUser(userId);
    }

    public User getUser(long id)throws Exception {
        User result=null;
        try{
          //  result=userMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }
    /**
     * description:
     *  更新机构用户关联关系
     * create time: 2019/3/12 9:40
     * @Param: orgaUserRel
     * @return void
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public OrgaUserRel updateOrgaUserRel(OrgaUserRel orgaUserRel) throws Exception{
        User userInfo=getCurrentUser();
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
    public String getUserIdListByUserId(Long userId) throws Exception{
        String users = "";
        OrgaUserRelExample example = new OrgaUserRelExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<OrgaUserRel> list = orgaUserRelMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            OrgaUserRel our = list.get(0);
            List<Long> userIdList = getUserIdListByOrgId(our.getOrgaId());
            for(Long u: userIdList){
                users = users + u + ",";
            }
            if(users.length()>0){
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
        if(orgIdList!=null && orgIdList.size()>0) {
            example.createCriteria().andOrgaIdIn(orgIdList).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        } else {
            example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        }
        List<OrgaUserRel> list = orgaUserRelMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            for(OrgaUserRel our: list) {
                userIdList.add(our.getUserId());
            }
        }
        return  userIdList;
    }
}
