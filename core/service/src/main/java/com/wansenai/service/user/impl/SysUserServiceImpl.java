/*
 * Copyright 2023-2033 WanSen AI Team, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://opensource.wansenai.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.wansenai.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansenai.entities.system.SysPlatformConfig;
import com.wansenai.entities.warehouse.Warehouse;
import com.wansenai.mappers.warehouse.WarehouseMapper;
import com.wansenai.middleware.oss.TencentOSS;
import com.wansenai.service.system.ISysPlatformConfigService;
import com.wansenai.utils.CommonTools;
import com.wansenai.utils.FileUtil;
import com.wansenai.utils.SnowflakeIdUtil;
import com.wansenai.dto.user.*;
import com.wansenai.utils.constants.*;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.UserCodeEnum;
import com.wansenai.utils.redis.RedisUtil;
import com.wansenai.utils.response.Response;
import com.wansenai.middleware.security.JWTUtil;
import com.wansenai.service.user.ISysUserDeptRelService;
import com.wansenai.service.user.ISysUserRoleRelService;
import com.wansenai.service.user.ISysUserService;
import com.wansenai.entities.SysDepartment;
import com.wansenai.entities.role.SysRole;
import com.wansenai.entities.role.SysRoleMenuRel;
import com.wansenai.entities.system.SysMenu;
import com.wansenai.entities.user.SysUser;
import com.wansenai.entities.user.SysUserDeptRel;
import com.wansenai.entities.user.SysUserRoleRel;
import com.wansenai.mappers.role.SysRoleMapper;
import com.wansenai.mappers.system.SysDepartmentMapper;
import com.wansenai.mappers.system.SysMenuMapper;
import com.wansenai.mappers.user.SysUserMapper;
import com.wansenai.service.role.SysRoleMenuRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansenai.vo.UserInfoVO;
import com.wansenai.vo.UserListVO;
import com.wansenai.vo.UserRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper userMapper;

    private final RedisUtil redisUtil;

    private final JWTUtil jwtUtil;

    private final ISysUserRoleRelService userRoleRelService;

    private final ISysUserDeptRelService userDeptRelService;

    private final SysRoleMapper roleMapper;

    private final SysDepartmentMapper departmentMapper;

    private final SysMenuMapper menuMapper;

    private final SysRoleMenuRelService roleMenuRelService;

    private final WarehouseMapper warehouseMapper;

    private final ISysPlatformConfigService platformConfigService;

    public SysUserServiceImpl(SysUserMapper userMapper, RedisUtil redisUtil, JWTUtil jwtUtil, ISysUserRoleRelService userRoleRelService,
                              ISysUserDeptRelService userDeptRelService, SysRoleMapper roleMapper, SysDepartmentMapper departmentMapper, SysMenuMapper menuMapper, SysRoleMenuRelService roleMenuRelService, WarehouseMapper warehouseMapper, ISysPlatformConfigService platformConfigService) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
        this.userRoleRelService = userRoleRelService;
        this.userDeptRelService = userDeptRelService;
        this.roleMapper = roleMapper;
        this.departmentMapper = departmentMapper;
        this.menuMapper = menuMapper;
        this.roleMenuRelService = roleMenuRelService;
        this.warehouseMapper = warehouseMapper;
        this.platformConfigService = platformConfigService;
    }


    @Override
    @Transactional
    public Response<String> accountRegister(AccountRegisterDTO accountRegisterDto) {
        if (accountRegisterDto == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var verifyCode = redisUtil.get(SecurityConstants.REGISTER_VERIFY_CODE_CACHE_PREFIX + accountRegisterDto.getPhoneNumber());
        if (ObjectUtils.isEmpty(verifyCode)) {
            return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_CODE_EXPIRE);
        }

        if (!String.valueOf(verifyCode).equals(accountRegisterDto.getSms())) {
            return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_CODE_ERROR);
        }

        // check if the username under the same tenant is duplicate
        if (checkUserNameExist(accountRegisterDto.getUsername())) {
            return Response.responseMsg(UserCodeEnum.USER_NAME_EXISTS);
        }

        if (checkPhoneNumberExist(accountRegisterDto.getPhoneNumber())) {
            return Response.responseMsg(UserCodeEnum.USER_REGISTER_PHONE_EXISTS);
        }

        // start register
        var userId = SnowflakeIdUtil.nextId();
        var user = SysUser.builder()
                .id(userId)
                .userName(accountRegisterDto.getUsername())
                .name("测试租户")
                .password(CommonTools.md5Encryp(accountRegisterDto.getPassword()))
                .phoneNumber(accountRegisterDto.getPhoneNumber())
                .tenantId(userId)
                .createTime(LocalDateTime.now())
                .build();
        // Assign the tenant administrator role and default department to newly registered tenants by default
        var role = SysRole.builder()
                .id(SnowflakeIdUtil.nextId())
                .tenantId(userId)
                .roleName(RoleConstants.DEFAULT_TENANT_ROLE_NAME)
                .type(RoleConstants.ROLE_TYPE_ALL_DATA)
                .description(RoleConstants.DEFAULT_TENANT_ROLE_REMARK)
                .status(CommonConstants.NOT_DELETED)
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();
        var department = SysDepartment.builder()
                .id(SnowflakeIdUtil.nextId())
                .tenantId(userId)
                .number(DepartmentConstants.DEFAULT_TENANT_DEPARTMENT_NUMBER)
                .name(DepartmentConstants.DEFAULT_TENANT_DEPARTMENT_NAME)
                .leader(String.valueOf(userId))
                .status(CommonConstants.NOT_DELETED)
                .remark(DepartmentConstants.DEFAULT_TENANT_DEPARTMENT_REMARK)
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();

        var userRoleRel = SysUserRoleRel.builder()
                .id(SnowflakeIdUtil.nextId())
                .roleId(role.getId())
                .userId(userId)
                .tenantId(userId)
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();

        var userDepartmentRel = SysUserDeptRel.builder()
                .id(SnowflakeIdUtil.nextId())
                .deptId(department.getId())
                .userId(userId)
                .tenantId(userId)
                .sort(0)
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();
        var menuIds = menuMapper.selectList(null)
                .stream()
                .map(SysMenu::getId)
                .filter(id -> id != 15)
                .toList();
        var menuIdStr = menuIds.toString().replaceAll(",\\s*", "][");
        var roleMenuRel = SysRoleMenuRel.builder()
                .id(SnowflakeIdUtil.nextId())
                .tenantId(userId)
                .roleId(role.getId())
                .menuId(menuIdStr)
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();
        // 2023-12-05 默认分配仓库
        var warehouse = Warehouse.builder()
                .id(SnowflakeIdUtil.nextId())
                .tenantId(userId)
                .warehouseManager(userId)
                .warehouseName("默认仓库")
                .remark("默认仓库")
                .createTime(LocalDateTime.now())
                .createBy(userId)
                .build();
        warehouseMapper.insert(warehouse);

        var userResult = save(user);
        var roleResult = roleMapper.insert(role);
        var departmentResult = departmentMapper.insert(department);
        var userRoleResult = userRoleRelService.save(userRoleRel);
        var userDepartmentResult = userDeptRelService.save(userDepartmentRel);
        var roleMenuResult = roleMenuRelService.save(roleMenuRel);

        if (!userResult && roleResult!=0 && departmentResult!=0 && !userRoleResult && !userDepartmentResult && !roleMenuResult) {
            return Response.fail();
        }

        return Response.responseMsg(UserCodeEnum.USER_REGISTER_SUCCESS);
    }

    @Override
    public Response<UserInfoVO> accountLogin(AccountLoginDTO accountLoginDto) {

        var verifyCode = redisUtil.get(SecurityConstants.EMAIL_VERIFY_CODE_CACHE_PREFIX + accountLoginDto.getCaptchaId());
        if (ObjectUtils.isEmpty(verifyCode)) {
            return Response.responseMsg(BaseCodeEnum.VERIFY_CODE_EXPIRE);
        }

        if (!String.valueOf(verifyCode).equals(accountLoginDto.getCaptcha())) {
            return Response.responseMsg(BaseCodeEnum.VERIFY_CODE_ERROR);
        }

        var user = lambdaQuery()
                .eq(SysUser::getUserName, accountLoginDto.getUsername())
                .eq(SysUser::getPassword, CommonTools.md5Encryp(accountLoginDto.getPassword()))
                .one();

        if (user == null) {
            return Response.responseMsg(UserCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        if (user.getStatus() == UserConstants.USER_STATUS_DISABLE) {
            return Response.responseMsg(UserCodeEnum.USER_ACCOUNT_FREEZE);
        }

        if (user.getDeleteFlag() == CommonConstants.DELETED) {
            return Response.responseMsg(UserCodeEnum.USER_ACCOUNT_INVALID);
        }

        var token = "";
        if (redisUtil.hasKey(user.getUserName() + ":token")) {
            token = String.valueOf(redisUtil.get(user.getUserName() + ":token"));
        } else {
            // 生成JWT的令牌
            token = jwtUtil.createToken(accountLoginDto.getUsername());
            redisUtil.set(accountLoginDto.getUsername() + ":token", token);
            redisUtil.expire(accountLoginDto.getUsername() + ":token", 86400);
            // 同时存放userId和userName 租户id
            redisUtil.set(token + ":userName", user.getUserName(), 86400);
            redisUtil.set(token + ":userId", String.valueOf(user.getId()), 86400);
            redisUtil.set(token + ":tenantId", String.valueOf(user.getTenantId()), 86400);
        }

        return Response.responseData(UserInfoVO.builder()
                .id(user.getId())
                .token(token)
                .expire(1694757956L)
                .build());
    }

    @Override
    public Response<UserInfoVO> mobileLogin(MobileLoginDTO mobileLoginDto) {
        var verifyCode = redisUtil.getString(SecurityConstants.LOGIN_VERIFY_CODE_CACHE_PREFIX + mobileLoginDto.getPhoneNumber());
        if (ObjectUtils.isEmpty(verifyCode)) {
            return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_CODE_EXPIRE);
        }

        if (!verifyCode.equals(mobileLoginDto.getSms())) {
            return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_CODE_ERROR);
        }

        var user = lambdaQuery()
                .eq(SysUser::getPhoneNumber, mobileLoginDto.getPhoneNumber())
                .one();

        if (user == null) {
            return Response.responseMsg(UserCodeEnum.USER_NOT_EXISTS);
        }

        if (user.getStatus() == UserConstants.USER_STATUS_DISABLE) {
            return Response.responseMsg(UserCodeEnum.USER_ACCOUNT_FREEZE);
        }

        if (user.getDeleteFlag() == CommonConstants.DELETED) {
            return Response.responseMsg(UserCodeEnum.USER_ACCOUNT_INVALID);
        }

        var token = "";
        if (redisUtil.hasKey(user.getUserName() + ":token")) {
            token = String.valueOf(redisUtil.get(user.getUserName() + ":token"));
        } else {
            // 生成JWT的令牌
            token = jwtUtil.createToken(user.getUserName());
            redisUtil.set(user.getUserName() + ":token", token);
            redisUtil.expire(user.getUserName() + ":token", 86400);
            var userId = String.valueOf(user.getId());
            var tenantId = String.valueOf(user.getTenantId());
            redisUtil.set(token + ":userName", user.getUserName(), 86400);
            redisUtil.set(token + ":userId", userId, 86400);
            redisUtil.set(token + ":tenantId", tenantId, 86400);
        }

        return Response.responseData(UserInfoVO.builder()
                .id(user.getId())
                .token(token)
                .expire(1694757956L)
                .build());
    }

    @Override
    public Response<String> updatePassword(UpdatePasswordDto updatePasswordDto) {
        if (updatePasswordDto == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var verifyCode = redisUtil.getString(SecurityConstants.UPDATE_PASSWORD_VERIFY_CODE_CACHE_PREFIX + updatePasswordDto.getPhoneNumber());
        if (ObjectUtils.isEmpty(verifyCode)) {
            return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_CODE_EXPIRE);
        }

        if (!verifyCode.equals(updatePasswordDto.getSms())) {
            return Response.responseMsg(BaseCodeEnum.SMS_VERIFY_CODE_ERROR);
        }

        var isExists = lambdaQuery()
                .eq(SysUser::getUserName, updatePasswordDto.getUsername())
                .eq(SysUser::getPhoneNumber, updatePasswordDto.getPhoneNumber())
                .one();

        if (isExists == null) {
            return Response.responseMsg(UserCodeEnum.USER_NOT_EXISTS);
        }

        var result = lambdaUpdate()
                .eq(SysUser::getUserName, updatePasswordDto.getUsername())
                .eq(SysUser::getPhoneNumber, updatePasswordDto.getPhoneNumber())
                .set(SysUser::getPassword, CommonTools.md5Encryp(updatePasswordDto.getPassword()))
                .update();

        if (!result) {
            return Response.responseMsg(UserCodeEnum.UPDATE_PASSWORD_ERROR);
        }

        return Response.responseMsg(UserCodeEnum.UPDATE_PASSWORD_SUCCESS);
    }

    @Override
    public Response<UserInfoVO> userInfo() {
        var user = getCurrentUser();
        if (user == null) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }
        return Response.responseData(user);
    }

    private String httpServletRequestContextToken() {
        var sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra == null) {
            log.error("[异常]获取HttpServletRequest为空");
        }
        return Optional.ofNullable(sra.getRequest().getHeader("Authorization")).orElseThrow(null);
    }

    /**
     * 通过请求获取当前用户信息
     *
     * @return UserInfoVo
     */
    @Override
    public UserInfoVO getCurrentUser() {
        var token = httpServletRequestContextToken();
        if (StringUtils.hasText(token)) {
            var userId = Long.parseLong(redisUtil.getString(token + ":userId"));
            var user = userMapper.selectById(userId);
            if (user != null) {
                return UserInfoVO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .position(user.getPosition())
                        .description(user.getDescription())
                        .phoneNumber(user.getPhoneNumber())
                        .userName(user.getUserName())
                        .avatar(user.getAvatar())
                        .build();
            }
        }
        return null;
    }

    @Override
    public Long getCurrentUserId() {
        var token = httpServletRequestContextToken();
        return Long.parseLong(redisUtil.getString(token + ":userId"));
    }

    @Override
    public Long getCurrentTenantId() {
        var token = httpServletRequestContextToken();
        return Long.parseLong(redisUtil.getString(token + ":tenantId"));
    }

    @Override
    public String getCurrentUserName() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":userName");
    }

    @Override
    public Response<List<UserRoleVO>> userRole() {
        var userRoleVos = new ArrayList<UserRoleVO>();

        var userId = getCurrentUserId();
        var ids = userRoleRelService.queryByUserId(userId).stream()
                .map(SysUserRoleRel::getRoleId).toList();
        if (ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.QUERY_DATA_EMPTY);
        }

        var roles = roleMapper.selectBatchIds(ids);
        roles.forEach(item -> {
            UserRoleVO userRoleVo = new UserRoleVO();
            userRoleVo.setRoleId(item.getId());
            userRoleVo.setRoleType(item.getType());
            userRoleVo.setRoleName(item.getRoleName());
            userRoleVos.add(userRoleVo);
        });

        return Response.responseData(userRoleVos);
    }

    @Override
    public Response<String> userLogout() {
        var token = httpServletRequestContextToken();
        redisUtil.del(token + ":userId", token + ":userName", getCurrentUserName() + ":token");
        return Response.responseMsg(UserCodeEnum.USER_LOGOUT);
    }

    /**
     * 这里要查询管理角色和部门表以获取 角色名称和 部门名称 主表user 关联表2张 部门和角色表各1张
     * 所以是5表联查，最好可以用mapper xml写，但我这里实现是通过关联数据集合筛选查询
     * 后续数据量大的情况下可以使用二分查询
     *
     * @param userListDto 用户列表查询数据请求对象
     * @return 返回用户列表
     */
    @Override
    public Response<Page<UserListVO>> userList(UserListDTO userListDto) {
        var result = new Page<UserListVO>();
        var userListVos = new ArrayList<UserListVO>();

        // Dept query
        var userIds = new ArrayList<Long>();
        if (StringUtils.hasText(userListDto.getDeptId())) {
            var userDeptRelList = userDeptRelService.lambdaQuery()
                    .eq(SysUserDeptRel::getDeptId, userListDto.getDeptId())
                    .list();
            if (!userDeptRelList.isEmpty()) {
                userIds.addAll(userDeptRelList.stream().map(SysUserDeptRel::getUserId).toList());
                userMapper.selectBatchIds(userIds);
            }
        }

        var user = userMapper.selectById(getCurrentUserId());
        var tenantId = user.getTenantId();

        Page<SysUser> page = new Page<>(userListDto.getPage(), userListDto.getPageSize());
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        if (!userIds.isEmpty()) {
            query.in(SysUser::getId, userIds);
        }
        query.eq(SysUser::getTenantId, tenantId);
        query.eq(StringUtils.hasText(userListDto.getEmail()), SysUser::getEmail, userListDto.getEmail());
        query.eq(StringUtils.hasText(userListDto.getUsername()), SysUser::getUserName, userListDto.getUsername());
        query.eq(StringUtils.hasText(userListDto.getName()), SysUser::getName, userListDto.getName());
        query.eq(StringUtils.hasText(userListDto.getPhoneNumber()), SysUser::getPhoneNumber, userListDto.getPhoneNumber());
        query.eq(SysUser::getDeleteFlag, CommonConstants.NOT_DELETED);
        userMapper.selectPage(page, query);

        var resultIds = page.getRecords().stream().map(SysUser::getId).toList();

        // query role info need roleName
        var userRoles = userRoleRelService.queryBatchByUserIds(resultIds);
        var roleIds = userRoles.stream().map(SysUserRoleRel::getRoleId).toList();
        var roles = roleMapper.selectBatchIds(roleIds);
        // query department info, need deptName
        var userDepartments = userDeptRelService.queryBatchByUserIds(resultIds);
        var deptIds = userDepartments.stream().map(SysUserDeptRel::getDeptId).toList();
        var departments = departmentMapper.selectBatchIds(deptIds);

        page.getRecords().forEach(item -> {
            UserListVO userVo = UserListVO.builder()
                    .id(item.getId())
                    .username(item.getUserName())
                    .name(item.getName())
                    .phoneNumber(item.getPhoneNumber())
                    .email(item.getEmail())
                    .status(item.getStatus())
                    .createTime(item.getCreateTime())
                    .build();
            // bind roleName
            var userRoleIds = new ArrayList<Long>();
            var roleName = new StringBuilder();
            userRoles.forEach(userRole -> {
                roles.forEach(role -> {
                    if (Objects.equals(item.getId(), userRole.getUserId()) &&
                            Objects.equals(userRole.getRoleId(), role.getId())) {
                        userRoleIds.add(role.getId());
                        roleName.append(role.getRoleName()).append(" ");
                    }
                });
            });
            userVo.setRoleName(String.valueOf(roleName));
            userVo.setRoleId(userRoleIds);
            // bind deptName
            var userDepartmentIds = new ArrayList<Long>();
            userDepartments.forEach(userDept -> {
                departments.forEach(dept -> {
                    if (Objects.equals(item.getId(), userDept.getUserId()) &&
                            Objects.equals(userDept.getDeptId(), dept.getId())) {
                        userDepartmentIds.add(dept.getId());
                    }
                });
            });
            userVo.setDeptId(userDepartmentIds);
            userListVos.add(userVo);
        });
        result.setRecords(userListVos);
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setPages(page.getPages());

        return Response.responseData(result);
    }

    @Override
    public Response<List<UserListVO>> userListAll() {
        List<UserListVO> userVOS =  new ArrayList<>();
        var users = lambdaQuery()
                .eq(SysUser::getTenantId, getCurrentTenantId())
                .list();
        users.forEach(item -> {
            UserListVO userVo = UserListVO.builder().build();
            BeanUtils.copyProperties(item, userVo);
            userVOS.add(userVo);
        });
        return Response.responseData(userVOS);
    }

    @Override
    public Response<String> updateUser(UpdateUserDTO updateUserDTO) {
        if (updateUserDTO == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var phoneExist = lambdaQuery()
                .eq(SysUser::getId, updateUserDTO.getId())
                .eq(SysUser::getPhoneNumber, updateUserDTO.getPhoneNumber())
                .one();
        if (phoneExist == null) {
            if(checkPhoneNumberExist(updateUserDTO.getPhoneNumber())) {
                return Response.responseMsg(UserCodeEnum.PHONE_EXISTS);
            }
        }
        var existEmail = lambdaQuery()
                .eq(SysUser::getEmail, updateUserDTO.getEmail())
                .eq(SysUser::getId, updateUserDTO.getId())
                .one();

        if (existEmail == null) {
            if(checkEmailExist(updateUserDTO.getEmail())) {
                return Response.responseMsg(UserCodeEnum.EMAIL_EXISTS);
            }
        }

        var updateResult = lambdaUpdate()
                .eq(SysUser::getId, updateUserDTO.getId())
                .set(SysUser::getName, updateUserDTO.getName())
                .set(SysUser::getEmail, updateUserDTO.getEmail())
                .set(SysUser::getPhoneNumber, updateUserDTO.getPhoneNumber())
                .set(SysUser::getPosition, updateUserDTO.getPosition())
                .set(SysUser::getDescription, updateUserDTO.getDescription())
                .set(null != updateUserDTO.getStatus(), SysUser::getStatus, updateUserDTO.getStatus())
                .update();

        if (!updateResult) {
            return Response.responseMsg(UserCodeEnum.USER_INFO_UPDATE_ERROR);
        }

        return Response.responseMsg(UserCodeEnum.USER_INFO_UPDATE_SUCCESS);
    }

    @Override
    public Response<String> uploadAvatar(MultipartFile file, Long userId, String name) {
        if (userId == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }
        var platform = platformConfigService.list().stream().filter(item -> item.getPlatformKey().startsWith("tencent_oss")).toList();
        var ossInfoMap = platform.stream().collect(Collectors.toMap(SysPlatformConfig::getPlatformKey, SysPlatformConfig::getPlatformValue));

        if (ossInfoMap.get("tencent_oss_secret_id") == null || ossInfoMap.get("tencent_oss_secret_key") == null
                || ossInfoMap.get("tencent_oss_region") == null || ossInfoMap.get("tencent_oss_bucket") == null) {
            return Response.responseMsg(BaseCodeEnum.OSS_KEY_NOT_EXIST);
        }

        TencentOSS.getInstance().setBucket(ossInfoMap.get("tencent_oss_bucket"));
        TencentOSS.getInstance().setRegion(ossInfoMap.get("tencent_oss_region"));
        TencentOSS.getInstance().setSecretid(ossInfoMap.get("tencent_oss_secret_id"));
        TencentOSS.getInstance().setSecretkey(ossInfoMap.get("tencent_oss_secret_key"));
        var instance = TencentOSS.getInstance();
        if(instance == null) {
            return Response.responseMsg(BaseCodeEnum.OSS_GET_INSTANCE_ERROR);
        }
        try {
            var key = "temp" + "_" + SnowflakeIdUtil.nextId() + "_" + name;
            var result = instance.upload(FileUtil.convertMultipartFilesToFile(file), key);
            log.info("上传文件信息: " + result);

            var updateResult = lambdaUpdate()
                    .eq(SysUser::getId, userId)
                    .set(SysUser::getAvatar, result)
                    .update();
            if (updateResult) {
                return Response.responseData(result);
            }
            return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
        }catch (Exception e) {
            log.error("上传文件失败: " + e.getMessage());
            return Response.responseMsg(BaseCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    private Boolean checkUserNameExist(String userName) {
        return lambdaQuery()
                .eq(SysUser::getUserName, userName)
                .exists();
    }

    private Boolean checkPhoneNumberExist(String phoneNumber) {
        return lambdaQuery()
                .eq(SysUser::getPhoneNumber, phoneNumber)
                .exists();
    }

    private Boolean checkEmailExist(String email) {
        return lambdaQuery()
                .eq(SysUser::getEmail, email)
                .exists();
    }

    private boolean addUserRoleRelations(Long userId, List<Long> roleIds) {
        List<SysUserRoleRel> userRoleReals = new ArrayList<>(roleIds.size());
        roleIds.forEach(roleId -> {
            var userRoleRel = SysUserRoleRel.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .roleId(roleId)
                    .userId(userId)
                    .tenantId(getCurrentTenantId())
                    .createBy(getCurrentUserId())
                    .createTime(LocalDateTime.now())
                    .build();
            userRoleReals.add(userRoleRel);
        });
        return userRoleRelService.saveBatch(userRoleReals);
    }

    private boolean addUserDeptRelations(Long userId, List<Long> deptIds) {
        List<SysUserDeptRel> userDeptReals = new ArrayList<>(deptIds.size());
        deptIds.forEach(deptId -> {
            var userDeptRel = SysUserDeptRel.builder()
                    .id(SnowflakeIdUtil.nextId())
                    .deptId(deptId)
                    .userId(userId)
                    .tenantId(getCurrentTenantId())
                    .createBy(getCurrentUserId())
                    .createTime(LocalDateTime.now())
                    .build();
            userDeptReals.add(userDeptRel);
        });
        return userDeptRelService.saveBatch(userDeptReals);
    }


    @Override
    @Transactional
    public Response<String> addOrUpdate(AddOrUpdateUserDTO addOrUpdateUserDTO) {
        if (addOrUpdateUserDTO.getId() != null) {
            var userExist = lambdaQuery()
                    .eq(SysUser::getId, addOrUpdateUserDTO.getId())
                    .eq(SysUser::getPhoneNumber, addOrUpdateUserDTO.getPhoneNumber())
                    .one();
            if (userExist == null) {
                if(checkPhoneNumberExist(addOrUpdateUserDTO.getPhoneNumber())) {
                    return Response.responseMsg(UserCodeEnum.PHONE_EXISTS);
                }
            }
            // update user info, Delete the original role and department association information
            var userRoles = userRoleRelService.queryByUserId(addOrUpdateUserDTO.getId());
            var userDepartments = userDeptRelService.queryByUserId(addOrUpdateUserDTO.getId());
            if(!userRoles.isEmpty()) {
                userRoleRelService.removeBatchByIds(userRoles.stream()
                        .map(SysUserRoleRel::getId)
                        .toList()
                );
            }
            if(!userDepartments.isEmpty()) {
                userDeptRelService.removeBatchByIds(userDepartments.stream()
                        .map(SysUserDeptRel::getId)
                        .toList()
                );
            }
            // Reassign roles and departments
            var saveUserRoleRealResult = addUserRoleRelations(addOrUpdateUserDTO.getId(), addOrUpdateUserDTO.getRoleId());
            var saveUserDeptRealResult = addUserDeptRelations(addOrUpdateUserDTO.getId(), addOrUpdateUserDTO.getDeptId());
            var updateUserResult = lambdaUpdate()
                    .eq(SysUser::getId, addOrUpdateUserDTO.getId())
                    .set(StringUtils.hasText(addOrUpdateUserDTO.getName()), SysUser::getName, addOrUpdateUserDTO.getName())
                    .set(StringUtils.hasText(addOrUpdateUserDTO.getPhoneNumber()), SysUser::getPhoneNumber, addOrUpdateUserDTO.getPhoneNumber())
                    .set(StringUtils.hasText(addOrUpdateUserDTO.getEmail()), SysUser::getEmail, addOrUpdateUserDTO.getEmail())
                    .set(StringUtils.hasText(addOrUpdateUserDTO.getRemake()), SysUser::getRemark, addOrUpdateUserDTO.getRemake())
                    .update();

            if(updateUserResult && saveUserRoleRealResult && saveUserDeptRealResult) {
                return Response.responseMsg(UserCodeEnum.USER_INFO_UPDATE_SUCCESS);
            }
            return Response.responseMsg(UserCodeEnum.USER_INFO_UPDATE_ERROR);
        } else {
            // Add user info
            if (checkUserNameExist(addOrUpdateUserDTO.getUsername())) {
                return Response.responseMsg(UserCodeEnum.USER_NAME_EXISTS);
            }

            if (checkPhoneNumberExist(addOrUpdateUserDTO.getPhoneNumber())) {
                return Response.responseMsg(UserCodeEnum.PHONE_EXISTS);
            }

            var userId = SnowflakeIdUtil.nextId();

            var password = "";
            if(StringUtils.hasText(addOrUpdateUserDTO.getPassword())) {
                password = CommonTools.md5Encryp(addOrUpdateUserDTO.getPassword());
            } else {
                password = CommonTools.md5Encryp(UserConstants.DEFAULT_PASSWORD);
            }

            var user = SysUser.builder()
                    .id(userId)
                    .userName(addOrUpdateUserDTO.getUsername())
                    .password(password)
                    .name(addOrUpdateUserDTO.getName())
                    .phoneNumber(addOrUpdateUserDTO.getPhoneNumber())
                    .email(addOrUpdateUserDTO.getEmail())
                    .createBy(getCurrentUserId())
                    .createTime(LocalDateTime.now())
                    .description(addOrUpdateUserDTO.getRemake())
                    .tenantId(getCurrentTenantId())
                    .build();

            var saveUserResult = save(user);

            // add dept relation and role relation
            var saveUserRoleRealResult = addUserRoleRelations(userId, addOrUpdateUserDTO.getRoleId());
            var saveUserDeptRealResult = addUserDeptRelations(userId, addOrUpdateUserDTO.getDeptId());

            if(saveUserResult && saveUserRoleRealResult && saveUserDeptRealResult) {
                return Response.responseMsg(UserCodeEnum.USER_ADD_SUCCESS);
            }
            return Response.responseMsg(UserCodeEnum.USER_ADD_ERROR);
        }
    }

    @Override
    public Response<String> deleteUser(List<Long> ids) {
        if(ids.isEmpty()) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        boolean deleteResult = lambdaUpdate()
                .in(SysUser::getId, ids)
                .set(SysUser::getDeleteFlag, CommonConstants.DELETED)
                .update();
        if(!deleteResult) {
            return Response.responseMsg(UserCodeEnum.USER_DELETE_ERROR);
        }

        return Response.responseMsg(UserCodeEnum.USER_DELETE_SUCCESS);
    }

    @Override
    public Response<String> resetPassword(Long id) {
        if(id == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        boolean resetResult = lambdaUpdate()
                .eq(SysUser::getId, id)
                .set(SysUser::getPassword, CommonTools.md5Encryp(UserConstants.DEFAULT_PASSWORD))
                .update();

        if(!resetResult) {
            return Response.responseMsg(UserCodeEnum.USER_RESET_PASSWORD_ERROR);
        }

        return Response.responseMsg(UserCodeEnum.USER_RESET_PASSWORD_SUCCESS);
    }

    @Override
    public Response<List<UserInfoVO>> operator() {
        var user = lambdaQuery()
                .eq(SysUser::getTenantId, getCurrentTenantId())
                .eq(SysUser::getStatus, UserConstants.USER_STATUS_ENABLE)
                .eq(SysUser::getDeleteFlag, CommonConstants.NOT_DELETED)
                .list();
        var result = new ArrayList<UserInfoVO>(user.size() + 1);
        for (SysUser sysUser : user) {
            var userVO = UserInfoVO.builder()
                    .id(sysUser.getId())
                    .name(sysUser.getName())
                    .userName(sysUser.getUserName())
                    .avatar(sysUser.getAvatar())
                    .build();
            result.add(userVO);
        }
        return Response.responseData(result);
    }
}
