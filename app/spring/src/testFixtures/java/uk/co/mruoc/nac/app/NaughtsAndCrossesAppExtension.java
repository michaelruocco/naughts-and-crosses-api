package uk.co.mruoc.nac.app;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

public class NaughtsAndCrossesAppExtension
    implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, CloseableResource {

  private static final NaughtsAndCrossesAppRunner APP_RUNNER = new NaughtsAndCrossesAppRunner();

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    APP_RUNNER.startIfNotStarted();
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    NaughtsAndCrossesApiClient client = getAppClient();
    client.deleteAllGames();
    client.resetIds();
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) {
    shutdown();
  }

  @Override
  public void close() {
    shutdown();
  }

  public NaughtsAndCrossesApiClient getAppClient() {
    return new NaughtsAndCrossesApiClient(APP_RUNNER.getUrl());
  }

  private void shutdown() {
    APP_RUNNER.shutdownIfRunning();
  }
}
