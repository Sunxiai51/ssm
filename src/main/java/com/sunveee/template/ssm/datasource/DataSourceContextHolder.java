package com.sunveee.template.ssm.datasource;

public class DataSourceContextHolder {
    public static final String               DATA_SOURCE_A       = "dataSourceA";
    public static final String               DATA_SOURCE_B       = "dataSourceB";
    public static final String               DATA_SOURCE_DEFAULT = DATA_SOURCE_A;

    private static final ThreadLocal<String> contextHolder       = new ThreadLocal<String>();

    /**
     * 设置数据源
     * 
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 获取数据源
     * 
     * @return
     */
    public static String getDataSource() {
        return contextHolder.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

}
