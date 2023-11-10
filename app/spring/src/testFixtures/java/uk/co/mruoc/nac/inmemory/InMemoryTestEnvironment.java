package uk.co.mruoc.nac.inmemory;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;

@Slf4j
public class InMemoryTestEnvironment implements TestEnvironment {

  @Override
  public void startDependentServices() {
    // intentionally blank
  }

  @Override
  public void stopDependentServices() {
    // intentionally blank
  }

  @Override
  public UnaryOperator<Stream<String>> getArgsDecorator() {
    return new InMemoryRepositoryArgsDecorator();
  }
}
