package uk.co.mruoc.nac.environment.integrated;

import java.net.SocketAddress;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.environment.LocalApp;
import uk.co.mruoc.nac.environment.integrated.activemq.TestActiveMQContainer;
import uk.co.mruoc.nac.environment.integrated.clamav.TestClamAvContainer;
import uk.co.mruoc.nac.environment.integrated.cognito.TestCognitoContainer;
import uk.co.mruoc.nac.environment.integrated.cognito.TestMockServerContainer;
import uk.co.mruoc.nac.environment.integrated.postgres.TestPostgresContainer;

@RequiredArgsConstructor
@Slf4j
public class IntegratedTestEnvironment implements TestEnvironment {

  private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

  private static final TestCognitoContainer COGNITO = new TestCognitoContainer();

  private static final TestActiveMQContainer ACTIVEMQ = new TestActiveMQContainer();

  private static final TestClamAvContainer CLAM_AV = new TestClamAvContainer();

  private static final TestMockServerContainer MOCK_SERVER = new TestMockServerContainer();

  private final LocalApp localApp;

  public IntegratedTestEnvironment() {
    this(new LocalApp());
  }

  @Override
  public void startDependentServices() {
    log.info("starting cognito");
    COGNITO.start();
    log.info("starting postgres");
    POSTGRES.start();
    log.info("starting activemq");
    ACTIVEMQ.start();
    log.info("starting clam av");
    CLAM_AV.start();
    log.info("starting mock server");
    MOCK_SERVER.start();
  }

  @Override
  public void stopDependentServices() {
    log.info("stopping activemq");
    ACTIVEMQ.close();
    log.info("stopping postgres");
    POSTGRES.close();
    log.info("stopping cognito");
    COGNITO.close();
    log.info("stopping clam av");
    CLAM_AV.close();
    log.info("stopping mock server");
    MOCK_SERVER.stop();
  }

  @Override
  public int getAppPort() {
    return localApp.getPort();
  }

  @Override
  public String getAppUrl() {
    return localApp.getUrl();
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
        .authCodeUrl(MOCK_SERVER.getUri())
        .authIssuerUrl(COGNITO.getIssuerUrl())
        .cognitoEndpointOverride(COGNITO.getBaseUri())
        .userPoolId(COGNITO.getUserPoolId())
        .userPoolClientId(COGNITO.getUserPoolClientId())
        .awsAccessKeyId("abc")
        .awsSecretAccessKey("123")
        .clamAvHost(CLAM_AV.getHost())
        .clamAvPort(CLAM_AV.getFirstMappedPort())
        .build()
        .apply(Stream.of(localApp.getServerPortArg()))
        .toArray(String[]::new);
  }
}
