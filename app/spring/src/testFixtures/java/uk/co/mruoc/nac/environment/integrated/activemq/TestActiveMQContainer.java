package uk.co.mruoc.nac.environment.integrated.activemq;

import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.utility.DockerImageName;

public class TestActiveMQContainer extends ActiveMQContainer {

  private static final int STOMP_PORT = 61613;

  public TestActiveMQContainer() {
    super(DockerImageName.parse("apache/activemq-classic:latest"));
  }

  public int getMappedStompPort() {
    return getMappedPort(STOMP_PORT);
  }
}
