package uk.co.mruoc.nac.environment.integrated;

import java.net.SocketAddress;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;
import uk.co.mruoc.nac.environment.LocalApp;
import uk.co.mruoc.nac.environment.integrated.kafka.TestKafkaContainer;
import uk.co.mruoc.nac.environment.integrated.keycloak.TestKeycloakContainer;
import uk.co.mruoc.nac.environment.integrated.postgres.TestPostgresContainer;

@RequiredArgsConstructor
@Slf4j
public class IntegratedTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  private static final TestKafkaContainer KAFKA = new TestKafkaContainer();

  private static final TestKeycloakContainer KEYCLOAK = new TestKeycloakContainer();

  private final LocalApp localApp;
  private final String kafkaGameEventTopicName;

  public IntegratedTestEnvironment() {
    this(new LocalApp(), "game-event");
  }

  @Override
  public void startDependentServices() {
    log.info("starting keycloak");
    KEYCLOAK.start();
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
    log.info("stopping keycloak");
    KEYCLOAK.close();
  }

  @Override
  public int getAppPort() {
    return localApp.getPort();
  }

  @Override
  public SocketAddress getAppSocketAddress() {
    return localApp.getSocketAddress();
  }

  @Override
  public String[] getAppArgs() {
    return IntegratedTestEnvironmentArgsDecorator.builder()
        .dbHost(POSTGRES.getHost())
        .dbPort(POSTGRES::getFirstMappedPort)
        .dbName(POSTGRES.getDatabaseName())
        .kafkaBootstrapServers(KAFKA.getBootstrapServers())
        .kafkaGameEventTopicName(kafkaGameEventTopicName)
        .authIssuerUrl(KEYCLOAK.getIssuerUrl())
        .build()
        .apply(Stream.of(localApp.getServerPortArg()))
        .toArray(String[]::new);
  }

  @Override
  public NaughtsAndCrossesApiClient buildApiClient() {
    return new NaughtsAndCrossesApiClient(localApp.getUrl(), KEYCLOAK.getAuthTokenValue());
  }

  @Override
  public NaughtsAndCrossesWebsocketClient buildWebsocketClient() {
    return new NaughtsAndCrossesWebsocketClient(localApp.getUrl(), KEYCLOAK.getAuthTokenValue());
  }
}
