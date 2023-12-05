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
import uk.mruoc.nac.access.AccessTokenClient;
import uk.mruoc.nac.access.RestAccessTokenClient;
import uk.mruoc.nac.access.RestAccessTokenConfig;

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
    String token = generateAuthToken();
    System.out.println("mruoc creating api client with auth token " + token);
    return new NaughtsAndCrossesApiClient(localApp.getUrl(), token);
  }

  @Override
  public NaughtsAndCrossesWebsocketClient buildWebsocketClient() {
    return new NaughtsAndCrossesWebsocketClient(localApp.getUrl());
  }

  private String generateAuthToken() {
    RestAccessTokenConfig config =
        RestAccessTokenConfig.builder()
            .clientId("naughts-and-crosses-api")
            .clientSecret("naughts-and-crosses-api-secret")
            .grantType("client_credentials")
            .tokenUrl(KEYCLOAK.getTokenUrl())
            .build();
    AccessTokenClient client = new RestAccessTokenClient(config);
    return client.generateToken().getValue();
  }
}
