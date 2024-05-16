package uk.co.mruoc.nac.environment.integrated.cognito;

import java.net.URI;
import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import uk.co.mruoc.nac.environment.AvailablePortFinder;
import uk.co.mruoc.nac.user.cognito.CognitoUserPoolAndClientIds;
import uk.co.mruoc.nac.user.cognito.LocalDockerCognitoConfigurer;
import uk.mruoc.nac.access.AccessTokenClient;
import uk.mruoc.nac.access.CognitoAccessTokenClient;
import uk.mruoc.nac.access.TokenCredentials;

@Slf4j
public class TestCognitoContainer extends FixedHostPortGenericContainer<TestCognitoContainer> {

  private static final String CONFIRMATION_CODE = "9999";

  private final int port;

  private CognitoUserPoolAndClientIds poolAndClientIds;
  private AccessTokenClient tokenClient;

  public TestCognitoContainer() {
    this(AvailablePortFinder.findAvailableTcpPort());
  }

  public TestCognitoContainer(int port) {
    super("michaelruocco/cognito-local:latest");
    withEnv("AWS_ACCESS_KEY_ID", "abc");
    withEnv("AWS_SECRET_ACCESS_KEY", "123");
    withEnv("PORT", Integer.toString(port));
    withEnv("CODE", CONFIRMATION_CODE);
    withFixedExposedPort(port, port);
    this.port = port;
  }

  public void init() {
    CognitoIdentityProviderClient identityProviderClient = buildIdentityProviderClient();
    poolAndClientIds =
        LocalDockerCognitoConfigurer.builder()
            .client(identityProviderClient)
            .confirmationCode(CONFIRMATION_CODE)
            .build()
            .configure();
    tokenClient = buildAccessTokenClient(identityProviderClient, poolAndClientIds);
  }

  public String getAuthTokenValue(TokenCredentials credentials) {
    return tokenClient.generateToken(credentials).getValue();
  }

  public String getIssuerUrl() {
    return String.format("%s/%s", getBaseUri(), getUserPoolId());
  }

  public String getBaseUri() {
    return String.format("http://localhost:%d", getMappedPort(port));
  }

  public String getUserPoolId() {
    return poolAndClientIds.getPoolId();
  }

  private CognitoIdentityProviderClient buildIdentityProviderClient() {
    return CognitoIdentityProviderClient.builder()
        .credentialsProvider(credentialsProvider())
        .endpointOverride(URI.create(getBaseUri()))
        .region(Region.EU_CENTRAL_1)
        .build();
  }

  private static AccessTokenClient buildAccessTokenClient(
      CognitoIdentityProviderClient identityProviderClient,
      CognitoUserPoolAndClientIds poolAndClientIds) {
    return CognitoAccessTokenClient.builder()
        .client(identityProviderClient)
        .userPoolId(poolAndClientIds.getPoolId())
        .clientId(poolAndClientIds.getClientId())
        .clock(Clock.systemUTC())
        .build();
  }

  private static AwsCredentialsProvider credentialsProvider() {
    AwsBasicCredentials credentials =
        AwsBasicCredentials.builder().accessKeyId("abc").secretAccessKey("123").build();
    return StaticCredentialsProvider.create(credentials);
  }
}
