package uk.co.mruoc.nac.app;

import org.junit.jupiter.api.extension.RegisterExtension;
import uk.co.mruoc.nac.environment.integrated.IntegratedTestEnvironment;

class IntegratedNaughtsAndCrossesAppIntegrationTest extends NaughtsAndCrossesAppIntegrationTest {

  @RegisterExtension
  public static final NaughtsAndCrossesAppExtension EXTENSION =
      new NaughtsAndCrossesAppExtension(new IntegratedTestEnvironment());

  @Override
  public NaughtsAndCrossesAppExtension getExtension() {
    return EXTENSION;
  }

  // TODO add tests for rest and websocket endpoints failing when auth token
  // is invalid or not provided
}
