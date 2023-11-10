package uk.co.mruoc.nac.app;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.Builder;

@Builder
public class TestAppConfig {

  private final int appPort;
  private final UnaryOperator<Stream<String>> argsDecorator;

  public String[] asArgs() {
    return argsDecorator.apply(buildArgs()).toArray(String[]::new);
  }

  private Stream<String> buildArgs() {
    return Stream.of(String.format("--server.port=%d", appPort));
  }

  public int getAppPort() {
    return appPort;
  }

  public String getAppUrl() {
    return String.format("http://localhost:%d", appPort);
  }
}
