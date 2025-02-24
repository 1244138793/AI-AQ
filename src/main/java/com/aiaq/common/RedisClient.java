package com.aiaq.common;


import com.aiaq.utils.JsonUtil;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/10/13 9:40
 * @Description redis客户端
 */
public class RedisClient {

    private static final Charset CODE = StandardCharsets.UTF_8;

    private static final String KEY_PREFIX = "codingroom_";

    private static RedisTemplate<String, String> template;

    public static void register(RedisTemplate<String, String> template) {
        RedisClient.template = template;
    }

    public static void nullCheck(Object... args) {
        for (Object obj : args) {
            if (obj == null) {
                throw new IllegalArgumentException("redis argument can not be null!");
            }
        }
    }

    /**
     * 缓存值序列化处理
     *
     * @param val
     * @return
     * @param <T>
     */
    public static <T> byte[] valBytes(T val) {
        if (val instanceof String) {
            return ((String) val).getBytes(CODE);
        } else {
            return JsonUtil.toStr(val).getBytes(CODE);
        }
    }

    /**
     * 生成缓存key
     * @param key
     * @return
     */
    public static byte[] keyBytes(String key) {
        nullCheck(key);
        key = KEY_PREFIX + key;
        return key.getBytes(CODE);
    }

    public static byte[][] keyBytes(List<String> keys) {
        byte[][] bytes = new byte[keys.size()][];
        int index = 0;
        for (String key : keys) {
            bytes[index++] = keyBytes(key);
        }
        return bytes;
    }

    /**
     * 获取key的有效期
     *
     * @param key
     * @return
     */
    public static Long ttl(String key) {
        return template.execute((RedisCallback<Long>) con -> con.ttl(keyBytes(key)));
    }

    /**
     * 查询缓存
     *
     * @param key
     * @return
     */
    public static String getStr(String key) {
        return template.execute((RedisCallback<String>) con -> {
            byte[] val = con.get(keyBytes(key));
            return val == null ? null : new String(val);
        });
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void setStr(String key, String value) {
        template.execute((RedisCallback<Void>) con -> {
            con.set(keyBytes(key), valBytes(value));
            return null;
        });
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void del(String key) {
        template.execute((RedisCallback<Long>) con -> con.del(keyBytes(key)));
    }

    /**
     * 设置缓存有效期
     */
    public static void expire(String key, Long expire) {
        template.execute((RedisCallback<Void>) con -> {
            con.expire(keyBytes(key), expire);
            return null;
        });
    }

    /**
     * 带过期时间的缓存写入
     *
     * @param key
     * @param value
     * @param expire 过期时间，单位秒
     * @return
     */
    public static Boolean setStrWithExpire(String key, String value, Long expire) {
        return template.execute((RedisCallback<Boolean>) con -> con.setEx(keyBytes(key), expire, valBytes(value)));
    }

}
