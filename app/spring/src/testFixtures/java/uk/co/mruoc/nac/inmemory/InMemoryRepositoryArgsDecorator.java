package uk.co.mruoc.nac.inmemory;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class InMemoryRepositoryArgsDecorator implements UnaryOperator<Stream<String>> {

  @Override
  public Stream<String> apply(Stream<String> args) {
    return Stream.concat(args, postgresArgs());
  }

  private Stream<String> postgresArgs() {
    return Stream.of("--in.memory.repository.enabled=true");
  }
}
