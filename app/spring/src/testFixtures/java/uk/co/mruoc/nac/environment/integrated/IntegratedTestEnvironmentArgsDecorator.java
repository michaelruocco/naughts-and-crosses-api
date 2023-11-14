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
        "--kafka.game.event.topic=game-event",
        String.format("--kafka.bootstrap.servers=%s", kafkaBootstrapServers));
  }

  private String buildJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort.get(), dbName);
  }
}
