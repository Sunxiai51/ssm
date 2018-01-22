package com.sunveee.template.ssm.redis;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存工具,使用Redis对数据进行缓存、读取等操作
 * 
 * @author 51
 * @version $Id: RedisUtil.java, v 0.1 2018年1月22日 下午2:45:51 51 Exp $
 */
public class RedisUtil {

    /** 默认缓存失效时间 */
    private static final long DEFAULT_EXPIRE_TIME = 10L;

    /**
     * 保存数据至Redis<br>
     * 数据将按照unit时间单位保持timeout时长
     * 
     * @param key     
     * @param value
     * @param timeout  保存时长
     * @param unit     时间单位{@link TimeUnit}
     */
    public static void set(RedisTemplate<String, Object> redisTemplate, String key, Object value, Long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 以默认过期时间保存数据至Redis
     * 
     * @param key
     * @param value
     */
    public static void setWithDefaultExpire(RedisTemplate<String, Object> redisTemplate, String key, Object value) {
        set(redisTemplate, key, value, DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    /**
     * 通过key从Redis中取缓存数据
     * 
     * @param key
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(RedisTemplate<String, Object> redisTemplate, String key, Class<T> clazz) {
        return (T) redisTemplate.boundValueOps(key).get();
    }

    /**
     * 通过key从Redis中取缓存数据
     * 
     * @param key
     * @return
     */
    public static Object get(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * 根据key从Redis中删除缓存数据
     * 
     * @param key
     */
    public static void delete(RedisTemplate<String, Object> redisTemplate, String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    /**
     * 根据key判断数据是否存在Redis中
     * 
     * @param key
     * @return
     */
    public static boolean exists(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.hasKey(key);
    }

}
