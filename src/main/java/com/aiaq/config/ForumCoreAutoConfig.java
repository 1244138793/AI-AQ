package com.aiaq.config;


import com.aiaq.common.RedisClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 最紧要开心
 */
@Configuration
@ComponentScan(basePackages = "com.aiaq")
public class ForumCoreAutoConfig {

    public ForumCoreAutoConfig(RedisTemplate<String, String> redisTemplate) {
        RedisClient.register(redisTemplate);
    }
}
