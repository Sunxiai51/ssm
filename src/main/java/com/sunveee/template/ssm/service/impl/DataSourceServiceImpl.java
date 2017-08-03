package com.sunveee.template.ssm.service.impl;

import org.springframework.stereotype.Service;

import com.sunveee.template.ssm.datasource.DataSourceContextHolder;
import com.sunveee.template.ssm.datasource.DynamicDataSourceAnnotation;
import com.sunveee.template.ssm.service.DataSourceService;

@Service
@DynamicDataSourceAnnotation
public class DataSourceServiceImpl implements DataSourceService {

    @DynamicDataSourceAnnotation(dataSource = DataSourceContextHolder.DATA_SOURCE_B)
    public void changeDataSource() {
        System.out.println("切换数据源serviceImple");
    }
}
