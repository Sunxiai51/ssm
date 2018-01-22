package com.sunveee.template.ssm.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 基础测试类
 * 
 * @author 51
 * @version $Id: BaseTest.java, v 0.1 2018年1月22日 下午3:28:32 51 Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml" })
public class BaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

}
