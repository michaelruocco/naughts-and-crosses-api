package uk.co.mruoc.nac.environment.integrated.activemq;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestActiveMQContainer extends GenericContainer<TestActiveMQContainer> {

  private static final int STOMP_PORT = 61613;

  public TestActiveMQContainer() {
    super(DockerImageName.parse("apache/activemq-artemis:latest-alpine"));
    withExposedPorts(STOMP_PORT);
  }

  public int getMappedStompPort() {
    return getMappedPort(STOMP_PORT);
  }
}
