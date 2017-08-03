package com.sunveee.template.ssm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sunveee.template.ssm.dao.UserDao;
import com.sunveee.template.ssm.datasource.DataSourceContextHolder;
import com.sunveee.template.ssm.datasource.DynamicDataSourceAnnotation;
import com.sunveee.template.ssm.model.User;
import com.sunveee.template.ssm.service.UserService;
import com.sunveee.template.ssm.util.PageEntity;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    public User getUserById(int userId) {
        return this.userDao.selectById(userId);
    }

    @DynamicDataSourceAnnotation(dataSource = DataSourceContextHolder.DATA_SOURCE_B)
    public User getUserById_dataSourceB(int userId) {
        return this.userDao.selectById(userId);
    }

    public List<User> getUserPage(int pageNo, int pageSize) {
        return this.userDao.selectUserPage(new PageEntity(pageNo, pageSize));
    }

    public Integer getAllUserCount() {
        return this.userDao.countAll();
    }

    @DynamicDataSourceAnnotation(dataSource = DataSourceContextHolder.DATA_SOURCE_B)
    public List<User> getUserPage_dataSourceB(int pageNo, int pageSize) {
        return this.userDao.selectUserPage(new PageEntity(pageNo, pageSize));
    }

    @DynamicDataSourceAnnotation(dataSource = DataSourceContextHolder.DATA_SOURCE_B)
    public Integer getAllUserCount_dataSourceB() {
        return this.userDao.countAll();
    }

}
