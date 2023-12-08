package uk.mruoc.nac.access;

import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class RestAccessTokenClient implements AccessTokenClient {

  private final URI uri;
  private final HttpEntity<MultiValueMap<String, String>> request;
  private final RestTemplate restTemplate;
  private final Clock clock;

  public RestAccessTokenClient(RestAccessTokenConfig config) {
    this(config, new RestTemplate());
  }

  public RestAccessTokenClient(RestAccessTokenConfig config, RestTemplate restTemplate) {
    this(URI.create(config.getTokenUrl()), toRequest(config), restTemplate, Clock.systemUTC());
  }

  @Override
  public AccessToken generateToken() {
    var start = Instant.now();
    try {
      ResponseEntity<AccessTokenResponse> response =
          restTemplate.postForEntity(uri, request, AccessTokenResponse.class);
      if (response.getBody() == null) {
        throw new AccessTokenClientException("cop keycloak returned null response");
      }
      return toAccessToken(response.getBody());
    } catch (RestClientException e) {
      throw new AccessTokenClientException("error calling cop keycloak access token", e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("took {}ms to generate access token", duration.toMillis());
    }
  }

  private AccessToken toAccessToken(AccessTokenResponse response) {
    Instant expiry = clock.instant().plusSeconds(response.getExpiresIn());
    return AccessToken.builder()
        .type(response.getType())
        .value(response.getValue())
        .expiry(expiry)
        .build();
  }

  private static HttpEntity<MultiValueMap<String, String>> toRequest(RestAccessTokenConfig config) {
    String authorizationHeaderValue = toBasicAuthHeaderValue(config);
    MultiValueMap<String, String> body = toBody(config.getGrantType());
    HttpHeaders headers = toHeaders(authorizationHeaderValue);
    return new HttpEntity<>(body, headers);
  }

  private static String toBasicAuthHeaderValue(RestAccessTokenConfig config) {
    var auth = String.format("%s:%s", config.getClientId(), config.getClientSecret());
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
    return String.format("Basic %s", new String(encodedAuth));
  }

  private static MultiValueMap<String, String> toBody(String grantType) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", grantType);
    log.debug("generating token with grant type {}", grantType);
    return body;
  }

  private static HttpHeaders toHeaders(String authorizationHeaderValue) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("Authorization", authorizationHeaderValue);
    return headers;
  }
}
