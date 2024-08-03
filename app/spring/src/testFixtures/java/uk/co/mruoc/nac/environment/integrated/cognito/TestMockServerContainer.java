package uk.co.mruoc.nac.environment.integrated.cognito;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class TestMockServerContainer extends MockServerContainer {

  private static final int PORT = 1080;

  public TestMockServerContainer() {
    this("auth-code-responses.json");
  }

  public TestMockServerContainer(String responsesFilename) {
    super(DockerImageName.parse("mockserver/mockserver:latest"));
    withFileCopiedToContainer(responsesFilename);
    withEnv("MOCKSERVER_INITIALIZATION_JSON_PATH", toPath(responsesFilename));
    withExposedPorts(PORT);
    withStartupTimeout(Duration.ofMinutes(1));
    withLogConsumer(TestMockServerContainer::logInfo);
  }

  public String getUri() {
    String host = getHost();
    int port = getMappedPort(PORT);
    return String.format("http://%s:%d", host, port);
  }

  private void withFileCopiedToContainer(String filename) {
    String classpathPath = String.format("mock-server/%s", filename);
    String containerPath = toPath(filename);
    withCopyToContainer(forClasspathResource(classpathPath), containerPath);
  }

  private static String toPath(String filename) {
    return String.format("/%s", filename);
  }

  private static void logInfo(OutputFrame frame) {
    log.info(removeNewline(frame.getUtf8String()));
  }

  private static String removeNewline(String value) {
    return value.replace("\n", "").replace("\r", "");
  }
}
