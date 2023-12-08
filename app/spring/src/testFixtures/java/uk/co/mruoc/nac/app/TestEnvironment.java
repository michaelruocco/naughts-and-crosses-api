package uk.co.mruoc.nac.app;

import java.net.SocketAddress;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesWebsocketClient;

public interface TestEnvironment {

  void startDependentServices();

  void stopDependentServices();

  int getAppPort();

  SocketAddress getAppSocketAddress();

  String[] getAppArgs();

  NaughtsAndCrossesApiClient buildApiClient();

  NaughtsAndCrossesWebsocketClient buildWebsocketClient();
}
