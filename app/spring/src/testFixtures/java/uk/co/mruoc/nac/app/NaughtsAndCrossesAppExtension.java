package uk.co.mruoc.nac.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.client.GameUpdateListener;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;

@Slf4j
@RequiredArgsConstructor
public class NaughtsAndCrossesAppExtension
    implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, CloseableResource {

  private boolean started = false;
  private TestAppConfig appConfig;
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
      appConfig = toConfig(environment);
      appRunner.startIfNotStarted(appConfig);
      websocketClient = new NaughtsAndCrossesWebsocketClient(appConfig.getAppUrl());
      log.info("extension startup complete");
      started = true;
    }
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    NaughtsAndCrossesApiClient client = getRestClient();
    client.deleteAllGames();
    client.resetIds();
    websocketClient.removeAllListeners();
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
    return new NaughtsAndCrossesApiClient(appConfig.getAppUrl());
  }

  public void add(GameUpdateListener listener) {
    websocketClient.add(listener);
  }

  private void shutdown() {
    if (started) {
      websocketClient.close();
      appRunner.shutdownIfRunning();
      environment.stopDependentServices();
    }
  }

  private static TestAppConfig toConfig(TestEnvironment environment) {
    return TestAppConfig.builder()
        .appPort(AvailablePortFinder.findAvailableTcpPort())
        .argsDecorator(environment.getArgsDecorator())
        .build();
  }
}
