package uk.co.mruoc.nac.environment.minimal;

import java.net.SocketAddress;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.TestEnvironment;
import uk.co.mruoc.nac.environment.LocalApp;

@RequiredArgsConstructor
@Slf4j
public class MinimalTestEnvironment implements TestEnvironment {

  private final LocalApp localApp;

  public MinimalTestEnvironment() {
    this(new LocalApp());
  }

  @Override
  public void startDependentServices() {
    log.info("no dependent services to start");
  }

  @Override
  public void stopDependentServices() {
    log.info("no dependent services to stop");
  }

  @Override
  public int getAppPort() {
    return localApp.getPort();
  }

  @Override
  public String getAppUrl() {
    return localApp.getUrl();
  }

  @Override
  public SocketAddress getAppSocketAddress() {
    return localApp.getSocketAddress();
  }

  @Override
  public String[] getAppArgs() {
    return new MinimalTestEnvironmentArgsDecorator()
        .apply(Stream.of(localApp.getServerPortArg()))
        .toArray(String[]::new);
  }
}
