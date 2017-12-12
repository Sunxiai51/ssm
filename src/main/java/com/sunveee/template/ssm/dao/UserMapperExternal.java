package com.sunveee.template.ssm.dao;

import java.util.List;

import com.sunveee.template.ssm.entity.mbg.User;
import com.sunveee.template.ssm.util.PageEntity;

public interface UserMapperExternal {

    public List<User> selectUserPage(PageEntity pageEntity);

    public Integer countAll();

}
