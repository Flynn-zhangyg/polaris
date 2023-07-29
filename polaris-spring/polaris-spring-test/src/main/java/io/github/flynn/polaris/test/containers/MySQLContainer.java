package io.github.flynn.polaris.test.containers;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.testcontainers.utility.DockerImageName;

public class MySQLContainer extends org.testcontainers.containers.MySQLContainer<MySQLContainer> {

  private static final String IMG_TAG = "mysql:8.0.33";

  public MySQLContainer() {
    super(DockerImageName.parse(IMG_TAG));
    this.withUsername("root");
    this.withPassword("root");
  }

  @Override
  protected void containerIsStarting(InspectContainerResponse containerInfo) {
    super.containerIsStarting(containerInfo);
    System.setProperty("spring.datasource.driverClassName", getDriverClassName());
    System.setProperty("spring.datasource.url", getJdbcUrl());
    System.setProperty("spring.datasource.username", getUsername());
    System.setProperty("spring.datasource.password", getPassword());
  }

  @Override
  protected void containerIsStopped(InspectContainerResponse containerInfo) {
    super.containerIsStopped(containerInfo);
    System.clearProperty("spring.datasource.driverClassName");
    System.clearProperty("spring.datasource.url");
    System.clearProperty("spring.datasource.username");
    System.clearProperty("spring.datasource.password");
  }
}
