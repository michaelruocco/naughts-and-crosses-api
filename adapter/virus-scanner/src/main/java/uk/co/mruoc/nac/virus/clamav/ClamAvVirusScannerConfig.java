package uk.co.mruoc.nac.virus.clamav;

import java.time.Duration;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClamAvVirusScannerConfig {

  private final String host;
  private final int port;
  private final Duration connectTimeout;
  private final Duration readTimeout;

  public int getReadTimeoutMillis() {
    return Math.toIntExact(readTimeout.toMillis());
  }

  public int getConnectTimeoutMillis() {
    return Math.toIntExact(readTimeout.toMillis());
  }
}
