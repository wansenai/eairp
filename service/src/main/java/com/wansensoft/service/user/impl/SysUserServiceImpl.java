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

import com.wansensoft.dto.user.AccountLoginDto;
import com.wansensoft.dto.user.AccountRegisterDto;
import com.wansensoft.entities.user.SysUser;
import com.wansensoft.entities.user.SysUserRoleRel;
import com.wansensoft.mappers.role.SysRoleMapper;
import com.wansensoft.mappers.user.SysUserMapper;
import com.wansensoft.middleware.security.JWTUtil;
import com.wansensoft.service.user.ISysUserRoleRelService;
import com.wansensoft.service.user.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.CommonTools;
import com.wansensoft.utils.constants.SecurityConstants;
import com.wansensoft.utils.enums.CodeEnum;
import com.wansensoft.utils.redis.RedisUtil;
import com.wansensoft.vo.UserInfoVo;
import com.wansensoft.vo.UserRoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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

    private final SysRoleMapper roleMapper;

    public SysUserServiceImpl(SysUserMapper userMapper, RedisUtil redisUtil, JWTUtil jwtUtil, ISysUserRoleRelService userRoleRelService, SysRoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
        this.userRoleRelService = userRoleRelService;
        this.roleMapper = roleMapper;
    }

    @Override
    public Response<String> accountRegister(AccountRegisterDto accountRegisterDto) {
        if (accountRegisterDto == null) {
            return Response.responseMsg(CodeEnum.PARAMETER_NULL);
        }

        // check if the username under the same tenant is duplicate


        return null;
    }

    @Override
    public Response<UserInfoVo> accountLogin(AccountLoginDto accountLoginDto) {

        var verifyCode = redisUtil.get(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + accountLoginDto.getCaptchaId());
        if(ObjectUtils.isEmpty(verifyCode)) {
            return Response.responseMsg(CodeEnum.VERIFY_CODE_EXPIRE);
        }

        if(!String.valueOf(verifyCode).equals(accountLoginDto.getCaptcha())) {
            return Response.responseMsg(CodeEnum.VERIFY_CODE_EXPIRE);
        }

        var md5Password = "";
        try {
            md5Password = CommonTools.md5Encryp(accountLoginDto.getPassword());
        }catch (NoSuchAlgorithmException e) {
            log.error("用户登录MD5密码解密错误: " + e.getMessage());
        }

        var user = lambdaQuery()
                .eq(SysUser::getUserName, accountLoginDto.getUsername())
                .eq(SysUser::getPassword, md5Password)
                .one();

        if(user == null) {
            return Response.responseMsg(CodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        var token = "";
        if(redisUtil.hasKey(user.getUserName() + ":token")) {
            token = String.valueOf(redisUtil.get(user.getUserName() + ":token"));
        } else {
            // 生成JWT的令牌
            token = jwtUtil.createToken(accountLoginDto.getUsername());
            redisUtil.set(user.getUserName() + ":token", token);
            redisUtil.expire(user.getUserName() + ":token", 86400);
            // 同时存放userId和userName
            var userId = String.valueOf(user.getId());
            redisUtil.set(token + ":userName", user.getUserName(), 86400);
            redisUtil.set(token + ":userId", userId, 86400);
        }

        return Response.responseData(UserInfoVo.builder()
                        .id(user.getId())
                        .token(token)
                        .expire(1694757956L)
                .build());
    }

    @Override
    public Response<UserInfoVo> userInfo() {
        var user = getCurrentUser();
        if (user == null) {
            return Response.responseMsg(CodeEnum.QUERY_DATA_EMPTY);
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
     * @return UserInfoVo
     */
    @Override
    public UserInfoVo getCurrentUser() {
        var token = httpServletRequestContextToken();
        if (StringUtils.hasText(token)) {
            var userId = Long.parseLong(redisUtil.getString(token + ":userId"));
            var user = userMapper.selectById(userId);
            if (user != null) {
                return UserInfoVo.builder()
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
    public String getCurrentUserName() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":userName");
    }

    @Override
    public Response<List<UserRoleVo>> userRole() {
        var userRoleVos = new ArrayList<UserRoleVo>();

        var userId = Long.parseLong(getCurrentUserId());
        var ids = userRoleRelService.queryByUserId(userId).stream()
                .map(SysUserRoleRel::getRoleId).toList();
        if(ids.isEmpty()) {
            return Response.responseMsg(CodeEnum.QUERY_DATA_EMPTY);
        }

        var roles = roleMapper.selectBatchIds(ids);
        roles.forEach(item -> {
            UserRoleVo userRoleVo = new UserRoleVo();
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
        return Response.responseMsg(CodeEnum.USER_LOGOUT);
    }
}