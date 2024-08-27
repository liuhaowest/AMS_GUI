package com.student.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;

/**
 * @author 李123
 * Jwt工具类
 */
@Service
public class JwtUtils {
    // 过期时间5分钟
    //private static final long EXPIRE_TIME = 60 * 1000;

    private static final String SING = "A7k3vjFN1g1LafYVMNaeE0ifQLMxYqAzxBNT#kLUujEzbjzW";

    // 创建Token
    public static String getToken(Map<String, String> map) {
        //设置过期时间 , 过期时间为7天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 3);
        //创建 Builder
        JWTCreator.Builder builder = JWT.create();
        //遍历map,设置token参数
        map.forEach(builder::withClaim);

        return builder
                //设置过期时间
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SING));
    }

    // 验证token是否合法
    public static Boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 获取token中的某一个数据
    public static String getString(String token, String search) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim(search).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    // 获取token信息
    public static DecodedJWT getTokenInfo(String token) {
        //获取 token 得 DecodedJWT
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
}

