package com.sunveee.template.ssm.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DynamicDataSourceAnnotation {
    //dataSource 自定义注解的参数
    String dataSource() default DataSourceContextHolder.DATA_SOURCE_A;
}
