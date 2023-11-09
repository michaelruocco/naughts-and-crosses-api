package uk.co.mruoc.nac.postgres;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;

@Slf4j
public class PostgresTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  @Override
  public void startDependentServices() {
    log.info("starting postgres");
    POSTGRES.start();
  }

  @Override
  public void stopDependentServices() {
    log.info("starting postgres");
    POSTGRES.close();
  }

  @Override
  public UnaryOperator<Stream<String>> getArgsDecorator() {
    return PostgresRepositoryArgsDecorator.builder()
        .dbHost(POSTGRES.getHost())
        .dbPort(POSTGRES::getFirstMappedPort)
        .dbName(POSTGRES.getDatabaseName())
        .build();
  }
}
