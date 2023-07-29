package io.github.flynn.polaris.data.configuration;

import io.github.flynn.polaris.data.properties.PolarisDataSourceProperties;
import java.time.Duration;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisMultiDataSourceConfiguration implements
    BeanDefinitionRegistryPostProcessor,
    EnvironmentAware {

  private PolarisDataSourceProperties.RedisDataSources dataSources;

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    String primary = dataSources.getPrimary();

    dataSources.getDatasource().forEach((name, properties) -> {
      JedisConnectionFactory jedisConnectionFactory = buildConnectionFactory(properties);

      BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
          .genericBeanDefinition(StringRedisTemplate.class, () ->
              new StringRedisTemplate(jedisConnectionFactory)
          );
      beanDefinitionBuilder.setPrimary(Objects.equals(primary, name));

      registry.registerBeanDefinition(name + "StringRedisTemplate",
          beanDefinitionBuilder.getBeanDefinition());
    });
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
  }

  @Override
  public void setEnvironment(Environment environment) {
    dataSources = Binder.get(environment)
        .bind("polaris.data.redis", PolarisDataSourceProperties.RedisDataSources.class).get();
  }

  public JedisConnectionFactory buildConnectionFactory(RedisProperties properties) {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(properties.getHost());
    redisStandaloneConfiguration.setPort(properties.getPort());
    if (StringUtils.isNotBlank(properties.getPassword())) {
      redisStandaloneConfiguration.setPassword(properties.getPassword());
    }
    if (properties.getDatabase() != 0) {
      redisStandaloneConfiguration.setDatabase(properties.getDatabase());
    }

    JedisClientConfiguration.JedisClientConfigurationBuilder builder =
        JedisClientConfiguration.builder();
    if (properties.getTimeout() != null) {
      Duration timeout = properties.getTimeout();
      builder.readTimeout(timeout).connectTimeout(timeout);
    }
    RedisProperties.Pool pool = properties.getJedis().getPool();
    if (pool != null) {
      builder.usePooling().poolConfig(poolConfig(properties));
    }
    JedisClientConfiguration jedisClientConfiguration = builder.build();
    JedisConnectionFactory jedisConnectionFactory =
        new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    jedisConnectionFactory.afterPropertiesSet();
    return jedisConnectionFactory;
  }

  public JedisPoolConfig poolConfig(RedisProperties properties) {
    RedisProperties.Pool pool = properties.getJedis().getPool();
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(pool.getMaxActive());
    poolConfig.setMaxIdle(pool.getMaxIdle());
    poolConfig.setMinIdle(pool.getMinIdle());
    poolConfig.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
    poolConfig.setMaxWait(pool.getMaxWait());
    return poolConfig;
  }
}
