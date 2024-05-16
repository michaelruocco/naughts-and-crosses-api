package uk.co.mruoc.nac.app;

import java.net.SocketAddress;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;
import uk.co.mruoc.nac.environment.integrated.cognito.DefaultCognitoTokenCredentials;
import uk.mruoc.nac.access.TokenCredentials;

public interface TestEnvironment {

  void startDependentServices();

  void stopDependentServices();

  int getAppPort();

  SocketAddress getAppSocketAddress();

  String[] getAppArgs();

  default NaughtsAndCrossesApiClient buildApiClient() {
    return buildApiClient(new DefaultCognitoTokenCredentials());
  }

  NaughtsAndCrossesApiClient buildApiClient(TokenCredentials credentials);

  NaughtsAndCrossesWebsocketClient buildWebsocketClient();
}
