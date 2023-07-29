package io.github.flynn.polaris.test.containers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.flynn.polaris.test.containers.mapper.UserMapper;
import io.github.flynn.polaris.test.containers.models.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@MapperScan(basePackages = "io.github.flynn.polaris.test.containers.mapper")
@MybatisTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MySQLContainerTest {

  @Container
  public static final MySQLContainer MYSQL_CONTAINER =
      new MySQLContainer().withDatabaseName("testdb");
  @Autowired
  private UserMapper userMapper;

  @Test
  void simpleTest() {
    User user = userMapper.findById(1L);
    assertThat(user.getName()).isEqualTo("abc");
  }
}