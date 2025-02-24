package com.aiaq.common;

import com.aiaq.utils.JsonUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/10/12 14:32
 * @Description 使用jwt生成token
 */
@Slf4j
@Component
public class UserSessionHelper {

    @Component
    @Data
    @ConfigurationProperties("aiaq.jwt")
    public static class JwtProperties {
        /**
         * 签发人
         */
        private String issuer;

        /**
         * 密钥
         */
        private String secret;

        /**
         * 有效期，毫秒时间戳
         */
        private Long expire;
    }

    private final JwtProperties jwtProperties;

    private Algorithm algorithm;

    private JWTVerifier verifier;

    public UserSessionHelper(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        verifier = JWT.require(algorithm).withIssuer(jwtProperties.getIssuer()).build();
    }

    /**
     * 生成会话
     *
     * @param userId
     * @return
     */
    public String genSession(Long userId) {
        // 1.生成jwt格式的会话，内部持有有效期，用户信息
        String session = "{\"u\":" + userId + "}";
        String token = JWT.create().withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpire()))
                .withPayload(session)
                .sign(algorithm);
        // 2.将token缓存到redis
        RedisClient.setStrWithExpire(token, String.valueOf(userId), jwtProperties.getExpire() / 1000);
        return token;
    }

    public void removeSession(String session) {
        RedisClient.del(session);
    }

    /**
     * 根据会话获取用户信息
     *
     * @param session
     * @return
     */
    public Long getUserIdBySession(String session) {
        // jwt的校验方式，如果token非法或者过期，则直接验签失败
        try {
            DecodedJWT decodedJWT = verifier.verify(session);
            String pay = new String(Base64Utils.decodeFromString(decodedJWT.getPayload()));
            // jwt验证通过，获取对应的userId
            String userId = String.valueOf(JsonUtil.toObj(pay, HashMap.class).get("u"));

            // 从redis中获取userId，解决用户登出，后台失效jwt token的问题
            String user = RedisClient.getStr(session);
            if (user == null || !Objects.equals(userId, user)) {
                return null;
            }
            return Long.valueOf(user);
        } catch (Exception e) {
            log.info("jwt token校验失败! token: {}, msg: {}", session, e.getMessage());
            return null;
        }
    }
}
