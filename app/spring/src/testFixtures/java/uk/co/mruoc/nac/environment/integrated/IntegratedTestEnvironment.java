package uk.co.mruoc.nac.environment.integrated;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.environment.integrated.kafka.TestKafkaContainer;
import uk.co.mruoc.nac.environment.integrated.postgres.TestPostgresContainer;

@RequiredArgsConstructor
@Slf4j
public class IntegratedTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  private static final TestKafkaContainer KAFKA = new TestKafkaContainer();

  private final String kafkaGameEventTopicName;

  public IntegratedTestEnvironment() {
    this("game-event");
  }

  @Override
  public void startDependentServices() {
    log.info("starting postgres");
    POSTGRES.start();
    log.info("starting kafka");
    KAFKA.start();
    KAFKA.createTopics(kafkaGameEventTopicName);
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
        .kafkaGameEventTopicName(kafkaGameEventTopicName)
        .build();
  }
}
