package uk.co.mruoc.nac.app;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.client.DefaultGameUpdateListener;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;
import uk.co.mruoc.nac.environment.integrated.cognito.DefaultCognitoTokenCredentials;
import uk.mruoc.nac.access.TokenCredentials;

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
    return getRestClient(new DefaultCognitoTokenCredentials());
  }

  public NaughtsAndCrossesApiClient getRestClient(TokenCredentials credentials) {
    return environment.buildApiClient(credentials);
  }

  public DefaultGameUpdateListener connectAndListenToWebsocket() {
    if (Objects.isNull(websocketClient)) {
      websocketClient = environment.buildWebsocketClient();
      websocketClient.connect();
    }
    DefaultGameUpdateListener listener = new DefaultGameUpdateListener();
    websocketClient.add(listener);
    return listener;
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
