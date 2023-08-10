package io.github.flynn.polaris.spring.apollo.refresh;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import io.github.flynn.polaris.spring.apollo.annotation.RefreshableConfigurationProperties;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;


@Configuration
@Slf4j
public class ApolloConfigAutoRefresher implements ApplicationContextAware {

  private static final String LOGGER_TAG = "logging.level.";

  private ApplicationContext applicationContext;

  @Autowired
  private LoggingSystem loggingSystem;

  @Autowired
  private RefreshScope refreshScope;

  @ApolloConfigChangeListener("${polaris.apollo.auto-refresh.interested.namespaces:application}")
  public void refreshConfig(ConfigChangeEvent event) {
    refreshConfigurationProperties(event);
    refreshLogLevel(event);
  }


  private void refreshLogLevel(ConfigChangeEvent event) {

    Set<String> keyNames = event.changedKeys();
    for (String key : keyNames) {
      if (StringUtils.containsIgnoreCase(key, LOGGER_TAG)) {
        String strLevel = event.getChange(key).getNewValue();
        LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
        loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
        log.debug("updated logging level: {} -> {}.", key, strLevel);
      }
    }
  }

  private void refreshConfigurationProperties(ConfigChangeEvent event) {
    Map<String, Object> configBeanMap =
        applicationContext.getBeansWithAnnotation(RefreshableConfigurationProperties.class);
    if (ObjectUtils.isEmpty(configBeanMap)) {
      return;
    }

    configBeanMap.forEach((className, configClass) -> {
      if (StringUtils.isBlank(className) || ObjectUtils.isEmpty(configClass)) {
        return;
      }
      RefreshableConfigurationProperties configurationProperties =
          configClass.getClass().getAnnotation(RefreshableConfigurationProperties.class);
      if (configurationProperties == null) {
        return;
      }
      String prefix = configurationProperties.prefix();
      if (StringUtils.isBlank(prefix.trim())) {
        return;
      }

      for (String changeKey : event.changedKeys()) {
        if (changeKey.startsWith(prefix)) {
          log.debug("Apollo key = {} refreshed.", changeKey);
          refreshScope.refresh(className);
        }
      }
    });
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
