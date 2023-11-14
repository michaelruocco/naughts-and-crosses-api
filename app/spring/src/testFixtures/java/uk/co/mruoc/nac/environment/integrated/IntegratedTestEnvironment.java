package uk.co.mruoc.nac.environment.integrated;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.environment.integrated.kafka.TestKafkaContainer;
import uk.co.mruoc.nac.environment.integrated.postgres.TestPostgresContainer;

@Slf4j
public class IntegratedTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  private static final TestKafkaContainer KAFKA = new TestKafkaContainer();

  @Override
  public void startDependentServices() {
    log.info("starting postgres");
    POSTGRES.start();
    log.info("starting kafka");
    KAFKA.start();
  }

  @Override
  public void stopDependentServices() {
    log.info("stopping kafka");
    KAFKA.close();
    log.info("stopping postgres");
    POSTGRES.close();
  }

  @Override
  public UnaryOperator<Stream<String>> getArgsDecorator() {
    return IntegratedTestEnvironmentArgsDecorator.builder()
        .dbHost(POSTGRES.getHost())
        .dbPort(POSTGRES::getFirstMappedPort)
        .dbName(POSTGRES.getDatabaseName())
        .kafkaBootstrapServers(KAFKA.getBootstrapServers())
        .build();
  }
}
