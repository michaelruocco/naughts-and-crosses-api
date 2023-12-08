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

  private final String kafkaBootstrapServers;
  private final String kafkaGameEventTopicName;

  private final String authIssuerUrl;

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
        "--kafka.listeners.enabled=true",
        "--kafka.producers.enabled=true",
        "--kafka.consumer.group.id=naughts-and-crosses-api-group",
        "--kafka.client.id=naughts-and-crosses-api-client-id",
        "--kafka.security.protocol=PLAINTEXT",
        String.format("--kafka.game.event.topic=%s", kafkaGameEventTopicName),
        String.format("--kafka.bootstrap.servers=%s", kafkaBootstrapServers),
        String.format("--auth.issuer.url=%s", authIssuerUrl));
  }

  private String buildJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort.get(), dbName);
  }
}
