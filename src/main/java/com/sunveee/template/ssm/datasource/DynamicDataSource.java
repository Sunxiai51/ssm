package com.sunveee.template.ssm.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println("此时获取到的数据源为：" + DataSourceContextHolder.getDbType());
        return DataSourceContextHolder.getDbType();
    }
}
