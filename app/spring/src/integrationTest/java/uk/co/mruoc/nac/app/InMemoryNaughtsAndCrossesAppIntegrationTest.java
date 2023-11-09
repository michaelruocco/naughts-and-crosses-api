package uk.co.mruoc.nac.app;

import org.junit.jupiter.api.extension.RegisterExtension;
import uk.co.mruoc.nac.inmemory.InMemoryTestEnvironment;

class InMemoryNaughtsAndCrossesAppIntegrationTest
    implements NaughtsAndCrossesAppIntegrationTest {

  @RegisterExtension
  public static final NaughtsAndCrossesAppExtension EXTENSION =
      new NaughtsAndCrossesAppExtension(new InMemoryTestEnvironment());

  @Override
  public NaughtsAndCrossesAppExtension getExtension() {
    return EXTENSION;
  }
}
