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
package com.wansenai.api.config;

import com.wansenai.api.RateLimitException;
import com.wansenai.utils.IpUtils;
import com.wansenai.utils.enums.BaseCodeEnum;
import com.wansenai.utils.enums.LimitType;
import com.wansenai.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Component
@Aspect
@Slf4j
public class RateLimiterAspect {

    private final RedisUtil redisUtil;

    public RateLimiterAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        String key = rateLimiter.key();
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        List<String> keys = Collections.singletonList(combineKey);
        try {
            if (redisUtil.get(keys.get(0)) != null) {
                long number = redisUtil.incr(keys.get(0), 1);
                if ((int) number > count) {
                    throw new RateLimitException(BaseCodeEnum.FREQUENT_SYSTEM_ACCESS.getCode(), BaseCodeEnum.FREQUENT_SYSTEM_ACCESS.getMsg());
                }
                log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, (int) number, keys.get(0));
            } else {
                redisUtil.set(keys.get(0), 1, time);
            }
        } catch (Exception e) {
            throw new RateLimitException(BaseCodeEnum.SYSTEM_BUSY.getCode(), BaseCodeEnum.SYSTEM_BUSY.getMsg());
        }
    }

    /**
     * 获取ip为key
     * @param rateLimiter
     * @param point
     * @return
     */
    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuilder stringBuffer = new StringBuilder(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(
                            IpUtils.getIpAddr(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                                    .getRequest()))
                    .append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
