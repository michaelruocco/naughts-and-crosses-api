package uk.co.mruoc.nac.environment.integrated.cognito;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

import java.net.URI;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import uk.co.mruoc.nac.environment.AvailablePortFinder;

public class TestCognitoContainer extends FixedHostPortGenericContainer<TestCognitoContainer> {

  private static final String ACCESS_KEY_ID = "abc";
  private static final String SECRET_ACCESS_KEY = "123";

  private final int port;

  public TestCognitoContainer() {
    this(AvailablePortFinder.findAvailableTcpPort());
  }

  public TestCognitoContainer(int port) {
    super("michaelruocco/cognito-local:latest");
    withEnv("AWS_ACCESS_KEY_ID", ACCESS_KEY_ID);
    withEnv("AWS_SECRET_ACCESS_KEY", SECRET_ACCESS_KEY);
    withEnv("PORT", Integer.toString(port));
    withEnv("CODE", "9999");
    withEnv("USE_EXTERNAL_IP_AS_HOSTNAME", "true");
    withFixedExposedPort(port, port);
    withFileCopiedToContainer("local_4RsGXSAf.json");
    withFileCopiedToContainer("clients.json");
    this.port = port;
  }

  public CognitoIdentityProviderClient buildIdentityProviderClient() {
    return CognitoIdentityProviderClient.builder()
        .credentialsProvider(credentialsProvider())
        .endpointOverride(URI.create(getBaseUri()))
        .region(Region.EU_CENTRAL_1)
        .build();
  }

  public String getIssuerUrl() {
    return String.format("%s/%s", getBaseUri(), getUserPoolId());
  }

  public String getBaseUri() {
    return String.format("http://localhost:%d", getMappedPort(port));
  }

  public String getUserPoolId() {
    return "local_4RsGXSAf";
  }

  public String getUserPoolClientId() {
    return "6b0j5hb1u25z290vv502lfl1c";
  }

  private void withFileCopiedToContainer(String filename) {
    String classpathPath = String.format("cognito/%s", filename);
    String containerPath = String.format("/app/.cognito/db/%s", filename);
    withCopyToContainer(forClasspathResource(classpathPath), containerPath);
  }

  private static AwsCredentialsProvider credentialsProvider() {
    AwsBasicCredentials credentials =
        AwsBasicCredentials.builder()
            .accessKeyId(ACCESS_KEY_ID)
            .secretAccessKey(SECRET_ACCESS_KEY)
            .build();
    return StaticCredentialsProvider.create(credentials);
  }
}
