package com.sunveee.template.ssm.test.cache.redis;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sunveee.template.ssm.redis.RedisCacheConfig;
import com.sunveee.template.ssm.redis.RedisUtil;
import com.sunveee.template.ssm.test.BaseTest;

/**
 * Redis缓存测试类
 * 
 * @author 51
 * @version $Id: RedisCacheTest.java, v 0.1 2018年1月11日 上午10:23:21 51 Exp $
 */
public class RedisCacheTest extends BaseTest {

    @Resource
    private RedisCacheConfig               redisCacheConfig;

    @Resource
    private UserService4RedisCacheTest     userService4RedisCacheTest;

    @Resource
    private PositionService4RedisCacheTest positionService4RedisCacheTest;

    /**
     * 用于测试缓存的用户Id
     */
    private final String                   userId     = "user001";
    /**
     * 用于测试分布式锁的头寸Id
     */
    private final String                   positionId = "position_01";

    /**
     * 测试缓存前确保bean的注入与相关值清空
     */
    @Before
    public void beforeTestCache() {
        Assert.assertNotNull(redisCacheConfig);
        RedisUtil.delete(redisCacheConfig.getRedisTemplate(), userId, positionId);
    }

    /**
     * 测试缓存服务
     * <p>依次执行UserService4RedisCacheTest中的查询、更新和删除，然后取缓存中的值进行比较</p>
     */
    @Test
    public void testRedisCache() {
        Assert.assertNull(RedisUtil.get(redisCacheConfig.getRedisTemplate(), userId)); // 初始缓存中对应用户数据为空

        UserDomain4RedisCacheTest user = userService4RedisCacheTest.getUserByID(userId); // 查询后对应用户数据放入缓存
        Assert.assertNotNull(RedisUtil.get(redisCacheConfig.getRedisTemplate(), userId));

        UserDomain4RedisCacheTest userNew = userService4RedisCacheTest.updateUserByID(userId); // 更新后缓存中数据也被更新
        Assert.assertEquals(userNew, RedisUtil.get(redisCacheConfig.getRedisTemplate(), userId));
        Assert.assertNotEquals(user, RedisUtil.get(redisCacheConfig.getRedisTemplate(), userId));

        userService4RedisCacheTest.deleteUserByID(userId); // 删除后缓存中数据也被删除
        Assert.assertNull(RedisUtil.get(redisCacheConfig.getRedisTemplate(), userId));
    }

    /**
     * 测试分布式锁服务
     * <p>每隔0.5秒创建并启动一个线程，共启动10个线程；</p>
     * <p>对于每个线程：每隔0.5秒对头寸增加1，共增加5次；</p>
     * <p>主线程创建完最后一个线程后，阻塞10秒以等待其余线程执行完毕，然后从缓存中取出头寸值，预期头寸值为50。</p>
     * 
     * @throws InterruptedException
     */
    @Test
    public void testChangePosition() throws InterruptedException {
        Assert.assertNull(RedisUtil.get(redisCacheConfig.getRedisTemplate(), positionId)); // 初始缓存中对应头寸数据为空
        for (int i = 0; i < 10; i++) {
            // 每隔0.5秒新建并启动一个线程
            new Thread("thread_" + i) {
                public void run() {
                    // 每个线程每隔0.5秒头寸+1，执行5次
                    for (int i = 0; i < 5; i++) {
                        positionService4RedisCacheTest.changePosition(positionId, 1);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            Thread.sleep(500);
        }

        try {
            Thread.sleep(10 * 1000); // 确保上述线程执行结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(Integer.valueOf(50), positionService4RedisCacheTest.getPosition(positionId));

    }

}
