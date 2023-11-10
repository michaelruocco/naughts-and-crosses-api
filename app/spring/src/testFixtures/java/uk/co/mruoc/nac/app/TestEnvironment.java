package uk.co.mruoc.nac.app;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface TestEnvironment {

  void startDependentServices();

  void stopDependentServices();

  UnaryOperator<Stream<String>> getArgsDecorator();
}
