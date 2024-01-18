package io.github.flynn.polaris.spring.kafka.configuration;

import io.github.flynn.polaris.spring.kafka.properties.PolarisKafkaProperties;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableConfigurationProperties(PolarisKafkaProperties.class)
public class PolarisKafkaConfiguration {

  @Bean
  KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>>
  kafkaListenerContainerFactory(
      ConsumerFactory<Object, Object> consumerFactory,
      ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
      KafkaTemplate<Object, Object> kafkaTemplate,
      PolarisKafkaProperties properties) {
    ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    configurer.configure(factory, consumerFactory);
    factory.setConsumerFactory(consumerFactory);
    factory.setRecordMessageConverter(new JsonMessageConverter());
    DefaultErrorHandler commonErrorHandler = new DefaultErrorHandler(
        new DeadLetterPublishingRecoverer(kafkaTemplate,
            (record, e) -> {
              String originTopic = record.topic();
              if (!originTopic.endsWith(properties.getDeadLetterTopicSuffix())) {
                originTopic = originTopic + properties.getDeadLetterTopicSuffix();
              }
              return new TopicPartition(originTopic, record.partition());
            }),
        new FixedBackOff(properties.getRetryInterval(), properties.getMaximumRetryTimes()));
    commonErrorHandler.setCommitRecovered(true);
    factory.setCommonErrorHandler(commonErrorHandler);
    return factory;
  }
}
