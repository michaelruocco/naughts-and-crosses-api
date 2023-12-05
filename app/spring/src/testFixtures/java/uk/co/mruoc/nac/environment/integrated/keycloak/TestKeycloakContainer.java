package uk.co.mruoc.nac.environment.integrated.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;
import java.util.Objects;
import uk.mruoc.nac.access.AccessToken;
import uk.mruoc.nac.access.AccessTokenClient;
import uk.mruoc.nac.access.CacheAccessTokenClientDecorator;
import uk.mruoc.nac.access.RestAccessTokenClient;
import uk.mruoc.nac.access.RestAccessTokenConfig;

public class TestKeycloakContainer extends ExtendableKeycloakContainer<TestKeycloakContainer> {

  private AccessTokenClient client;

  public TestKeycloakContainer() {
    withRealmImportFile("keycloak/naughts-and-crosses-realm.json");
  }

  public String getIssuerUrl() {
    return String.format("%s/realms/naughts-and-crosses-local", getAuthServerUrl());
  }

  public String getAuthTokenValue() {
    return generateAccessToken().getValue();
  }

  private AccessToken generateAccessToken() {
    if (Objects.isNull(client)) {
      client = buildClient();
    }
    return client.generateToken();
  }

  private AccessTokenClient buildClient() {
    RestAccessTokenConfig config =
        RestAccessTokenConfig.builder()
            .clientId("naughts-and-crosses-api")
            .clientSecret("naughts-and-crosses-api-secret")
            .grantType("client_credentials")
            .tokenUrl(getTokenUrl())
            .build();
    return new CacheAccessTokenClientDecorator(new RestAccessTokenClient(config));
  }

  private String getTokenUrl() {
    return String.format("%s/protocol/openid-connect/token", getIssuerUrl());
  }
}
