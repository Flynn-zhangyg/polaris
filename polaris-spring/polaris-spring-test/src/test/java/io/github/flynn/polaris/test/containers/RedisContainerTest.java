package io.github.flynn.polaris.test.containers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.flynn.polaris.test.containers.apps.NoDataBaseApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NoDataBaseApplication.class)
@Testcontainers
public class RedisContainerTest {

  @Container
  private final static RedisContainer REDIS = new RedisContainer();

  @BeforeAll
  static void beforeAll() {
    System.setProperty("spring.data.redis.host", REDIS.getHost());
    System.setProperty("spring.data.redis.port", String.valueOf(REDIS.getFirstMappedPort()));
  }

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Test
  void simpleTest() {
      redisTemplate.opsForValue().set("key", "abc");
      assertThat(redisTemplate.opsForValue().get("key")).isEqualTo("abc");
  }
}
