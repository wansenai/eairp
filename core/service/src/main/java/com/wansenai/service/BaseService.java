/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wansenai.service;

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

    public BaseService(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
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

    private String httpServletRequestContextToken() {
        var sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra == null) {
            log.error("[异常]获取HttpServletRequest为空");
        }
        return Optional.ofNullable(sra.getRequest().getHeader("Authorization")).orElseThrow(null);
    }
}
