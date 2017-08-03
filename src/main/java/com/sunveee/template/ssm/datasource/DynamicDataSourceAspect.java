package com.sunveee.template.ssm.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order
public class DynamicDataSourceAspect {
    @Pointcut("@annotation(com.sunveee.template.ssm.datasource.DynamicDataSourceAnnotation)")
    public void applyDataSource() {
    }

    @Before("applyDataSource()")
    public void testBefore(JoinPoint point) {
        Class<?> className = point.getTarget().getClass();
        DynamicDataSourceAnnotation dataSourceAnnotation = className.getAnnotation(DynamicDataSourceAnnotation.class);
        // 默认配置
        String dataSource = DataSourceContextHolder.DATA_SOURCE_DEFAULT;
        if (dataSourceAnnotation != null) {
            // 如果类配置了数据源，默认使用类的数据源配置，然后继续扫描具体方法
            dataSource = dataSourceAnnotation.dataSource();
        }
        String methodName = point.getSignature().getName();
        Class<?>[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        try {
            Method method = className.getMethod(methodName, argClass);
            // 如果方法使用注解配置了数据源，将覆盖掉类的配置
            if (method.isAnnotationPresent(DynamicDataSourceAnnotation.class)) {
                DynamicDataSourceAnnotation annotation = method.getAnnotation(DynamicDataSourceAnnotation.class);
                dataSource = annotation.dataSource();
            }
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DataSourceContextHolder.setDataSource(dataSource);
    }

    @After("applyDataSource()")
    public void testAfter(JoinPoint point) {
        Class<?> className = point.getTarget().getClass(); // 当前访问的class
        String methodName = point.getSignature().getName(); // 当前访问的方法名
        Class<?>[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes(); // 方法的参数的类型
        String dataSource = DataSourceContextHolder.DATA_SOURCE_DEFAULT;
        try {
            Method method = className.getMethod(methodName, argClass);
            if (method.isAnnotationPresent(DynamicDataSourceAnnotation.class)) {
                DynamicDataSourceAnnotation annotation = method.getAnnotation(DynamicDataSourceAnnotation.class);
                dataSource = annotation.dataSource();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (dataSource != null && !DataSourceContextHolder.DATA_SOURCE_DEFAULT.equals(dataSource))
            DataSourceContextHolder.clearDataSource(); // 如果不是默认的数据源，则在使用后清除其值
    }
}
