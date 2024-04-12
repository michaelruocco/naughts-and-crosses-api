package uk.co.mruoc.nac.app.config.websocket;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BrokerConfig {

  private final boolean inMemoryEnabled;
  private final boolean sslEnabled;
  private final String host;
  private final int port;
  private final String clientLogin;
  private final String clientPasscode;
  private final String systemLogin;
  private final String systemPasscode;
}
