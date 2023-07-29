package io.github.flynn.polaris.test.containers;

import com.github.dockerjava.api.command.InspectContainerResponse;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisContainer extends GenericContainer<RedisContainer> {

  private static final String IMAGE_TAG = "redis:6.0";
  private static final int REDIS_PORT = 6379;
  private final Map<String, String> envConfig = new HashMap<>();

  public RedisContainer() {
    super(DockerImageName.parse(IMAGE_TAG));
  }

  @Override
  public RedisContainer withEnv(String key, String value) {
    envConfig.put(key, value);
    System.setProperty(key, value);
    return super.withEnv(key, value);
  }

  @Override
  protected void configure() {
    super.configure();
    addExposedPort(REDIS_PORT);
  }

  @Override
  protected void containerIsStopped(InspectContainerResponse containerInfo) {
    super.containerIsStopped(containerInfo);
    envConfig.keySet().forEach(System::clearProperty);
  }
}
