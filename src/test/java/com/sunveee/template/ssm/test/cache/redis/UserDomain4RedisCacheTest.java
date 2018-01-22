package com.sunveee.template.ssm.test.cache.redis;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.sunveee.template.ssm.entity.BaseDomain;

/**
 * 用于测试Redis缓存的Domain
 * 
 * @author 51
 * @version $Id: UserDomain4RedisCacheTest.java, v 0.1 2018年1月15日 下午3:38:46 51 Exp $
 */
public class UserDomain4RedisCacheTest extends BaseDomain {

    private static final long serialVersionUID = 3528245830163321308L;

    private String            userId;
    private Long              updateTime;

    public UserDomain4RedisCacheTest() {
        super();
    }

    public UserDomain4RedisCacheTest(String userId) {
        super();
        this.userId = userId;
        this.updateTime = new Date().getTime();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        UserDomain4RedisCacheTest _obj = (UserDomain4RedisCacheTest) obj;
        return StringUtils.equals(userId, _obj.getUserId()) && updateTime.equals(_obj.getUpdateTime());
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode() & this.updateTime.hashCode();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime() {
        this.updateTime = new Date().getTime();
    }

}
