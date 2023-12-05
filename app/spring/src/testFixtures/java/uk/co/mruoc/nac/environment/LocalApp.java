package uk.co.mruoc.nac.environment;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LocalApp {

  private final String host;
  private final int port;

  public LocalApp() {
    this("localhost", AvailablePortFinder.findAvailableTcpPort());
  }

  public SocketAddress getSocketAddress() {
    return new InetSocketAddress(host, port);
  }

  public String getUrl() {
    return String.format("http://%s:%d", host, port);
  }

  public String getServerPortArg() {
    return String.format("--server.port=%d", port);
  }
}
