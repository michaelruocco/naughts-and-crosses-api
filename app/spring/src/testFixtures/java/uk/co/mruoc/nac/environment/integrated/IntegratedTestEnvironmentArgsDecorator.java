package uk.co.mruoc.nac.environment.integrated;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.Builder;

@Builder
public class IntegratedTestEnvironmentArgsDecorator implements UnaryOperator<Stream<String>> {

  private final String dbHost;
  private final Supplier<Integer> dbPort;
  private final String dbName;

  private final String brokerHost;
  private final int brokerPort;

  private final String authIssuerUrl;
  private final String authCodeUrl;

  private final String cognitoEndpointOverride;
  private final String userPoolId;
  private final String userPoolClientId;
  private final String awsAccessKeyId;
  private final String awsSecretAccessKey;

  private final String clamAvHost;
  private final int clamAvPort;

  @Override
  public Stream<String> apply(Stream<String> args) {
    return Stream.concat(args, args());
  }

  private Stream<String> args() {
    return Stream.of(
        "--database.username=postgres",
        "--database.password=postgres",
        String.format("--database.url=%s", buildJdbcUrl()),
        "--database.driver=org.postgresql.Driver",
        "--broker.ssl.enabled=false",
        String.format("--broker.host=%s", brokerHost),
        String.format("--broker.port=%s", brokerPort),
        "--broker.client.login=artemis",
        "--broker.client.passcode=artemis",
        "--broker.system.login=artemis",
        "--broker.system.passcode=artemis",
        String.format("--auth.issuer.url=%s", authIssuerUrl),
        String.format("--aws.cognito.authCodeEndpoint=%s/oauth2/token", authCodeUrl),
        "--aws.cognito.regionName=eu-central-1",
        String.format("--aws.cognito.userPoolId=%s", userPoolId),
        String.format("--aws.cognito.userPoolClientId=%s", userPoolClientId),
        String.format("--aws.cognito.endpointOverride=%s", cognitoEndpointOverride),
        String.format("--aws.cognito.accessKeyId=%s", awsAccessKeyId),
        String.format("--aws.cognito.secretAccessKey=%s", awsSecretAccessKey),
        String.format("--clam.av.host=%s", clamAvHost),
        String.format("--clam.av.port=%s", clamAvPort),
        String.format("--clam.av.connect.timeout=%s", 2),
        String.format("--clam.av.read.timeout=%s", 20),
        "--stub.jwt.parser.enabled=true");
  }

  private String buildJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort.get(), dbName);
  }
}
