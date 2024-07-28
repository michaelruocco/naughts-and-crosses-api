package uk.co.mruoc.nac.app.security;

import java.net.URI;
import java.util.Objects;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.usecases.AuthCodeClient;

@RequiredArgsConstructor
@Builder
public class SpringAuthCodeClient implements AuthCodeClient {

  private final URI uri;
  private final String clientId;
  private final RestTemplate template;

  @Override
  public TokenResponse create(AuthCodeRequest request) {
    HttpEntity<MultiValueMap<String, String>> entity = toRequestEntity(request);
    ResponseEntity<AuthCodeResponse> response =
        template.exchange(uri, HttpMethod.POST, entity, AuthCodeResponse.class);
    return toTokenResponse(Objects.requireNonNull(response.getBody()));
  }

  private HttpEntity<MultiValueMap<String, String>> toRequestEntity(AuthCodeRequest request) {
    return new HttpEntity<>(toRequestBody(request), buildRequestHeaders());
  }

  private MultiValueMap<String, String> toRequestBody(AuthCodeRequest request) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("code", request.getAuthCode());
    body.add("redirect_uri", request.getRedirectUri());
    return body;
  }

  private static HttpHeaders buildRequestHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    return headers;
  }

  private static TokenResponse toTokenResponse(AuthCodeResponse response) {
    AuthCodeResponse responseBody = Objects.requireNonNull(response);
    return TokenResponse.builder()
        .accessToken(responseBody.getAccessToken())
        .refreshToken(responseBody.getRefreshToken())
        .build();
  }
}
