package com.wansensoft.service.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.organization.Organization;
import com.wansensoft.entities.organization.OrganizationExample;
import com.wansensoft.entities.user.User;
import com.wansensoft.service.log.LogService;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.utils.constants.ExceptionConstants;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import com.wansensoft.mappers.organization.OrganizationMapper;
import com.wansensoft.mappers.organization.OrganizationMapperEx;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.vo.TreeNode;
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

/**
 * Description
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService{
    private Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationMapper organizationMapper;
    private final OrganizationMapperEx organizationMapperEx;
    private final LogService logService;

    public OrganizationServiceImpl(OrganizationMapper organizationMapper, OrganizationMapperEx organizationMapperEx, LogService logService) {
        this.organizationMapper = organizationMapper;
        this.organizationMapperEx = organizationMapperEx;
        this.logService = logService;
    }

    public Organization getOrganization(long id) {
        return organizationMapper.selectByPrimaryKey(id);
    }

    public List<Organization> getOrganizationListByIds(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<Organization> list = new ArrayList<>();
        try{
            OrganizationExample example = new OrganizationExample();
            example.createCriteria().andIdIn(idList);
            list = organizationMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertOrganization(JSONObject obj, HttpServletRequest request) {
        Organization organization = JSONObject.parseObject(obj.toJSONString(), Organization.class);
        organization.setCreateTime(new Date());
        organization.setUpdateTime(new Date());
        int result=0;
        try{
            result = organizationMapper.insertSelective(organization);

            logService.insertLog("机构",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + organization.getOrgAbr(),request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateOrganization(JSONObject obj, HttpServletRequest request) {
        Organization organization = JSONObject.parseObject(obj.toJSONString(), Organization.class);
        organization.setUpdateTime(new Date());
        int result=0;
        try{
            result=organizationMapper.updateByPrimaryKeySelective(organization);
            logService.insertLog("机构",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + organization.getOrgAbr(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteOrganization(Long id, HttpServletRequest request) {
        return batchDeleteOrganizationByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteOrganization(String ids, HttpServletRequest request) {
        return batchDeleteOrganizationByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteOrganizationByIds(String ids) {
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<Organization> list = getOrganizationListByIds(ids);
        for(Organization organization: list){
            sb.append("[").append(organization.getOrgAbr()).append("]");
        }
        logService.insertLog("机构", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
       // User userInfo=userService.getCurrentUser();
        User userInfo= null;
        String [] idArray=ids.split(",");
        int result=0;
        List <Organization> organList = organizationMapperEx.getOrganizationByParentIds(idArray);
        if(organList!=null && !organList.isEmpty()) {
            //如果存在子机构则不能删除
            logger.error("异常码[{}],异常提示[{}]",
                    ExceptionConstants.ORGANIZATION_CHILD_NOT_ALLOWED_DELETE_CODE,ExceptionConstants.ORGANIZATION_CHILD_NOT_ALLOWED_DELETE_MSG);
            throw new BusinessRunTimeException(ExceptionConstants.ORGANIZATION_CHILD_NOT_ALLOWED_DELETE_CODE,
                    ExceptionConstants.ORGANIZATION_CHILD_NOT_ALLOWED_DELETE_MSG);
        } else {
            result=organizationMapperEx.batchDeleteOrganizationByIds(
                    new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name) {
        OrganizationExample example = new OrganizationExample();
        example.createCriteria().andIdNotEqualTo(id).andOrgAbrEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Organization> list=null;
        try{
            list= organizationMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int addOrganization(Organization org) {
        logService.insertLog("机构",
                BusinessConstants.LOG_OPERATION_TYPE_ADD + org.getOrgAbr(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        //新增时间
        Date date=new Date();
        // User userInfo=userService.getCurrentUser();
        User userInfo= null;
        org.setCreateTime(date);
        //修改时间
        org.setUpdateTime(date);
        /**
         *添加的时候检测机构编号是否已存在
         * */
        if(StringUtil.isNotEmpty(org.getOrgNo())){
            checkOrgNoIsExists(org.getOrgNo(),null);
        }
        /**
         * 未指定父级机构的时候默认为根机构
         * */
        if(org.getParentId()!=null){
            org.setParentId(null);
        }
        int result=0;
        try{
            result=organizationMapperEx.addOrganization(org);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int editOrganization(Organization org) {
        logService.insertLog("机构",
                BusinessConstants.LOG_OPERATION_TYPE_EDIT + org.getOrgAbr(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        //修改时间
        org.setUpdateTime(new Date());
        // User userInfo=userService.getCurrentUser();
        User userInfo= null;
        /**
         * 修改的时候检测机构编号是否已存在
         * */
        if(StringUtil.isNotEmpty(org.getOrgNo())){
            checkOrgNoIsExists(org.getOrgNo(),org.getId());
        }
        /**
         * 未指定父级机构的时候默认为根机构
         * */
        if(org.getParentId()!=null){
            org.setParentId(null);
        }
        int result=0;
        try{
            result=organizationMapperEx.editOrganization(org);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public List<TreeNode> getOrganizationTree(Long id) {
        List<TreeNode> list=null;
        try{
            list=organizationMapperEx.getNodeTree(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Organization> findById(Long id) {
        OrganizationExample example = new OrganizationExample();
        example.createCriteria().andIdEqualTo(id);
        List<Organization> list=null;
        try{
            list=organizationMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Organization> findByParentId(Long parentId) {
        List<Organization> list=null;
        if(parentId!=null){
            OrganizationExample example = new OrganizationExample();
            example.createCriteria().andIdEqualTo(parentId).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
            try{
                list=organizationMapper.selectByExample(example);
            }catch(Exception e){
                JshException.readFail(logger, e);
            }
        }
        return list;
    }

    public List<Organization> findByOrgNo(String orgNo) {
        OrganizationExample example = new OrganizationExample();
        example.createCriteria().andOrgNoEqualTo(orgNo).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Organization> list=null;
        try{
            list=organizationMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }
    /**
     * description:
     *  检查机构编号是否已经存在
     * @Param: orgNo
     * @return void
     */
    public void checkOrgNoIsExists(String orgNo,Long id) {
        List<Organization> orgList=findByOrgNo(orgNo);
        if(orgList!=null&&orgList.size()>0){
            if(orgList.size()>1){
                logger.error("异常码[{}],异常提示[{}],参数,orgNo[{}]",
                        ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_CODE,ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_MSG,orgNo);
                //获取的数据条数大于1，机构编号已存在
                throw new BusinessRunTimeException(ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_CODE,
                        ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_MSG);
            }
            if(id!=null){
                if(!orgList.get(0).getId().equals(id)){
                    //数据条数等于1，但是和编辑的数据的id不相同
                    logger.error("异常码[{}],异常提示[{}],参数,orgNo[{}],id[{}]",
                            ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_CODE,ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_MSG,orgNo,id);
                    throw new BusinessRunTimeException(ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_CODE,
                            ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_MSG);
                }
            }else{
                logger.error("异常码[{}],异常提示[{}],参数,orgNo[{}]",
                        ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_CODE,ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_MSG,orgNo);
                //数据条数等于1，但此时是新增
                throw new BusinessRunTimeException(ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_CODE,
                        ExceptionConstants.ORGANIZATION_NO_ALREADY_EXISTS_MSG);
            }
        }

    }

    /**
     * 根据父级id递归获取子集组织id
     * @return
     */
    public List<Long> getOrgIdByParentId(Long orgId) {
        List<Long> idList = new ArrayList<>();
        OrganizationExample example = new OrganizationExample();
        example.createCriteria().andIdEqualTo(orgId).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Organization> orgList = organizationMapper.selectByExample(example);
        if(orgList!=null && !orgList.isEmpty()) {
            idList.add(orgId);
            getOrgIdByParentNo(idList, orgList.get(0).getId());
        }
        return idList;
    }

    /**
     * 根据组织编号递归获取下级编号
     * @param id
     * @return
     */
    public void getOrgIdByParentNo(List<Long> idList,Long id) {
        OrganizationExample example = new OrganizationExample();
        example.createCriteria().andParentIdEqualTo(id).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Organization> orgList = organizationMapper.selectByExample(example);
        if(orgList!=null && !orgList.isEmpty()) {
            for(Organization o: orgList) {
                idList.add(o.getId());
                getOrgIdByParentNo(idList, o.getId());
            }
        }
    }
}
