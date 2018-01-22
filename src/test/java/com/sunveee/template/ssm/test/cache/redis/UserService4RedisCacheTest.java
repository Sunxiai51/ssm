package com.sunveee.template.ssm.test.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sunveee.template.ssm.util.LogUtil;

/**
 * User相关业务
 * 用于测试Redis缓存，展示了使用缓存的配置示例
 * 
 * @author 51
 * @version $Id: UserService4RedisCacheTest.java, v 0.1 2018年1月11日 上午10:47:52 51 Exp $
 */
@Service
public class UserService4RedisCacheTest {

    private static final Logger logger = LoggerFactory.getLogger(UserService4RedisCacheTest.class);

    /**
     * 根据ID查询用户
     * 被<code>@Cacheable</code>注解修饰的方法，被调用前会先进入缓存查找，如果找到，则不会执行该方法，直接返回缓存中的数据
     * 
     * @param userId
     * @return
     */
    @Cacheable(value = "userCache", key = "#userId")
    public UserDomain4RedisCacheTest getUserByID(String userId) {
        LogUtil.info(logger, "查询用户，id={0}", userId);
        return geneUserById(userId);
    }

    /**
     * 根据ID更新用户
     * 被<code>@CachePut</code>注解修饰的方法总是会被执行，执行完成后，会将该方法的返回值放入缓存
     * 
     * @param userId
     * @return
     */
    @CachePut(value = "userCache", key = "#userId")
    public UserDomain4RedisCacheTest updateUserByID(String userId) {
        LogUtil.info(logger, "更新用户，id={0}", userId);
        UserDomain4RedisCacheTest result = geneUserById(userId);
        result.setUpdateTime();
        return result;
    }

    /**
     * 根据ID删除用户
     * 被<code>@CachePut</code>注解修饰的方法，执行时会触发缓存的清除操作，默认是在正常返回后触发
     * 
     * @param userId
     * @return
     */
    @CacheEvict(value = "userCache", key = "#userId")
    public int deleteUserByID(String userId) {
        LogUtil.info(logger, "删除用户，id={0}", userId);
        return 1;
    }

    /**
     * 创建用户实体，模拟数据库查询过程
     * 
     * @param userId
     * @return
     */
    private UserDomain4RedisCacheTest geneUserById(String userId) {
        return new UserDomain4RedisCacheTest(userId);
    }

}
