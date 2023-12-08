package uk.mruoc.nac.access;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RestAccessTokenConfig {

  private final String tokenUrl;
  private final String clientId;
  private final String clientSecret;
  private final String grantType;
}
