package uk.co.mruoc.nac.environment.integrated;

import java.net.SocketAddress;
import java.time.Clock;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.cognito.TestCognitoContainer;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.app.config.LocalCognitoUserPoolConfig;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;
import uk.co.mruoc.nac.environment.LocalApp;
import uk.co.mruoc.nac.environment.integrated.activemq.TestActiveMQContainer;
import uk.co.mruoc.nac.environment.integrated.cognito.DefaultCognitoTokenCredentials;
import uk.co.mruoc.nac.environment.integrated.postgres.TestPostgresContainer;
import uk.mruoc.nac.access.AccessToken;
import uk.mruoc.nac.access.CognitoAccessTokenClient;
import uk.mruoc.nac.access.TokenCredentials;

@RequiredArgsConstructor
@Slf4j
public class IntegratedTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  private static final TestCognitoContainer COGNITO =
      new TestCognitoContainer(new LocalCognitoUserPoolConfig());

  private static final TestActiveMQContainer ACTIVEMQ = new TestActiveMQContainer();

  private final LocalApp localApp;

  public IntegratedTestEnvironment() {
    this(new LocalApp());
  }

  @Override
  public void startDependentServices() {
    log.info("starting cognito");
    COGNITO.start();
    COGNITO.createUserPool();
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
    return new NaughtsAndCrossesApiClient(localApp.getUrl(), getAuthTokenValue(credentials));
  }

  @Override
  public NaughtsAndCrossesWebsocketClient buildWebsocketClient() {
    return new NaughtsAndCrossesWebsocketClient(
        localApp.getUrl(), getAuthTokenValue(new DefaultCognitoTokenCredentials()));
  }

  private static String getAuthTokenValue(TokenCredentials credentials) {
    CognitoAccessTokenClient client =
        CognitoAccessTokenClient.builder()
            .client(COGNITO.buildIdentityProviderClient())
            .userPoolId(COGNITO.getUserPoolId())
            .clientId(COGNITO.getUserPoolClientId())
            .clock(Clock.systemUTC())
            .build();
    AccessToken token = client.generateToken(credentials);
    return token.getValue();
  }
}
