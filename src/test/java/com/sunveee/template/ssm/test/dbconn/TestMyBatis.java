package com.sunveee.template.ssm.test.dbconn;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sunveee.template.ssm.entity.mbg.User;
import com.sunveee.template.ssm.service.UserService;
import com.sunveee.template.ssm.test.BaseTest;
import com.sunveee.template.ssm.util.LogUtil;

public class TestMyBatis extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestMyBatis.class);

    @Resource
    private UserService         userService;

    @Test
    public void test1() {
        User user = userService.getUserById(1); // 查询id为1的用户
        LogUtil.info(logger, "查询到id为1的用户,user={0}", JSON.toJSONString(user));

        List<User> userPage = userService.getUserPage(1, 2); // 按照每页两条数据查询第二页用户
        for (User _user : userPage) {
            LogUtil.info(logger, "第二页用户,user={0}", JSON.toJSONString(_user));
        }
    }
}