package com.wansensoft.middleware.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wansensoft.entities.SysUser;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//封装工具类
@Component
public class JWTUtils {

    /** 有效期为一天 **/
    private static long time = 1000*60*60*24;

    private static String SECRET ="token";

    /**
     * 生成token
     *
     */
    public static String createToken(String userName) {
        // 设置JWT头部
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // 创建token
        JwtBuilder builder = Jwts.builder();

        // 添加头部，可省略保持默认，默认即map中的键值对
        return builder.setHeader(map)
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .claim("userName", userName)
                .setSubject(String.valueOf(userName))
                // 设置签名解码算法
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 验证token
     * @param token
     */
    public static Claims checkToken(String token){
        if(token == null){
            return null;
        }
        JwtParser parser = Jwts.parser();
        try {
            Jws<Claims> claimsJws = parser.setSigningKey(SECRET).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            // 如果解析token正常，返回claims
            return claims;
        }catch (Exception e) {
            // 如果解析token抛出异常，返回null
            return null;
        }
    }

    /**
     * 通过token获取用户的id
     * @param token
     * @return
     */
    public static Long getToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);
        String jwtSubject = jwt.getSubject();
        return Long.valueOf(jwtSubject);
    }

}
