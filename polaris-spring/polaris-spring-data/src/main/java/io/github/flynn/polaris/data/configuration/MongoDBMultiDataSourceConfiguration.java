package io.github.flynn.polaris.data.configuration;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterConnectionMode;
import io.github.flynn.polaris.data.properties.PolarisDataSourceProperties;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


@Configuration
public class MongoDBMultiDataSourceConfiguration implements
    BeanDefinitionRegistryPostProcessor,
    EnvironmentAware {

  private PolarisDataSourceProperties.MongoDataSources dataSources;

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {

    String primary = dataSources.getPrimary();
    dataSources.getDatasource().forEach((name, properties) -> {
      ConnectionString connectString;

      if (StringUtils.isNoneEmpty(properties.getUri())) {
        connectString = new ConnectionString(properties.getUri());
      } else {
        connectString = getMongoDBConnectString(properties);
      }

      MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
          .applyToClusterSettings(builder -> builder.mode(ClusterConnectionMode.MULTIPLE))
          .applyConnectionString(connectString)
          .build()
      );
      SimpleMongoClientDatabaseFactory databaseFactory =
          new SimpleMongoClientDatabaseFactory(mongoClient, connectString.getDatabase());

      BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
          .genericBeanDefinition(MongoTemplate.class, () ->
              new MongoTemplate(databaseFactory)
          );
      beanDefinitionBuilder.setPrimary(Objects.equals(name, primary));

      registry.registerBeanDefinition(name + "MongoTemplate",
          beanDefinitionBuilder.getRawBeanDefinition());
    });
  }

  private ConnectionString getMongoDBConnectString(MongoProperties properties) {
    if (StringUtils.isNoneBlank(properties.getUri())) {
      return new ConnectionString(properties.getUri());
    }

    String password = Arrays.toString(properties.getPassword());

    return new ConnectionString("mongodb://"
        + (StringUtils.isEmpty(properties.getUsername()) ? EMPTY : properties.getUsername() + ":")
        + (StringUtils.isEmpty(password) ? EMPTY : password + "@")
        + properties.getHost()
        + (properties.getPort() == null ? EMPTY : ":" + properties.getPort() + "/")
        + (StringUtils.isEmpty(properties.getDatabase()) ? EMPTY : properties.getDatabase())
    );
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
  }

  @Override
  public void setEnvironment(Environment environment) {
    dataSources = Binder.get(environment)
        .bind("polaris.data.mongo", PolarisDataSourceProperties.MongoDataSources.class).get();
  }
}
