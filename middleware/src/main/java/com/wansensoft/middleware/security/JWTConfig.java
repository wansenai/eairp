package com.wansensoft.middleware.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JWTConfig implements WebMvcConfigurer {

    private final JWTInterceptor interceptor;

    public JWTConfig(JWTInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                // 路径不进行拦截
                .excludePathPatterns("/v2/common/captcha")
                .excludePathPatterns("/user/v2/user");
    }
}