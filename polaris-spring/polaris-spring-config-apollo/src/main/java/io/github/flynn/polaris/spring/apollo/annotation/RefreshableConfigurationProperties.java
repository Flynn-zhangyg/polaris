package io.github.flynn.polaris.spring.apollo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@RefreshScope
@ConfigurationProperties
public @interface RefreshableConfigurationProperties {

  @AliasFor(annotation = Component.class)
  String value() default "";

  @AliasFor(annotation = ConfigurationProperties.class)
  String prefix() default "";
}
