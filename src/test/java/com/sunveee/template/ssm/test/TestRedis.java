package com.sunveee.template.ssm.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.sunveee.template.ssm.entity.mbg.User;
import com.sunveee.template.ssm.service.UserService;
import com.sunveee.template.ssm.util.LogUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
public class TestRedis {
    private static final Logger             logger = LoggerFactory.getLogger(TestRedis.class);

    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService                     userService;

    @Test
    public void testInitRedis() {
        System.out.println(redisTemplate == null);
    }

    @Test
    public void test1() {
        User user = userService.getUserById(1); // 查询id为1的用户
        LogUtil.info(logger, "查询到id为1的用户,user={0}", JSON.toJSONString(user));

    }
}