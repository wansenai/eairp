package com.wansenai.service;

import com.wansenai.service.user.ISysUserService;
import com.wansenai.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
@Slf4j
public class BaseService {

    private final RedisUtil redisUtil;

    private final ISysUserService userService;

    public BaseService(RedisUtil redisUtil, ISysUserService userService) {
        this.redisUtil = redisUtil;
        this.userService = userService;
    }

    public Long getCurrentUserId() {
        var token = httpServletRequestContextToken();
        return Long.parseLong(redisUtil.getString(token + ":userId"));
    }

    public Long getCurrentTenantId() {
        var token = httpServletRequestContextToken();
        return Long.parseLong(redisUtil.getString(token + ":tenantId"));
    }

    public String getCurrentUserName() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":userName");
    }

    public String getCurrentUserAccount() {
        var token = httpServletRequestContextToken();
        return redisUtil.getString(token + ":userAccount");
    }

    public String getCurrentUserSystemLanguage() {
        return userService.getUserSystemLanguage(getCurrentUserId());
    }

    private String httpServletRequestContextToken() {
        var sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra == null) {
            log.error("[异常]获取HttpServletRequest为空");
        }
        return Optional.ofNullable(sra.getRequest().getHeader("Authorization")).orElseThrow(null);
    }
}
