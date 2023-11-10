package uk.co.mruoc.nac.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

@Slf4j
@RequiredArgsConstructor
public class NaughtsAndCrossesAppExtension
    implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, CloseableResource {

  private boolean started = false;
  private final TestEnvironment environment;
  private final NaughtsAndCrossesAppRunner appRunner;

  private final TestAppConfig appConfig;

  public NaughtsAndCrossesAppExtension(TestEnvironment environment) {
    this(environment, new NaughtsAndCrossesAppRunner(), toConfig(environment));
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    if (!started) {
      log.info("starting extension");
      environment.startDependentServices();
      appRunner.startIfNotStarted(appConfig);
      log.info("extension startup complete");
      started = true;
    }
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
    return new NaughtsAndCrossesApiClient(appConfig.getAppUrl());
  }

  private void shutdown() {
    if (started) {
      appRunner.shutdownIfRunning();
      environment.startDependentServices();
    }
  }

  private static TestAppConfig toConfig(TestEnvironment environment) {
    return TestAppConfig.builder()
        .appPort(AvailablePortFinder.findAvailableTcpPort())
        .argsDecorator(environment.getArgsDecorator())
        .build();
  }
}
