# ssm

使用SpringMVC和MyBatis搭建的java项目框架，用于学习与快速构建新项目。

包含内容：
- Spring集成mvc
- Spring集成MyBatis
- mybatis generator
- Redis客户端配置与缓存实现

## 部署与运行步骤

1. 新建数据库、数据库表，更改数据库连接信息

    项目所配置的数据库类型为*mysql*，新建一个表`user`，其对应的sql如下：
    ```sql
    CREATE TABLE `user` (
      `id` int(11) NOT NULL,
      `name` varchar(255) NOT NULL,
      `password` varchar(255) NOT NULL,
      `salt` varchar(255) NOT NULL,
      `status` int(11) NOT NULL,
      `type` int(11) NOT NULL,
      PRIMARY KEY (`id`)
    )
    ```
    可以根据需要随意添加示例数据，配置好数据库后需要修改`jdbc.properties`中的数据库连接信息。

2. Run as --> Maven build... --> tomcat7:run

## MBG(mybatis generator)配置说明

参见：[MBG配置说明](https://github.com/Sunxiai51/ssm/blob/master/MBG.md)

## Redis集成与配置

参见：[Redis集成与配置](https://github.com/Sunxiai51/ssm/blob/master/Redis.md)