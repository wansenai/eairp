package com.wansensoft.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.entities.function.Function;
import com.wansensoft.entities.organization.OrgaUserRel;
import com.wansensoft.entities.role.Role;
import com.wansensoft.entities.tenant.Tenant;
import com.wansensoft.entities.user.User;
import com.wansensoft.entities.user.UserBusiness;
import com.wansensoft.entities.user.UserEx;
import com.wansensoft.entities.user.UserExample;
import com.wansensoft.mappers.role.RoleMapper;
import com.wansensoft.mappers.role.RoleMapperEx;
import com.wansensoft.mappers.user.UserMapper;
import com.wansensoft.mappers.user.UserMapperEx;
import com.wansensoft.service.CommonService;
import com.wansensoft.service.functions.FunctionService;
import com.wansensoft.service.log.LogService;
import com.wansensoft.service.orgaUserRel.OrgaUserRelService;
import com.wansensoft.service.platformConfig.PlatformConfigService;
import com.wansensoft.service.redis.RedisService;
import com.wansensoft.service.tenant.TenantService;
import com.wansensoft.service.userBusiness.UserBusinessService;
import com.wansensoft.utils.HttpClient;
import com.wansensoft.utils.ExceptionCodeConstants;
import com.wansensoft.utils.StringUtil;
import com.wansensoft.utils.Tools;
import com.wansensoft.utils.redis.RedisUtil;
import com.wansensoft.vo.TreeNodeEx;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wansensoft.utils.constants.BusinessConstants;
import com.wansensoft.utils.constants.ExceptionConstants;
import com.wansensoft.plugins.exception.BusinessRunTimeException;
import com.wansensoft.plugins.exception.JshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final UserMapperEx userMapperEx;
    private final OrgaUserRelService orgaUserRelService;
    private final LogService logService;
    private final TenantService tenantService;
    private final UserBusinessService userBusinessService;
    private final FunctionService functionService;
    private final PlatformConfigService platformConfigService;
    private final RedisService redisService;

    private final RoleMapperEx roleMapperEx;

    public UserServiceImpl(UserMapper userMapper, UserMapperEx userMapperEx, OrgaUserRelService orgaUserRelService, LogService logService, TenantService tenantService, UserBusinessService userBusinessService, FunctionService functionService, PlatformConfigService platformConfigService, RedisService redisService, RoleMapperEx roleMapperEx) {
        this.userMapper = userMapper;
        this.userMapperEx = userMapperEx;
        this.orgaUserRelService = orgaUserRelService;
        this.logService = logService;
        this.tenantService = tenantService;
        this.userBusinessService = userBusinessService;
        this.roleMapperEx = roleMapperEx;
        this.functionService = functionService;
        this.platformConfigService = platformConfigService;
        this.redisService = redisService;
    }

    public User getUser(long id) {
        User result=null;
        try{
            result=userMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<User> getUserListByIds(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<User> list = new ArrayList<>();
        try{
            UserExample example = new UserExample();
            example.createCriteria().andIdIn(idList);
            list = userMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<User> getUser() {
        UserExample example = new UserExample();
        example.createCriteria().andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
        List<User> list=null;
        try{
            list=userMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<UserEx> select(String userName, String loginName, int offset, int rows) {
        List<UserEx> list=null;
        try{
            list=userMapperEx.selectByConditionUser(userName, loginName, offset, rows);
            for(UserEx ue: list){
                String userType = "";
                if (ue.getId().equals(ue.getTenantId())) {
                    userType = "租户";
                } else if(ue.getTenantId() == null){
                    userType = "超管";
                } else {
                    userType = "普通";
                }
                ue.setUserType(userType);
                //是否经理
                String leaderFlagStr = "";
                if("1".equals(ue.getLeaderFlag())) {
                    leaderFlagStr = "是";
                } else {
                    leaderFlagStr = "否";
                }
                ue.setLeaderFlagStr(leaderFlagStr);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countUser(String userName, String loginName) {
        Long result=null;
        try{
            result=userMapperEx.countsByUser(userName, loginName);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }
    /**
     * description:
     * 添加事务控制
     * @Param: beanJson
     * @Param: request
     * @return int
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertUser(JSONObject obj, HttpServletRequest request) {
        User user = JSONObject.parseObject(obj.toJSONString(), User.class);
        String password = "123456";
        //因密码用MD5加密，需要对密码进行转化
        try {
            password = Tools.md5Encryp(password);
            user.setPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error(">>>>>>>>>>>>>>转化MD5字符串错误 ：" + e.getMessage());
        }
        int result=0;
        try{
            result=userMapper.insertSelective(user);
            logService.insertLog("用户",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + user.getLoginName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    /**
     * description:
     * 添加事务控制
     * @Param: beanJson
     * @Param: id
     * @return int
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateUser(JSONObject obj, HttpServletRequest request) {
        User user = JSONObject.parseObject(obj.toJSONString(), User.class);
        int result=0;
        try{
            result=userMapper.updateByPrimaryKeySelective(user);
            logService.insertLog("用户",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + user.getLoginName(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    /**
     * description:
     * 添加事务控制
     * @Param: user
     * @return int
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateUserByObj(User user) {
        logService.insertLog("用户",
                BusinessConstants.LOG_OPERATION_TYPE_EDIT + user.getId(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        int result=0;
        try{
            result=userMapper.updateByPrimaryKeySelective(user);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }
    /**
     * description:
     *  添加事务控制
     * @Param: md5Pwd
     * @Param: id
     * @return int
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int resetPwd(String md5Pwd, Long id) {
        int result=0;
        logService.insertLog("用户",
                BusinessConstants.LOG_OPERATION_TYPE_EDIT + id,
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User u = getUser(id);
        String loginName = u.getLoginName();
        if("admin".equals(loginName)){
            logger.info("禁止重置超管密码");
        } else {
            User user = new User();
            user.setId(id);
            user.setPassword(md5Pwd);
            try{
                result=userMapper.updateByPrimaryKeySelective(user);
            }catch(Exception e){
                JshException.writeFail(logger, e);
            }
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteUser(Long id, HttpServletRequest request) {
        return batDeleteUser(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteUser(String ids, HttpServletRequest request) {
        return batDeleteUser(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batDeleteUser(String ids) {
        int result=0;
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<User> list = getUserListByIds(ids);
        for(User user: list){
            if(user.getId().equals(user.getTenantId())) {
                logger.error("异常码[{}],异常提示[{}],参数,ids:[{}]",
                        ExceptionConstants.USER_LIMIT_TENANT_DELETE_CODE,ExceptionConstants.USER_LIMIT_TENANT_DELETE_MSG,ids);
                throw new BusinessRunTimeException(ExceptionConstants.USER_LIMIT_TENANT_DELETE_CODE,
                        ExceptionConstants.USER_LIMIT_TENANT_DELETE_MSG);
            }
            sb.append("[").append(user.getLoginName()).append("]");
        }
        logService.insertLog("用户", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        String idsArray[]=ids.split(",");
        try{
            result=userMapperEx.batDeleteOrUpdateUser(idsArray,BusinessConstants.USER_STATUS_DELETE);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        if(result<1){
            logger.error("异常码[{}],异常提示[{}],参数,ids:[{}]",
                    ExceptionConstants.USER_DELETE_FAILED_CODE,ExceptionConstants.USER_DELETE_FAILED_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.USER_DELETE_FAILED_CODE,
                    ExceptionConstants.USER_DELETE_FAILED_MSG);
        }
        return result;
    }

    /**
     * 用户登录
     * @param userParam
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String, Object> login(User userParam, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        String msgTip = "";
        User user=null;
        String loginName = userParam.getLoginName().trim();
        String password = userParam.getPassword().trim();
        //判断用户是否已经登录过，登录过不再处理
        Object userId = redisService.getObjectFromSessionByKey(request,"userId");
        if (userId != null) {
            logger.info("====用户已经登录过, login 方法调用结束====");
            msgTip = "user already login";
        }
        //获取用户状态
        int userStatus = -1;
        try {
            redisService.deleteObjectBySession(request,"userId");
            userStatus = validateUser(loginName, password);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(">>>>>>>>>>>>>用户  " + loginName + " 登录 login 方法 访问服务层异常====", e);
            msgTip = "access service exception";
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "") + "";
        switch (userStatus) {
            case ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST:
                msgTip = "user is not exist";
                break;
            case ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR:
                msgTip = "user password error";
                break;
            case ExceptionCodeConstants.UserExceptionCode.BLACK_USER:
                msgTip = "user is black";
                break;
            case ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION:
                msgTip = "access service error";
                break;
            case ExceptionCodeConstants.UserExceptionCode.BLACK_TENANT:
                msgTip = "tenant is black";
                break;
            case ExceptionCodeConstants.UserExceptionCode.EXPIRE_TENANT:
                msgTip = "tenant is expire";
                break;
            case ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT:
                msgTip = "user can login";
                //验证通过 ，可以登录，放入session，记录登录日志
                user = getUserByLoginName(loginName);
                if(user.getTenantId()!=null) {
                    token = token + "_" + user.getTenantId();
                }
                redisService.storageObjectBySession(token,"userId",user.getId());
                break;
            default:
                break;
        }
        data.put("msgTip", msgTip);
        if(user!=null){
            String roleType = getRoleTypeByUserId(user.getId()).getType(); //角色类型
            redisService.storageObjectBySession(token,"roleType",roleType);
            redisService.storageObjectBySession(token,"clientIp", Tools.getLocalIp(request));
            logService.insertLogWithUserId(user.getId(), user.getTenantId(), "用户",
                    BusinessConstants.LOG_OPERATION_TYPE_LOGIN + user.getLoginName(),
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            JSONArray btnStrArr = getBtnStrArrById(user.getId());
            data.put("token", token);
            data.put("user", user);
            //用户的按钮权限
            if(!"admin".equals(user.getLoginName())){
                data.put("userBtn", btnStrArr);
            }
            data.put("roleType", roleType);
        }
        return data;
    }

    public int validateUser(String loginName, String password) {
        /**默认是可以登录的*/
        List<User> list = null;
        try {
            UserExample example = new UserExample();
            example.createCriteria().andLoginNameEqualTo(loginName).andStatusNotEqualTo(BusinessConstants.USER_STATUS_DELETE);
            list = userMapper.selectByExample(example);
            if (null != list && list.isEmpty()) {
                return ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST;
            } else if(list.size() ==1) {
                if(list.get(0).getStatus()!=0) {
                    return ExceptionCodeConstants.UserExceptionCode.BLACK_USER;
                }
                Long tenantId = list.get(0).getTenantId();
                Tenant tenant = tenantService.getTenantByTenantId(tenantId);
                if(tenant!=null) {
                    if(tenant.getEnabled()!=null && !tenant.getEnabled()) {
                        return ExceptionCodeConstants.UserExceptionCode.BLACK_TENANT;
                    }
                    if(tenant.getExpireTime()!=null && tenant.getExpireTime().getTime()<System.currentTimeMillis()){
                        return ExceptionCodeConstants.UserExceptionCode.EXPIRE_TENANT;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>访问验证用户姓名是否存在后台信息异常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }
        try {
            UserExample example = new UserExample();
            example.createCriteria().andLoginNameEqualTo(loginName).andPasswordEqualTo(password)
                    .andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
            list = userMapper.selectByExample(example);
            if (null != list && list.size() == 0) {
                return ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR;
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>访问验证用户密码后台信息异常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }
        return ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT;
    }

    public User getUserByLoginName(String loginName) {
        UserExample example = new UserExample();
        example.createCriteria().andLoginNameEqualTo(loginName).andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
        List<User> list=null;
        try{
            list= userMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        User user =null;
        if(list!=null&&list.size()>0){
            user = list.get(0);
        }
        return user;
    }

    public int checkIsNameExist(Long id, String name) {
        UserExample example = new UserExample();
        List <Byte> userStatus=new ArrayList<Byte>();
        userStatus.add(BusinessConstants.USER_STATUS_DELETE);
        userStatus.add(BusinessConstants.USER_STATUS_BANNED);
        example.createCriteria().andIdNotEqualTo(id).andLoginNameEqualTo(name).andStatusNotIn(userStatus);
        List<User> list=null;
        try{
            list= userMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }
    /**
     * description:
     *  获取当前用户信息
     * @Param:
     * @return com.jsh.erp.datasource.entities.User
     */
    public User getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Long userId = Long.parseLong(redisService.getObjectFromSessionByKey(request,"userId").toString());
        return getUser(userId);
    }

    /**
     * 根据用户名查询id
     * @param loginName
     * @return
     */
    public Long getIdByLoginName(String loginName) {
        Long userId = 0L;
        UserExample example = new UserExample();
        example.createCriteria().andLoginNameEqualTo(loginName).andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
        List<User> list = userMapper.selectByExample(example);
        if(list!=null) {
            userId = list.get(0).getId();
        }
        return userId;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addUserAndOrgUserRel(UserEx ue, HttpServletRequest request) {
        if(BusinessConstants.DEFAULT_MANAGER.equals(ue.getLoginName())) {
            throw new BusinessRunTimeException(ExceptionConstants.USER_NAME_LIMIT_USE_CODE,
                    ExceptionConstants.USER_NAME_LIMIT_USE_MSG);
        } else {
            logService.insertLog("用户",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD,
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            //检查用户名和登录名
            checkLoginName(ue);
            //新增用户信息
            ue= this.addUser(ue);
            if(ue==null){
                logger.error("异常码[{}],异常提示[{}],参数,[{}]",
                        ExceptionConstants.USER_ADD_FAILED_CODE,ExceptionConstants.USER_ADD_FAILED_MSG);
                throw new BusinessRunTimeException(ExceptionConstants.USER_ADD_FAILED_CODE,
                        ExceptionConstants.USER_ADD_FAILED_MSG);
            }
            //用户id，根据用户名查询id
            Long userId = getIdByLoginName(ue.getLoginName());
            if(ue.getRoleId()!=null){
                JSONObject ubObj = new JSONObject();
                ubObj.put("type", "UserRole");
                ubObj.put("keyid", userId);
                ubObj.put("value", "[" + ue.getRoleId() + "]");
                userBusinessService.insertUserBusiness(ubObj, request);
            }
            if(ue.getOrgaId()==null){
                //如果没有选择机构，就不建机构和用户的关联关系
                return;
            }
            if(ue.getOrgaId()!=null && "1".equals(ue.getLeaderFlag())){
                //检查当前机构是否存在经理
                List<User> checkList = userMapperEx.getListByOrgaId(ue.getId(), ue.getOrgaId());
                if(checkList.size()>0) {
                    throw new BusinessRunTimeException(ExceptionConstants.USER_LEADER_IS_EXIST_CODE,
                            ExceptionConstants.USER_LEADER_IS_EXIST_MSG);
                }
            }
            //新增用户和机构关联关系
            OrgaUserRel oul=new OrgaUserRel();
            //机构id
            oul.setOrgaId(ue.getOrgaId());
            oul.setUserId(userId);
            //用户在机构中的排序
            oul.setUserBlngOrgaDsplSeq(ue.getUserBlngOrgaDsplSeq());
            oul = orgaUserRelService.addOrgaUserRel(oul);
            if(oul==null){
                logger.error("异常码[{}],异常提示[{}],参数,[{}]",
                        ExceptionConstants.ORGA_USER_REL_ADD_FAILED_CODE,ExceptionConstants.ORGA_USER_REL_ADD_FAILED_MSG);
                throw new BusinessRunTimeException(ExceptionConstants.ORGA_USER_REL_ADD_FAILED_CODE,
                        ExceptionConstants.ORGA_USER_REL_ADD_FAILED_MSG);
            }
        }
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public UserEx addUser(UserEx ue) {
        /**
         * 新增用户默认设置
         * 1、密码默认123456
         * 2是否系统自带默认为非系统自带
         * 3是否管理者默认为员工
         * 4默认用户状态为正常
         * */
        if(ue.getIsmanager()==null){
            ue.setIsmanager(BusinessConstants.USER_NOT_MANAGER);
        }
        ue.setStatus(BusinessConstants.USER_STATUS_NORMAL);
        int result=0;
        try{
            ue.setPassword(Tools.md5Encryp(BusinessConstants.USER_DEFAULT_PASSWORD));
            ue.setIsystem(BusinessConstants.USER_NOT_SYSTEM);
            result= userMapper.insertSelective(ue);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        if(result>0){
            return ue;
        }
        return null;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public UserEx registerUser(UserEx ue, Integer manageRoleId, HttpServletRequest request) {
        /**
         * create by: qiankunpingtai
         * create time: 2019/4/9 18:00
         * 多次创建事务，事物之间无法协同，应该在入口处创建一个事务以做协调
         */
        if(BusinessConstants.DEFAULT_MANAGER.equals(ue.getLoginName())) {
            throw new BusinessRunTimeException(ExceptionConstants.USER_NAME_LIMIT_USE_CODE,
                    ExceptionConstants.USER_NAME_LIMIT_USE_MSG);
        } else {
            ue.setPassword(ue.getPassword());
            ue.setIsystem(BusinessConstants.USER_NOT_SYSTEM);
            if (ue.getIsmanager() == null) {
                ue.setIsmanager(BusinessConstants.USER_NOT_MANAGER);
            }
            ue.setStatus(BusinessConstants.USER_STATUS_NORMAL);
            int result=0;
            try{
                result= userMapper.insertSelective(ue);
                Long userId = getIdByLoginName(ue.getLoginName());
                ue.setId(userId);
            }catch(Exception e){
                JshException.writeFail(logger, e);
            }
            //更新租户id
            User user = new User();
            user.setId(ue.getId());
            user.setTenantId(ue.getId());
            updateUserTenant(user);
            //新增用户与角色的关系
            JSONObject ubObj = new JSONObject();
            ubObj.put("type", "UserRole");
            ubObj.put("keyid", ue.getId());
            JSONArray ubArr = new JSONArray();
            ubArr.add(manageRoleId);
            ubObj.put("value", ubArr.toString());
            ubObj.put("tenantId", ue.getId());
            userBusinessService.insertUserBusiness(ubObj, null);
            //创建租户信息
            JSONObject tenantObj = new JSONObject();
            tenantObj.put("tenantId", ue.getId());
            tenantObj.put("loginName",ue.getLoginName());
            tenantObj.put("userNumLimit", ue.getUserNumLimit());
            tenantObj.put("expireTime", ue.getExpireTime());
            tenantObj.put("remark", ue.getRemark());
            tenantService.insertTenant(tenantObj, request);
            logger.info("===============创建租户信息完成===============");
            if (result > 0) {
                return ue;
            }
            return null;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateUserTenant(User user) {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(user.getId());
        try{
            userMapper.updateByPrimaryKeySelective(user);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateUserAndOrgUserRel(UserEx ue, HttpServletRequest request) {
        if(BusinessConstants.DEFAULT_MANAGER.equals(ue.getLoginName())) {
            throw new BusinessRunTimeException(ExceptionConstants.USER_NAME_LIMIT_USE_CODE,
                    ExceptionConstants.USER_NAME_LIMIT_USE_MSG);
        } else {
            logService.insertLog("用户",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + ue.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            //检查用户名和登录名
            checkLoginName(ue);
            //更新用户信息
            ue = this.updateUser(ue);
            if (ue == null) {
                logger.error("异常码[{}],异常提示[{}],参数,[{}]",
                        ExceptionConstants.USER_EDIT_FAILED_CODE, ExceptionConstants.USER_EDIT_FAILED_MSG);
                throw new BusinessRunTimeException(ExceptionConstants.USER_EDIT_FAILED_CODE,
                        ExceptionConstants.USER_EDIT_FAILED_MSG);
            }
            if(ue.getRoleId()!=null){
                JSONObject ubObj = new JSONObject();
                ubObj.put("type", "UserRole");
                ubObj.put("keyid", ue.getId());
                ubObj.put("value", "[" + ue.getRoleId() + "]");
                Long ubId = userBusinessService.checkIsValueExist("UserRole", ue.getId().toString());
                if(ubId!=null) {
                    ubObj.put("id", ubId);
                    userBusinessService.updateUserBusiness(ubObj, request);
                } else {
                    userBusinessService.insertUserBusiness(ubObj, request);
                }
            }
            if (ue.getOrgaId() == null) {
                //如果没有选择机构，就不建机构和用户的关联关系
                return;
            }
            if(ue.getOrgaId()!=null && "1".equals(ue.getLeaderFlag())){
                //检查当前机构是否存在经理
                List<User> checkList = userMapperEx.getListByOrgaId(ue.getId(), ue.getOrgaId());
                if(checkList.size()>0) {
                    throw new BusinessRunTimeException(ExceptionConstants.USER_LEADER_IS_EXIST_CODE,
                            ExceptionConstants.USER_LEADER_IS_EXIST_MSG);
                }
            }
            //更新用户和机构关联关系
            OrgaUserRel oul = new OrgaUserRel();
            //机构和用户关联关系id
            oul.setId(ue.getOrgaUserRelId());
            //机构id
            oul.setOrgaId(ue.getOrgaId());
            //用户id
            oul.setUserId(ue.getId());
            //用户在机构中的排序
            oul.setUserBlngOrgaDsplSeq(ue.getUserBlngOrgaDsplSeq());
            if (oul.getId() != null) {
                //已存在机构和用户的关联关系，更新
                oul = orgaUserRelService.updateOrgaUserRel(oul);
            } else {
                //不存在机构和用户的关联关系，新建
                oul = orgaUserRelService.addOrgaUserRel(oul);
            }
            if (oul == null) {
                logger.error("异常码[{}],异常提示[{}],参数,[{}]",
                        ExceptionConstants.ORGA_USER_REL_EDIT_FAILED_CODE, ExceptionConstants.ORGA_USER_REL_EDIT_FAILED_MSG);
                throw new BusinessRunTimeException(ExceptionConstants.ORGA_USER_REL_EDIT_FAILED_CODE,
                        ExceptionConstants.ORGA_USER_REL_EDIT_FAILED_MSG);
            }
        }
    }
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public UserEx updateUser(UserEx ue) {
        int result =0;
        try{
            result=userMapper.updateByPrimaryKeySelective(ue);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        if(result>0){
            return ue;
        }
        return null;
    }
    /**
     *  检查登录名不能重复
     * @Param: userEx
     * @return void
     */
    public void checkLoginName(UserEx userEx) {
        List<User> list=null;
        if(userEx==null){
            return;
        }
        Long userId=userEx.getId();
        //检查登录名
        if(!StringUtils.isEmpty(userEx.getLoginName())){
            String loginName=userEx.getLoginName();
            list=this.getUserListByloginName(loginName);
            if(list!=null&&list.size()>0){
                if(list.size()>1){
                    //超过一条数据存在，该登录名已存在
                    logger.error("异常码[{}],异常提示[{}],参数,loginName:[{}]",
                            ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_CODE,ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_MSG,loginName);
                    throw new BusinessRunTimeException(ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_CODE,
                            ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_MSG);
                }
                //一条数据，新增时抛出异常，修改时和当前的id不同时抛出异常
                if(list.size()==1){
                    if(userId==null||(userId!=null&&!userId.equals(list.get(0).getId()))){
                        logger.error("异常码[{}],异常提示[{}],参数,loginName:[{}]",
                                ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_CODE,ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_MSG,loginName);
                        throw new BusinessRunTimeException(ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_CODE,
                                ExceptionConstants.USER_LOGIN_NAME_ALREADY_EXISTS_MSG);
                    }
                }
            }
        }
    }
    /**
     * 通过登录名获取用户列表
     * */
    public List<User> getUserListByloginName(String loginName) {
        List<User> list =null;
        try{
            list=userMapperEx.getUserListByUserNameOrLoginName(null,loginName);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<TreeNodeEx> getOrganizationUserTree() {
        List<TreeNodeEx> list =null;
        try{
            list=userMapperEx.getNodeTree();
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    /**
     * 根据用户id查询角色信息
     * @param userId
     * @return
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public Role getRoleTypeByUserId(long userId) {

        List<UserBusiness> list = userBusinessService.getBasicData(String.valueOf(userId), "UserRole");
        String roleId = "null";
        UserBusiness ub = null;
        if(!list.isEmpty()) {
            ub = list.get(0);
            String values = ub.getValue();
            if(values!=null) {
                values = values.replaceAll("\\[\\]",",").replace("[","").replace("]","");
            }
            String [] valueArray=values.split(",");
            if(valueArray.length>0) {
                roleId = valueArray[0];
            }
        }
        Role role = roleMapperEx.getRoleWithoutTenant(Long.valueOf(roleId));
        return role;
    }

    /**
     * 获取用户id
     * @param request
     * @return
     */
    public Long getUserId(HttpServletRequest request) {
        Object userIdObj = redisService.getObjectFromSessionByKey(request,"userId");
        Long userId = null;
        if(userIdObj != null) {
            userId = Long.parseLong(userIdObj.toString());
        }
        return userId;
    }

    /**
     * 用户的按钮权限
     * @param userId
     * @return
     * @throws Exception
     */
    public JSONArray getBtnStrArrById(Long userId) {
        JSONArray btnStrArr = new JSONArray();
        List<UserBusiness> userRoleList = userBusinessService.getBasicData(userId.toString(), "UserRole");
        if(userRoleList!=null && !userRoleList.isEmpty()) {
            String roleValue = userRoleList.get(0).getValue();
            if(StringUtil.isNotEmpty(roleValue) && roleValue.contains("[") && roleValue.contains("]")){
                roleValue = roleValue.replace("[", "").replace("]", ""); //角色id-单个
                List<UserBusiness> roleFunctionsList = userBusinessService.getBasicData(roleValue, "RoleFunctions");
                if(roleFunctionsList!=null && !roleFunctionsList.isEmpty()) {
                    String btnStr = roleFunctionsList.get(0).getBtnStr();
                    if(StringUtil.isNotEmpty(btnStr)){
                        btnStrArr = JSONArray.parseArray(btnStr);
                    }
                }
            }
        }
        //将数组中的funId转为url
        JSONArray btnStrWithUrlArr = new JSONArray();
        if(!btnStrArr.isEmpty()) {
            List<Function> functionList = functionService.getFunction();
            Map<Long, String> functionMap = new HashMap<>();
            for (Function function: functionList) {
                functionMap.put(function.getId(), function.getUrl());
            }
            for (Object obj : btnStrArr) {
                JSONObject btnStrObj = JSONObject.parseObject(obj.toString());
                Long funId = btnStrObj.getLong("funId");
                JSONObject btnStrWithUrlObj = new JSONObject();
                btnStrWithUrlObj.put("url", functionMap.get(funId));
                btnStrWithUrlObj.put("btnStr", btnStrObj.getString("btnStr"));
                btnStrWithUrlArr.add(btnStrWithUrlObj);
            }
        }
        return btnStrWithUrlArr;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchSetStatus(Byte status, String ids, HttpServletRequest request) {
        int result=0;
        List<User> list = getUserListByIds(ids);
        //选中的用户的数量
        int selectUserSize = list.size();
        //查询启用状态的用户的数量
        int enableUserSize = getUser().size();
        User userInfo = getCurrentUser();
        Tenant tenant = tenantService.getTenantByTenantId(userInfo.getTenantId());
        if(tenant!=null) {
            if (selectUserSize + enableUserSize > tenant.getUserNumLimit() && status == 0) {
//                throw new BusinessParamCheckingException(ExceptionConstants.USER_ENABLE_OVER_LIMIT_FAILED_CODE,
//                        ExceptionConstants.USER_ENABLE_OVER_LIMIT_FAILED_MSG);
            }
        }
        StringBuilder userStr = new StringBuilder();
        List<Long> idList = new ArrayList<>();
        for(User user: list) {
            if(user.getId().equals(user.getTenantId())) {
                //租户不能进行禁用
            } else {
                idList.add(user.getId());
                userStr.append(user.getLoginName()).append(" ");
            }
        }
        String statusStr ="";
        if(status == 0) {
            statusStr ="批量启用";
        } else if(status == 2) {
            statusStr ="批量禁用";
        }
        if(idList.size()>0) {
            User user = new User();
            user.setStatus(status);
            UserExample example = new UserExample();
            example.createCriteria().andIdIn(idList);
            result = userMapper.updateByExampleSelective(user, example);
            logService.insertLog("用户",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + userStr + "-" + statusStr,
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        } else {
            result = 1;
        }
        return result;
    }

    public User getUserByWeixinCode(String weixinCode) {
        String weixinUrl = platformConfigService.getPlatformConfigByKey("weixinUrl").getPlatformValue();
        String weixinAppid = platformConfigService.getPlatformConfigByKey("weixinAppid").getPlatformValue();
        String weixinSecret = platformConfigService.getPlatformConfigByKey("weixinSecret").getPlatformValue();
        String url = weixinUrl + "?appid=" + weixinAppid + "&secret=" + weixinSecret + "&js_code=" + weixinCode
                + "&grant_type=authorization_code";
        JSONObject jsonObject = HttpClient.httpGet(url);
        if(jsonObject!=null) {
            String weixinOpenId = jsonObject.getString("openid");
            if(StringUtil.isNotEmpty(weixinOpenId)) {
                return userMapperEx.getUserByWeixinOpenId(weixinOpenId);
            }
        }
        return null;
    }

    public int weixinBind(String loginName, String password, String weixinCode) {
        String weixinUrl = platformConfigService.getPlatformConfigByKey("weixinUrl").getPlatformValue();
        String weixinAppid = platformConfigService.getPlatformConfigByKey("weixinAppid").getPlatformValue();
        String weixinSecret = platformConfigService.getPlatformConfigByKey("weixinSecret").getPlatformValue();
        String url = weixinUrl + "?appid=" + weixinAppid + "&secret=" + weixinSecret + "&js_code=" + weixinCode
                + "&grant_type=authorization_code";
        JSONObject jsonObject = HttpClient.httpGet(url);
        if(jsonObject!=null) {
            String weixinOpenId = jsonObject.getString("openid");
            if(StringUtil.isNotEmpty(weixinOpenId)) {
                return userMapperEx.updateUserWithWeixinOpenId(loginName, password, weixinOpenId);
            }
        }
        return 0;
    }
}
