package uk.co.mruoc.nac.app;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenResponse;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.client.GameEventSubscriber;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiTokenClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;
import uk.co.mruoc.nac.client.TokenCredentials;
import uk.co.mruoc.nac.environment.integrated.cognito.AdminCognitoTokenCredentials;
import uk.co.mruoc.nac.environment.integrated.cognito.User1CognitoTokenCredentials;
import uk.co.mruoc.nac.environment.integrated.cognito.User2CognitoTokenCredentials;

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
      NaughtsAndCrossesApiClient client = getAdminRestClient();
      client.synchronizeExternalUsers();
    }
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    NaughtsAndCrossesApiClient client = getAdminRestClient();
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

  public NaughtsAndCrossesApiClient getAdminRestClient() {
    return getAdminRestClient(new AdminCognitoTokenCredentials());
  }

  public NaughtsAndCrossesApiClient getUser1RestClient() {
    return getAdminRestClient(new User1CognitoTokenCredentials());
  }

  public NaughtsAndCrossesApiClient getUser2RestClient() {
    return getAdminRestClient(new User2CognitoTokenCredentials());
  }

  public NaughtsAndCrossesApiClient getAdminRestClient(TokenCredentials credentials) {
    return new NaughtsAndCrossesApiClient(environment.getAppUrl(), generateToken(credentials));
  }

  public void connectToWebsocket() {
    connectToWebsocket(new AdminCognitoTokenCredentials());
  }

  public void connectToWebsocket(TokenCredentials credentials) {
    if (Objects.isNull(websocketClient)) {
      websocketClient = buildWebsocketClient(credentials);
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

  private NaughtsAndCrossesWebsocketClient buildWebsocketClient(TokenCredentials credentials) {
    return new NaughtsAndCrossesWebsocketClient(
        environment.getAppUrl(), generateToken(credentials));
  }

  private String generateToken(TokenCredentials credentials) {
    NaughtsAndCrossesApiTokenClient client =
        new NaughtsAndCrossesApiTokenClient(environment.getAppUrl());
    ApiCreateTokenResponse response = client.createToken(credentials);
    return response.getAccessToken();
  }

  private void shutdown() {
    if (started) {
      appRunner.shutdownIfRunning();
      environment.stopDependentServices();
    }
  }
}
