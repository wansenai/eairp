package com.wansensoft.middleware.security;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wansensoft.utils.constants.SecurityConstants;
import com.wansensoft.utils.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JWTInterceptor implements HandlerInterceptor { //校验类

    private final RedisUtil redisUtil;

    public JWTInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        String requestToken = request.getHeader("Authorization");
        if(StringUtils.hasText(requestToken)){
            Claims claims = JWTUtils.checkToken(request.getHeader("Authorization"));
            if (claims != null) {
                String token = redisUtil.getString(claims.get("userName") + ":token");
                if(Boolean.TRUE.equals(redisUtil.hasKey(claims.get("userName") + ":token"))){
                    if(requestToken.equals(token)){
                        // token正确
                        return true;
                    }else {
                        // token错误，判为并发登录，挤下线
                        // 对应的修改响应头的状态，用于前端判断做出相应的策略
                        map.put("msg", "token无效签名");
                        map.put("code", "A0312");
                        log.error("用户token验证失败======>" + "无效签名");
                    }
                }else {
                    // token不存在于redis中，已过期
                    map.put("msg", "token过期");
                    map.put("code", "A0312");
                    log.error("用户token验证失败======>" + "token过期");
                }
            }
            // 解析token中的用户信息claims为null
            map.put("msg", "token无效");
            map.put("code", "A0312");
            log.error("用户token验证失败======>" + "token无效");
        }
        String value = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(value);
        return false;



//        Map<Object, Object> map = new HashMap<>();
//        String token = request.getHeader("Authorization");
//        try {
//            JWTUtils.verify(token); //验证令牌
//            Long userId = JWTUtils.getToken(token);
//            return true; //放行请求
//        } catch (SignatureVerificationException e) {
//            log.error("用户token验证失败======>" + "无效签名");
//            map.put("msg", "无效签名");
//        } catch (TokenExpiredException e) {
//            log.error("用户token验证失败======>" + "token过期");
//            map.put("msg", "token过期");
//        } catch (AlgorithmMismatchException e) {
//            log.error("用户token验证失败======>" + "token算法不一致");
//            map.put("msg", "token算法不一致");
//        } catch (Exception e) {
//            log.error("用户token验证失败======>" + "token无效");
//            map.put("msg", "token无效");
//        }
//        map.put("code", "A0312");
//        String value = new ObjectMapper().writeValueAsString(map);
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().println(value);
//        return false;
    }

}
