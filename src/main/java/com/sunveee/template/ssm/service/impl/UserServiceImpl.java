package com.sunveee.template.ssm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sunveee.template.ssm.dao.UserMapperExternal;
import com.sunveee.template.ssm.dao.mbg.UserMapper;
import com.sunveee.template.ssm.entity.mbg.User;
import com.sunveee.template.ssm.service.UserService;
import com.sunveee.template.ssm.util.LogUtil;
import com.sunveee.template.ssm.util.PageEntity;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper          userMapper;
    @Resource
    private UserMapperExternal  userMapperExternal;

    @Cacheable(value = "user"/*, key = "'id_'+#userId"*/)
    @Override
    public User getUserById(int userId) {

        LogUtil.info(logger, "开始查询数据库中id为{0}的用户", userId);

        User result = this.userMapper.selectByPrimaryKey(userId);

        LogUtil.info(logger, "查询到数据库中id为{0}的用户,user={1}", userId, JSON.toJSONString(result));

        return result;
    }

    @Override
    public List<User> getUserPage(int pageNo, int pageSize) {
        return this.userMapperExternal.selectUserPage(PageEntity.getInstance(pageNo, pageSize));
    }

    @Override
    public Integer getAllUserCount() {
        return this.userMapperExternal.countAll();
    }

}
