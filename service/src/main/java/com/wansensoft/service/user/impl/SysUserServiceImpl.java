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
package com.wansensoft.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wansensoft.dto.user.*;
import com.wansensoft.entities.user.SysUser;
import com.wansensoft.entities.user.SysUserDeptRel;
import com.wansensoft.entities.user.SysUserRoleRel;
import com.wansensoft.mappers.role.SysRoleMapper;
import com.wansensoft.mappers.system.SysDepartmentMapper;
import com.wansensoft.mappers.user.SysUserMapper;
import com.wansensoft.middleware.security.JWTUtil;
import com.wansensoft.service.user.ISysUserDeptRelService;
import com.wansensoft.service.user.ISysUserRoleRelService;
import com.wansensoft.service.user.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.utils.SnowflakeIdUtil;
import com.wansensoft.utils.enums.UserCodeEnum;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.CommonTools;
import com.wansensoft.utils.constants.SecurityConstants;
import com.wansensoft.utils.enums.BaseCodeEnum;
import com.wansensoft.utils.redis.RedisUtil;
import com.wansensoft.vo.UserInfoVO;
import com.wansensoft.vo.UserListVO;
import com.wansensoft.vo.UserRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public SysUserServiceImpl(SysUserMapper userMapper, RedisUtil redisUtil, JWTUtil jwtUtil, ISysUserRoleRelService userRoleRelService, ISysUserDeptRelService userDeptRelService, SysRoleMapper roleMapper, SysDepartmentMapper departmentMapper) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
        this.userRoleRelService = userRoleRelService;
        this.userDeptRelService = userDeptRelService;
        this.roleMapper = roleMapper;
        this.departmentMapper = departmentMapper;
    }


    @Override
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
        var isRegister = lambdaQuery()
                .eq(SysUser::getUserName, accountRegisterDto.getUsername())
                .exists();
        if (isRegister) {
            return Response.responseMsg(UserCodeEnum.USER_NAME_EXISTS);
        }

        var checkPhoneNumber = lambdaQuery()
                .eq(SysUser::getPhoneNumber, accountRegisterDto.getPhoneNumber())
                .exists();
        if (checkPhoneNumber) {
            return Response.responseMsg(UserCodeEnum.PHONE_EXISTS);
        }

        // start register
        boolean result = save(SysUser.builder()
                .id(SnowflakeIdUtil.nextId())
                .userName(accountRegisterDto.getUsername())
                .name("测试租户")
                .password(CommonTools.md5Encryp(accountRegisterDto.getPassword()))
                .phoneNumber(accountRegisterDto.getPhoneNumber())
                .tenantId(SnowflakeIdUtil.nextId())
                .build());
        if (!result) {
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
                        .userName(user.getUserName())
                        .avatar(user.getAvatar())
                        .build();
            }
        }
        return null;
    }

    @Override
    public String getCurrentUserId() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":userId");
    }

    @Override
    public String getCurrentTenantId() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":tenantId");
    }

    @Override
    public String getCurrentUserName() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":userName");
    }

    @Override
    public Response<List<UserRoleVO>> userRole() {
        var userRoleVos = new ArrayList<UserRoleVO>();

        var userId = Long.parseLong(getCurrentUserId());
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
            userRoleVo.setRoleName(item.getName());
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
        if (StringUtils.hasText(userListDto.getDepartmentId())) {
            var userDeptRelList = userDeptRelService.lambdaQuery()
                    .eq(SysUserDeptRel::getDeptId, userListDto.getDepartmentId())
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
        userMapper.selectPage(page, query);

        var resultIds = page.getRecords().stream().map(SysUser::getId).toList();

        // query role info need roleName
        var userRoles =  userRoleRelService.queryBatchByUserIds(resultIds);
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
            userRoles.forEach(userRole -> {
                roles.forEach(role -> {
                    if(Objects.equals(item.getId(), userRole.getUserId()) &&
                            Objects.equals(userRole.getRoleId(), role.getId())) {
                        userVo.setRoleId(role.getId());
                        userVo.setRoleName(role.getName());
                    }
                });
            });
            // bind deptName
            userDepartments.forEach(userDept -> {
                departments.forEach(dept -> {
                    if(Objects.equals(item.getId(), userDept.getUserId()) &&
                            Objects.equals(userDept.getDeptId(), dept.getId())) {
                        userVo.setDeptId(dept.getId());
                        userVo.setDeptName(dept.getName());
                    }
                });
            });

            userListVos.add(userVo);
        });
        result.setRecords(userListVos);
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setPages(page.getPages());

        return Response.responseData(result);
    }

    @Override
    public Response<String> updateUser(UpdateUserDTO updateUserDTO) {
        if(updateUserDTO == null) {
            return Response.responseMsg(BaseCodeEnum.PARAMETER_NULL);
        }

        var updateResult = lambdaUpdate()
                .eq(SysUser::getId, updateUserDTO.getId())
                .set(StringUtils.hasText(updateUserDTO.getName()), SysUser::getName, updateUserDTO.getName())
                .set(StringUtils.hasText(updateUserDTO.getEmail()), SysUser::getEmail, updateUserDTO.getEmail())
                .set(StringUtils.hasText(updateUserDTO.getPhoneNumber()), SysUser::getPhoneNumber, updateUserDTO.getPhoneNumber())
                .set(StringUtils.hasText(updateUserDTO.getPosition()), SysUser::getPosition, updateUserDTO.getPosition())
                .set(null != updateUserDTO.getStatus(), SysUser::getStatus, updateUserDTO.getStatus())
                .update();

        if(!updateResult) {
            return Response.responseMsg(UserCodeEnum.USER_INFO_UPDATE_ERROR);
        }

        return Response.responseMsg(UserCodeEnum.USER_INFO_UPDATE_SUCCESS);
    }
}
