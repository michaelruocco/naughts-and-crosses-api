package uk.co.mruoc.nac.environment.integrated.keycloak;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

import java.util.Objects;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import uk.mruoc.nac.access.AccessToken;
import uk.mruoc.nac.access.AccessTokenClient;
import uk.mruoc.nac.access.CacheAccessTokenClientDecorator;
import uk.mruoc.nac.access.RestAccessTokenClient;
import uk.mruoc.nac.access.RestAccessTokenConfig;

public class TestKeycloakContainer extends GenericContainer<TestKeycloakContainer> {

  private static final int HTTP_PORT = 8080;
  private AccessTokenClient client;

  public TestKeycloakContainer() {
    super(DockerImageName.parse("quay.io/keycloak/keycloak:latest"));
    withCommand("start-dev", "--import-realm");
    withExposedPorts(HTTP_PORT);
    withFileCopiedToContainer("naughts-and-crosses-realm.json");
    waitingFor(StartupLogMessageWaitStrategyFactory.build());
  }

  public String getIssuerUrl() {
    return String.format(
        "http://%s:%d/realms/naughts-and-crosses-local", getHost(), getMappedPort(HTTP_PORT));
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

  private void withFileCopiedToContainer(String filename) {
    String classpathPath = String.format("keycloak/%s", filename);
    String containerPath = String.format("/opt/keycloak/data/import/%s", filename);
    withCopyToContainer(forClasspathResource(classpathPath), containerPath);
  }
}
