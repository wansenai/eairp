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
package com.wansenai.middleware.email;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wansenai.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmailToken {
    // 3 分钟
    public static final long EXPIRE_TIME = 3 * 60 * 1000;

    // token_secret
    public final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";

    private final RedisUtil redisUtil;

    public EmailToken(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 根据邮件地址和用户名创建token
     * 邮件地址为验签
     * HMAC256算法
     *
     * @param email
     * @param name
     * @return
     */
    public String createToke(String email, String name) {
        try {
            // 过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(email);
            // 设置头部信息
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("typ", "JWT");
            map.put("alg", "HS256");
            String token = JWT.create()
                    .withHeader(map)
                    .withClaim("username", name)
                    .withClaim("loginEmail", email)
                    .withExpiresAt(date)
                    .sign(algorithm);
            // 这里截取返回最后一段token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(email)).build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("Email生成的第一次：" + token);
            // 保存redis
            redisUtil.set("payload", jwt.getPayload());
            return jwt.getSignature();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token和邮件地址进行解密
     *
     * @param token
     * @param email
     * @return
     */
    public boolean checkToken(String token, String email) {
        // 根据校验规则HMAC256生成校验对象
        try {
            // 组装完整token,从redis获取payload的值
            String payload = redisUtil.getString("payload");
            log.info("Email: Redis获取的payload" + payload);
            String FullToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" + "." + payload + "." + token;
            log.info("Email: token:" + token);
            log.info(FullToken);
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(email)).build();
            verifier.verify(FullToken);
            return true;
        } catch (TokenExpiredException e) {
            log.error("Email: Token已经过期:" + e.getMessage());
            return false;
        } catch (SignatureVerificationException e) {
            log.error("Email: Token不合法:" + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Email: Token认证失败:" + e.getMessage());
            return false;
        }
    }
}
