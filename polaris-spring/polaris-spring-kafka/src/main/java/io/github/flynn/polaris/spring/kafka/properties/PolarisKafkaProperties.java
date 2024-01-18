package io.github.flynn.polaris.spring.kafka.properties;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@Data
@ConfigurationProperties(prefix = "polaris.kafka")
@NoArgsConstructor
@AllArgsConstructor
public class PolarisKafkaProperties {

  @Default
  private long retryInterval = 0L;

  @Default
  private long maximumRetryTimes = 3L;

  @Default
  private String deadLetterTopicSuffix = ".DLT";
}
