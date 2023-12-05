package uk.co.mruoc.nac.environment.integrated.keycloak;

import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;

public class TestKeycloakContainer extends ExtendableKeycloakContainer<TestKeycloakContainer> {

  public TestKeycloakContainer() {
    withRealmImportFile("keycloak/naughts-and-crosses-realm.json");
  }

  public String getIssuerUrl() {
    return String.format("%s/realms/naughts-and-crosses-local", getAuthServerUrl());
  }

  public String getTokenUrl() {
    return String.format("%s/protocol/openid-connect/token", getIssuerUrl());
  }
}
