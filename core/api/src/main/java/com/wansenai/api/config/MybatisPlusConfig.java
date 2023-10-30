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
package com.wansenai.api.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wansenai.utils.redis.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@MapperScan("com.wansenai.mappers")
public class MybatisPlusConfig {

    public final RedisUtil redisUtil;

    public MybatisPlusConfig(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }


    /**
     * 根据token截取租户id
     * @param token
     * @return
     */
    public Long getTenantIdByToken(String token) {
        long tenantId = -1L;
        if(StringUtils.hasText(token)) {
            tenantId = Long.parseLong(redisUtil.getString(token + ":tenantId"));
        }
        return tenantId;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(HttpServletRequest request) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String token = request.getHeader("Authorization");
                Long tenantId = getTenantIdByToken(token);
                if (tenantId!=0L) {
                    return new LongValue(tenantId);
                } else {
                    //超管
                    return new LongValue(0);
                }
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                return "sys_user".equalsIgnoreCase(tableName) || "sys_menu".equalsIgnoreCase(tableName)
                        || "sys_user_role_rel".equalsIgnoreCase(tableName) || "sys_user_dept_rel".equalsIgnoreCase(tableName)
                        || "sys_role_menu_rel".equalsIgnoreCase(tableName) || "sys_platform_config".equalsIgnoreCase(tableName);
            }
        }));

        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
