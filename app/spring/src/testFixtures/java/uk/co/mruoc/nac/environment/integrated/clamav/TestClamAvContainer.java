package uk.co.mruoc.nac.environment.integrated.clamav;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestClamAvContainer extends GenericContainer<TestClamAvContainer> {

  private static final int PORT = 3310;

  public TestClamAvContainer() {
    super(DockerImageName.parse("clamav/clamav:latest"));
    withExposedPorts(PORT);
  }
}
