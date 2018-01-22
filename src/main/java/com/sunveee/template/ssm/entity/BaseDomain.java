package com.sunveee.template.ssm.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基础基类
 * 
 * @author 51
 * @version $Id: BaseDomain.java, v 0.1 2018年1月22日 下午3:26:43 51 Exp $
 */
public class BaseDomain implements Serializable {

    /**  */
    private static final long serialVersionUID = 3883508838073222232L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
