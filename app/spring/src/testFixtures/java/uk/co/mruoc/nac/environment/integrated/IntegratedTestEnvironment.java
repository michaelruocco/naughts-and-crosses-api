package uk.co.mruoc.nac.environment.integrated;

import java.net.SocketAddress;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;
import uk.co.mruoc.nac.environment.LocalApp;
import uk.co.mruoc.nac.environment.integrated.activemq.TestActiveMQContainer;
import uk.co.mruoc.nac.environment.integrated.cognito.DefaultCognitoTokenCredentials;
import uk.co.mruoc.nac.environment.integrated.cognito.TestCognitoContainer;
import uk.co.mruoc.nac.environment.integrated.postgres.TestPostgresContainer;
import uk.mruoc.nac.access.TokenCredentials;

@RequiredArgsConstructor
@Slf4j
public class IntegratedTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  private static final TestCognitoContainer COGNITO = new TestCognitoContainer();

  private static final TestActiveMQContainer ACTIVEMQ = new TestActiveMQContainer();

  private final LocalApp localApp;

  public IntegratedTestEnvironment() {
    this(new LocalApp());
  }

  @Override
  public void startDependentServices() {
    log.info("starting cognito");
    COGNITO.start();
    COGNITO.init();
    log.info("starting postgres");
    POSTGRES.start();
    log.info("starting activemq");
    ACTIVEMQ.start();
  }

  @Override
  public void stopDependentServices() {
    log.info("stopping activemq");
    ACTIVEMQ.close();
    log.info("stopping postgres");
    POSTGRES.close();
    log.info("stopping cognito");
    COGNITO.close();
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
        .brokerHost(ACTIVEMQ.getHost())
        .brokerPort(ACTIVEMQ.getMappedStompPort())
        .authIssuerUrl(COGNITO.getIssuerUrl())
        .cognitoEndpointOverride(COGNITO.getBaseUri())
        .userPoolId(COGNITO.getUserPoolId())
        .awsAccessKeyId("abc")
        .awsSecretAccessKey("123")
        .build()
        .apply(Stream.of(localApp.getServerPortArg()))
        .toArray(String[]::new);
  }

  @Override
  public NaughtsAndCrossesApiClient buildApiClient(TokenCredentials credentials) {
    return new NaughtsAndCrossesApiClient(
        localApp.getUrl(), COGNITO.getAuthTokenValue(credentials));
  }

  @Override
  public NaughtsAndCrossesWebsocketClient buildWebsocketClient() {
    return new NaughtsAndCrossesWebsocketClient(
        localApp.getUrl(), COGNITO.getAuthTokenValue(new DefaultCognitoTokenCredentials()));
  }
}
