package uk.co.mruoc.nac.app.config.security;

import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeycloakAuthIssuerUrlParser {

  public static String toSchemeHostAndPort(String issuerUrl) {
    URI uri = URI.create(issuerUrl);
    return String.format("%s://%s:%d", uri.getScheme(), uri.getHost(), uri.getPort());
  }

  public static String toRealm(String issuerUrl) {
    URI uri = URI.create(issuerUrl);
    String path = uri.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }
}
