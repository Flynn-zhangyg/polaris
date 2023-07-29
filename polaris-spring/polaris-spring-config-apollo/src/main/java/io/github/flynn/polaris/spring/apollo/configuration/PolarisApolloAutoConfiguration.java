package io.github.flynn.polaris.spring.apollo.configuration;

import io.github.flynn.polaris.spring.apollo.refresh.ApolloConfigAutoRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolarisApolloAutoConfiguration {

  @Bean
  ApolloConfigAutoRefresher apolloConfigAutoRefresher() {
    return new ApolloConfigAutoRefresher();
  }
}
