package uk.co.mruoc.nac.app;

import java.net.SocketAddress;

public interface TestEnvironment {

  void startDependentServices();

  void stopDependentServices();

  int getAppPort();

  String getAppUrl();

  SocketAddress getAppSocketAddress();

  String[] getAppArgs();
}
