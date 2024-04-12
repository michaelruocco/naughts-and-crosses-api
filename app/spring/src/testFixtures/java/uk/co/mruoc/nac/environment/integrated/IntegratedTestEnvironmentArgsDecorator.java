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

  private final String keycloakAdminUrl;
  private final String keycloakAdminRealm;
  private final String keycloakAdminClientId;
  private final String keycloakAdminClientSecret;

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
        String.format("--keycloak.admin.client.id=%s", keycloakAdminClientId),
        String.format("--keycloak.admin.client.secret=%s", keycloakAdminClientSecret));
  }

  private String buildJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort.get(), dbName);
  }
}
