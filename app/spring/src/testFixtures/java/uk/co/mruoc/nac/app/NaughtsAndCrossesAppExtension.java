package uk.co.mruoc.nac.app;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.client.GameEventSubscriber;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;

@Slf4j
@RequiredArgsConstructor
public class NaughtsAndCrossesAppExtension
    implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, CloseableResource {

  private boolean started = false;
  private NaughtsAndCrossesWebsocketClient websocketClient;

  private final TestEnvironment environment;
  private final NaughtsAndCrossesAppRunner appRunner;

  public NaughtsAndCrossesAppExtension(TestEnvironment environment) {
    this(environment, new NaughtsAndCrossesAppRunner());
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    if (!started) {
      log.info("starting extension");
      environment.startDependentServices();
      appRunner.startIfNotStarted(environment);
      log.info("extension startup complete");
      started = true;
    }
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    NaughtsAndCrossesApiClient client = getRestClient();
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

  public NaughtsAndCrossesApiClient getRestClient() {
    return environment.buildApiClient();
  }

  public void connectToWebsocket() {
    if (Objects.isNull(websocketClient)) {
      websocketClient = environment.buildWebsocketClient();
      websocketClient.connect();
    }
  }

  public GameEventSubscriber<ApiGame> subscribeToGameUpdateEvents() {
    return websocketClient.subscribeToGameUpdateEvents();
  }

  public GameEventSubscriber<Long> subscribeToGameDeleteEvents() {
    return websocketClient.subscribeToGameDeleteEvents();
  }

  public void disconnectWebsocket() {
    if (Objects.isNull(websocketClient)) {
      return;
    }
    websocketClient.close();
    websocketClient = null;
  }

  private void shutdown() {
    if (started) {
      appRunner.shutdownIfRunning();
      environment.stopDependentServices();
    }
  }
}
