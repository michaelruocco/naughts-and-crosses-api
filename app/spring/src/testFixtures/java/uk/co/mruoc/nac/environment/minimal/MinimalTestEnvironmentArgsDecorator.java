package uk.co.mruoc.nac.environment.minimal;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class MinimalTestEnvironmentArgsDecorator implements UnaryOperator<Stream<String>> {

  @Override
  public Stream<String> apply(Stream<String> args) {
    return Stream.concat(args, args());
  }

  private Stream<String> args() {
    return Stream.of(
        "--repository.in.memory.enabled=true",
        "--broker.in.memory.enabled=true",
        "--broker.ssl.enabled=false",
        "--stub.user.provider.enabled=true",
        "--stub.token.service.enabled=true",
        "--stub.virus.scan.enabled=true",
        "--auth.issuer.public.key=classpath:/public-key.pem");
  }
}
