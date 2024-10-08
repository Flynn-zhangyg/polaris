package io.github.flynn.polaris.test.containers;

import com.github.dockerjava.api.command.InspectContainerResponse;
import java.time.Duration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.wait.strategy.Wait;

public class MongoDBContainer extends org.testcontainers.containers.MongoDBContainer {

  private static final String MONGODB_TAG = "mongo:4.0.28";
  private static final int MONGODB_PORT = 27017;

  public MongoDBContainer() {
    super(MONGODB_TAG);
    addExposedPort(MONGODB_PORT);
  }

  @Override
  protected void containerIsStarting(InspectContainerResponse containerInfo) {
    super.containerIsStarting(containerInfo);
  }

  @Override
  protected void containerIsStopped(InspectContainerResponse containerInfo) {
    super.containerIsStopped(containerInfo);
  }
}
