package io.github.flynn.polaris.test.containers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.flynn.polaris.test.containers.mapper.UserRepo;
import io.github.flynn.polaris.test.containers.models.MongoUser;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest
@EnableMongoRepositories(basePackageClasses = UserRepo.class)
class MongoDBContainerTest {

  @Container
  private static final MongoDBContainer MONGO = new MongoDBContainer();

  @Autowired
  private UserRepo userRepo;

  @Test
  void simpleTest() {
    MongoUser bob = userRepo.insert(MongoUser.builder().name("Bob").build());
    Optional<MongoUser> byId = userRepo.findById(bob.getId());
    assertThat(byId).isPresent();
    assertThat(byId.get().getName()).isEqualTo("Bob");
  }
}