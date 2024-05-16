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

  private final String cognitoEndpointOverride;
  private final String userPoolId;
  private final String awsAccessKeyId;
  private final String awsSecretAccessKey;

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
        "--aws.cognito.local.docker=false",
        "--aws.cognito.regionName=eu-central-1",
        String.format("--aws.cognito.userPoolId=%s", userPoolId),
        String.format("--aws.cognito.endpointOverride=%s", cognitoEndpointOverride),
        String.format("--aws.cognito.accessKeyId=%s", awsAccessKeyId),
        String.format("--aws.cognito.secretAccessKey=%s", awsSecretAccessKey));
  }

  private String buildJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort.get(), dbName);
  }
}
