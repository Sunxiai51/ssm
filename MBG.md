# MBG(mybatis generator)配置说明

项目中使用MBG生成对应的实体类、dao接口与映射文件，数据库中添加了新的表后能够快速生成对应的文件，省时且不易出错。

参考配置文件：[generatorConfig.xml](https://github.com/Sunxiai51/ssm/blob/master/src/main/resources/generatorConfig.xml)

但MBG只支持生成基本类型的SQL，很多时候我们需要根据业务添加SQL代码，如果直接添加到MBG生成的文件中，在下次运行MBG时有被覆盖的风险（虽然可以在每次运行前进行细致的配置以避免overwrite，但未免也太麻烦了）。

所以，项目中将自行编写的内容与MBG生成的内容分开存放，所有由MBG生成的文件均存放在名为`mbg`的文件夹中，例如：
- [com.sunveee.template.ssm.dao.mbg.UserMapper.java](https://github.com/Sunxiai51/ssm/blob/master/src/main/java/com/sunveee/template/ssm/dao/mbg/UserMapper.java)
- [com.sunveee.template.ssm.dao.UserMapperExternal.java](https://github.com/Sunxiai51/ssm/blob/master/src/main/java/com/sunveee/template/ssm/dao/UserMapperExternal.java)

`UserMapper.java`为MBG生成的映射接口文件，`UserMapperExternal.java`为根据功能需求编写的扩展映射接口文件。

很显然，会存在两个对应的mapper.xml文件与之对应，它们分别是：
- [/src/main/resources/mapping/mbg/UserMapper.xml](https://github.com/Sunxiai51/ssm/blob/master/src/main/resources/mapping/mbg/UserMapper.xml)
- [/src/main/resources/mapping/UserMapperExternal.xml](https://github.com/Sunxiai51/ssm/blob/master/src/main/resources/mapping/UserMapperExternal.xml)

其中`UserMapper.xml`由MBG生成，`UserMapperExternal.xml`由自己编写。

**特别注意：**
在编写`xxxMapperExternal.xml`时可以将`xxxMapper.xml`中的`<mapper>`元素等内容copy过来，但是需要注意修改其属性`namespace="com.sunveee.template.ssm.dao.xxxMapperExternal"`以进行正确的映射。

在service中使用dao层服务时，只要引入两个对应的mapper即可，如下：
```java
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper          userMapper;
    
    @Resource
    private UserMapperExternal  userMapperExternal;

    public User getUserById(int userId) {
       return this.userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<User> getUserPage(int pageNo, int pageSize) {
        return this.userMapperExternal.selectUserPage(PageEntity.getInstance(pageNo, pageSize));
    }

    @Override
    public Integer getAllUserCount() {
        return this.userMapperExternal.countAll();
    }

}
```
