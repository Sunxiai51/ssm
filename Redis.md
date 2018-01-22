# Redis缓存集成、配置与使用

本文档描述了**Redis缓存**在项目中的集成与配置过程，并以示例说明其在实际项目中的应用场景。

## 1. 相关依赖

Reids缓存中间件直接依赖的jar包括：

- org.springframework.data: **spring-data-redis** (1.8.9.RELEASE)
- org.springframework.integration: **spring-integration-redis** (4.3.13.RELEASE)
- redis.clients: **jedis** (2.9.0)
- com.fasterxml.jackson.core: **jackson-databind** (2.9.3)

## 2. 相关知识点说明

- spring集成redis
- redis常用操作封装
- redis缓存
- 分布式锁

### 2.1 Spring集成redis-replication

本项目使用的redis中间件采用replication形式部署并提供服务。Replication为redis主从复制(master-slave)模式，对于客户端来说仅仅需要配置到master的连接即可，配置好的slave会自动完成备份。

> redis-replication相关文档： [Replication - Redis](https://redis.io/topics/replication)

本项目使用spring-data-redis来集成redis，并使用spring-integration-redis来提供分布式环境下的锁服务，后文将集成的主要配置列出，插件详细特性及更多配置可参考以下文档：

> spring-data-redis文档：[Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/1.8.9.RELEASE/reference/html/)
>
> spring注解配置缓存文档：[Cache Abstraction](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cache)
>
> spring-integration-redis文档：[Redis Support](https://docs.spring.io/spring-integration/reference/html/redis.html)

Spring集成redis需要配置相应的bean，例如 `org.springframework.data.redis.core.RedisTemplate` ，需要配置的bean包括：

- redis.clients.jedis.**JedisPoolConfig**
- org.springframework.data.redis.connection.jedis.**JedisConnectionFactory**
- org.springframework.data.redis.core.**RedisTemplate**
- org.springframework.data.redis.cache.**RedisCacheManager**
- org.springframework.integration.redis.util.**RedisLockRegistry**
- com.sunveee.template.ssm.redis.**RedisCacheConfig**

其中唯一的自定义bean类 `RedisCacheConfig` 继承于 `org.springframework.cache.annotation.CachingConfigurerSupport` ，用于向spring提供CacheManager与KeyGenerator实例。

如果需要使用 `@Cacheable` 等注解来自动缓存内容，在该配置文件中还需要配置 `<cache:annotation-driven />` 以开启缓存注解驱动，该配置项缺省使用了一个bean名为 `cacheManager` 的缓存管理器。

### 2.2 Redis常用操作封装

`RedisUtil` 封装了基于RedisTemplate的一些对redis服务器的基本操作，例如存缓存、取缓存、删除缓存、查看是否存在于缓存中等，调用时需要注意分布式环境下的一致性问题。

### 2.3 缓存服务和分布式锁服务 

客户端使用redis中间件，需要在客户端的spring配置中引入 `spring-redis-replication.xml` 及其对应的 `redis-replication.properties` ：

```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations">
        <list>
          ...
          <value>classpath:redis/redis-replication.properties</value>
          ...
        </list>
    </property>
</bean>

<import resource="redis/spring-redis-replication.xml" />
```

测试中模拟了使用缓存中间件的多种场景，包括：

- 查询缓存：

  com.sunveee.template.ssm.test.cache.redis.UserService4RedisCacheTest.**getUserByID**(String userId)

- 更新缓存：

  com.sunveee.template.ssm.test.cache.redis.UserService4RedisCacheTest.**updateUserByID**(String userId)

- 删除缓存：

  com.sunveee.template.ssm.test.cache.redis.UserService4RedisCacheTest.**deleteUserByID**(String userId)

- 分布式环境下访问缓存：

  com.sunveee.template.ssm.test.cache.redis.PositionService4RedisCacheTest.**changePosition**(String positionId, int change)

#### 2.3.1 缓存服务场景 - 查询

每次执行查询操作时，先查询缓存，如果缓存中存在该条记录则直接作为查询结果返回（不执行方法），如果缓存中不存在该条记录则执行方法获取查询结果返回，并将结果存入缓存。

该场景可使用 `@Cacheable` 配置，示例如下：

```java
@Cacheable(value = "userCache", key = "#userId")
public UserDomain4RedisCacheTest getUserByID(String userId) {
  LogUtil.info(logger, "查询用户，id={0}", userId);
  return geneUserById(userId);
}
```

`@Cacheable` 可以标记在一个方法上，也可以标记在一个类上。标记在一个方法上时表示该方法支持缓存，标记在一个类上时则表示该类所有的方法都支持缓存。

`@Cacheable` 常用的有三个属性：value、key、condition。

- **value**：cache名称，必填，可以指定一至多个cache。

  ```java
  @Cacheable(value = "cache1") // 一个cache

  @Cacheable(value = {"cache1", "cache2"}) // 多个cache
  ```

- **key**：指定缓存的key，该属性支持SpringEL表达式。未指定该属性时，将使用默认策略生成key，默认策略定义于 `com.sunveee.template.ssm.redis.RedisCacheConfig.keyGenerator()` 中。

  ```java
  @Cacheable(value="userCache", key="#id")
  public User find(Integer id)

  @Cacheable(value="userCache", key="#p0")
  public User find(Integer id)

  @Cacheable(value="userCache", key="#user.id")
  public User find(User user)

  @Cacheable(value="userCache", key="#p0.id")
  public User find(User user)
  ```

- **condition**：指定发生的条件，默认为空，表示将缓存所有的调用情形。其值通过SpringEL表达式指定，当为 `true` 时表示进行缓存处理，当为 `false` 时表示不进行缓存处理。如下示例表示，只有当user的name长度小于32时才会进行缓存：

  ```java
  @Cacheable(value = "userCache", key="#user.id", condition="#user.name.length() < 32")
  public User find(User user)
  ```

有关于 `@Cacheable` 的更多配置项及说明可以通过2.1中的参考文档了解。

#### 2.3.2 缓存服务场景 - 更新

每次执行更新操作时，执行更新操作后，更新结果将存入缓存（覆盖之前可能存在的值）。

该场景使用 `@CachePut` 配置，示例如下：

```java
@CachePut(value = "userCache", key = "#userId")
public UserDomain4RedisCacheTest updateUserByID(String userId) {
  LogUtil.info(logger, "更新用户，id={0}", userId);
  UserDomain4RedisCacheTest result = geneUserById(userId);
  result.setUpdateTime();
  return result;
}
```

`@CachePut` 配置项与 `@Cacheable` 基本相同。

#### 2.3.3 缓存服务场景 - 删除

每次执行删除操作时，将对应的缓存也删除。

该场景使用 `@CacheEvict` 配置，示例如下：

```java
@CacheEvict(value = "userCache", key = "#userId")
public int deleteUserByID(String userId) {
  LogUtil.info(logger, "删除用户，id={0}", userId);
  return 1;
}
```

`@CacheEvict` 配置项与 `@Cacheable` 也基本相同，值得一提的是其allEntries和beforeInvocation属性。

- **allEntries**：是否要清除整个缓存，默认false，当指定为true时，将会清空value属性对应的整个缓存，此时框架将忽略 `@CacheEvict` 所配置的key属性。
- **beforeInvocation**：是否在方法执行之前删除缓存，默认false，表示方法成功完成后删除缓存（如果该方法不执行或发生异常，则不会删除缓存），当指定为true时，在执行方法前就删除缓存，与方法执行结果无关。

#### 2.3.4 分布式环境下访问缓存

以获取头寸和修改头寸来模拟分布式环境下访问缓存的场景。该场景中，要求在分布式环境下操作缓存并保证数据一致性，采用分布式锁的方式来实现。

例如，在修改头寸的方法中，不是直接修改缓存的值，而是首先尝试获取锁，获取到锁之后才进行修改缓存的操作，代码如下：

```java
public void changePosition(String positionId, int change) {
  final Lock lock = redisLockRegistry.obtain(positionId);
  try {
    if (lock.tryLock(RETRY_TIMEOUT, TimeUnit.SECONDS)) { // 尝试获取锁
      Integer oldPosition = RedisUtil.get(redisTemplate, positionId, Integer.class); // 查找头寸

      // 设置头寸开始
      if (null == oldPosition) {
        // 如果缓存中不存在头寸，则将change视为初始头寸，存入缓存
        RedisUtil.setWithDefaultExpire(redisTemplate, positionId, change);
      } else {
        // 如果缓存中存在头寸，则计算修改后的头寸，存入缓存
        RedisUtil.setWithDefaultExpire(redisTemplate, positionId, oldPosition + change);
      }
      // 设置头寸结束
    } else {
      LogUtil.warn(logger, "头寸修改获取锁失败,id={0},change={1}", positionId, change);
    }
  } catch (Exception e) {
    LogUtil.error(e, logger, "头寸修改失败,id={0},change={1}", positionId, change);
  } finally {
    try {
      lock.unlock();
    } catch (IllegalStateException e) {
      LogUtil.error(e, logger, "头寸修改尝试解锁异常");
    }
  }
}
```

在 `com.sunveee.template.ssm.test.cache.redis.RedisCacheTest` 中编写了缓存服务的单元测试。