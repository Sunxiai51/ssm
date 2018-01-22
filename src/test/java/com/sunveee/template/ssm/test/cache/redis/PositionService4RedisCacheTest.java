package com.sunveee.template.ssm.test.cache.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import com.sunveee.template.ssm.redis.RedisUtil;
import com.sunveee.template.ssm.util.LogUtil;

/**
 * 头寸相关业务
 * 用于测试缓存的分布式锁服务
 * 
 * @author 51
 * @version $Id: PositionService4RedisCacheTest.java, v 0.1 2018年1月13日 下午4:41:00 51 Exp $
 */
@Service
public class PositionService4RedisCacheTest {

    private static final Logger           logger        = LoggerFactory.getLogger(PositionService4RedisCacheTest.class);

    /** 重试时间 */
    private static final long             RETRY_TIMEOUT = 2L;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisLockRegistry             redisLockRegistry;

    /**
     * 修改头寸
     * 
     * @param positionId 对应头寸id
     * @param change 头寸变化值
     */
    public void changePosition(String positionId, int change) {
        final Lock lock = redisLockRegistry.obtain(positionId);
        try {
            if (lock.tryLock(RETRY_TIMEOUT, TimeUnit.SECONDS)) { // 尝试加锁
                Integer oldPosition = RedisUtil.get(redisTemplate, positionId, Integer.class); // 缓存中查找头寸

                // 设置头寸开始
                if (null == oldPosition) {
                    LogUtil.info(logger, "创建头寸:id={0},初始头寸={1}", positionId, change);
                    RedisUtil.setWithDefaultExpire(redisTemplate, positionId, change);
                } else {
                    LogUtil.info(logger, "设置头寸为:{0}", oldPosition + change);
                    RedisUtil.setWithDefaultExpire(redisTemplate, positionId, oldPosition + change);
                }
                // 设置头寸结束

            } else {
                LogUtil.warn(logger, "头寸修改获取锁失败,id={0},change={1}", positionId, change);
            }
        } catch (Exception e) {
            LogUtil.error(e, logger, "头寸修改失败,id={0},change={1}", positionId, change);
        } finally {
            try {
                lock.unlock();
            } catch (IllegalStateException e) {
                LogUtil.error(e, logger, "头寸修改尝试解锁异常");
            }
        }

    }

    /**
     * 读取头寸
     * 
     * @param positionId
     * @return
     */
    public Integer getPosition(String positionId) {
        final Lock lock = redisLockRegistry.obtain(positionId);
        try {
            if (lock.tryLock(RETRY_TIMEOUT, TimeUnit.SECONDS)) { // 尝试加锁
                return RedisUtil.get(redisTemplate, positionId, Integer.class); // 缓存中查找头寸
            } else {
                LogUtil.warn(logger, "读取头寸获取锁失败,id={0}", positionId);
            }
        } catch (Exception e) {
            LogUtil.error(e, logger, "读取头寸失败,id={0}", positionId);
        } finally {
            try {
                lock.unlock();
            } catch (IllegalStateException e) {
                LogUtil.error(e, logger, "头寸修改尝试解锁异常");
            }
        }
        return null;
    }

}
