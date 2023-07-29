package io.github.flynn.polaris.test.containers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.flynn.polaris.test.containers.models.MongoUser;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest
class MongoDBContainerTest {

  @Container
  private static final MongoDBContainer MONGO = new MongoDBContainer();

  @BeforeAll
  static void beforeAll() {
    System.setProperty("spring.data.mongodb.host", MONGO.getHost());
    System.setProperty("spring.data.mongodb.port", String.valueOf(MONGO.getFirstMappedPort()));
    System.setProperty("spring.data.mongodb.database", "test");
    System.setProperty("spring.data.mongodb.authenticationDatabase", "test");
  }

  @AfterAll
  static void afterAll() {
    System.clearProperty("spring.data.mongodb.host");
    System.clearProperty("spring.data.mongodb.port");
    System.clearProperty("spring.data.mongodb.database");
    System.clearProperty("spring.data.mongodb.authenticationDatabase");
  }

  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  void simpleTest() {
    mongoTemplate.insert(MongoUser.builder().name("Bob").build());
    List<MongoUser> all = mongoTemplate.findAll(MongoUser.class);
    assertThat(all.size()).isNotEqualTo(0);
  }
}