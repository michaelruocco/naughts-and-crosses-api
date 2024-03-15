package uk.co.mruoc.nac.user.keycloak;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KeycloakAdminConfig {

  private final String url;
  private final String realm;
  private final String clientId;
  private final String clientSecret;

  public String getGrantType() {
    return CLIENT_CREDENTIALS;
  }
}
