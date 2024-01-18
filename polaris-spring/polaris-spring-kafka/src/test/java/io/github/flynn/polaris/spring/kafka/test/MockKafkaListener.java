package io.github.flynn.polaris.spring.kafka.test;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@AllArgsConstructor
public class MockKafkaListener {

  private final MockService mockService;

  @KafkaListener(topics = "retry-test")
  public void handleRetryTest(MockReceivedMessage message) {
    mockService.handle(message.getMsg());
    throw new RuntimeException("Test exception");
  }


  @KafkaListener(topics = "retry-test.DLT")
  public void handleRetryTestDeadLetter(MockReceivedMessage message) {
    mockService.handleDeadLetter(message.getMsg());
    throw new RuntimeException("Test exception");
  }

  @KafkaListener(topics = "retry-test.DLT.DLT")
  public void handleRetryTestDeadLetter1(String message, Acknowledgment acknowledgment) {
    mockService.handNestedDeadLetter(message);
    acknowledgment.acknowledge();
  }
}
