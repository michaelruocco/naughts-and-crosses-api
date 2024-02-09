package uk.co.mruoc.nac.app.config;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RelayConfig {

  private final String host;
  private final int port;
}
