# ssm
这个项目是我使用SpringMVC与myBatis搭建的基本项目框架，该分支用于学习数据源的动态切换。

项目基于Spring的AOP实现了数据源的动态切换，采用*Druid*作为数据库连接池。

如果需要满足演示条件，至少需要配置两个数据源，请对照`spring-mybatis.xml`在`jdbc.properties`中进行配置。
