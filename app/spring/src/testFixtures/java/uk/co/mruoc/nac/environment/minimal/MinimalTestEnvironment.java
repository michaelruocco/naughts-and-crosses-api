package uk.co.mruoc.nac.environment.minimal;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;

@Slf4j
public class MinimalTestEnvironment implements TestEnvironment {

  @Override
  public void startDependentServices() {
    log.info("no dependent services to start");
  }

  @Override
  public void stopDependentServices() {
    log.info("no dependent services to stop");
  }

  @Override
  public UnaryOperator<Stream<String>> getArgsDecorator() {
    return new MinimalTestEnvironmentArgsDecorator();
  }
}
