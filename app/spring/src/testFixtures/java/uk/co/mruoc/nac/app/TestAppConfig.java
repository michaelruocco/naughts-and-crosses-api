package uk.co.mruoc.nac.app;

import java.util.function.Supplier;
import lombok.Builder;

@Builder
public class TestAppConfig {

  private final int appPort;

  private final String dbHost;
  private final Supplier<Integer> dbPort;
  private final String dbName;

  public String[] asArgs() {
    return new String[] {
      String.format("--server.port=%d", appPort),
      "--database.username=postgres",
      "--database.password=postgres",
      String.format("--database.url=%s", buildJdbcUrl()),
      "--database.driver=org.postgresql.Driver",
    };
  }

  public int getAppPort() {
    return appPort;
  }

  public String getAppUrl() {
    return String.format("http://localhost:%d", appPort);
  }

  private String buildJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort.get(), dbName);
  }
}
