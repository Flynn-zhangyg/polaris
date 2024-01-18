package io.github.flynn.polaris.spring.kafka.test;


import static org.awaitility.Awaitility.waitAtMost;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.flynn.polaris.spring.kafka.configuration.PolarisKafkaConfiguration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({MockKafkaListener.class})
@ImportAutoConfiguration({PolarisKafkaConfiguration.class, KafkaAutoConfiguration.class})
@DirtiesContext
@EnableKafka
@EmbeddedKafka(partitions = 1, brokerProperties = {
    "listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestPropertySource(properties = {"spring.kafka.consumer.group-id=test",
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.kafka.consumer.auto-offset-reset=earliest",
    "spring.kafka.consumer.enable-auto-commit=false",
    "spring.kafka.listener.ack-mode=MANUAL_IMMEDIATE",
    "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
    "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
    "spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.ByteArraySerializer",
    "spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer",
    "spring.kafka.consumer.properties.spring.json.trusted.packages=*",
    "spring.kafka.producer.properties.spring.json.trusted.packages=*"
})
public class KafkaRetryTest {

  @Autowired
  private KafkaTemplate kafkaTemplate;

  @MockBean
  private MockService mockService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testRetry() throws Exception {
    kafkaTemplate.send("retry-test",
        objectMapper.writeValueAsBytes(MockSentMessage.builder().msg("test").build()));
    waitAtMost(10, TimeUnit.SECONDS).untilAsserted(
        () -> verify(mockService, times(4)).handle("test"));

    waitAtMost(10, TimeUnit.SECONDS).untilAsserted(
        () -> verify(mockService, atLeast(6)).handleDeadLetter("test"));

    waitAtMost(10, TimeUnit.SECONDS).untilAsserted(
        () -> verify(mockService, never()).handNestedDeadLetter(anyString()));
  }
}
