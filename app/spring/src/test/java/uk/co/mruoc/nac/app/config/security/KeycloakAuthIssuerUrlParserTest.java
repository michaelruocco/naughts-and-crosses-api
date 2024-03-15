package uk.co.mruoc.nac.app.config.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KeycloakAuthIssuerUrlParserTest {

  private static final String ISSUER_URL = "http://keycloak:4021/realms/naughts-and-crosses-local";

  @Test
  void shouldReturnSchemeHostAndPort() {
    String result = KeycloakAuthIssuerUrlParser.toSchemeHostAndPort(ISSUER_URL);

    assertThat(result).isEqualTo("http://keycloak:4021");
  }

  @Test
  void shouldReturnRealm() {
    String result = KeycloakAuthIssuerUrlParser.toRealm(ISSUER_URL);

    assertThat(result).isEqualTo("naughts-and-crosses-local");
  }
}
