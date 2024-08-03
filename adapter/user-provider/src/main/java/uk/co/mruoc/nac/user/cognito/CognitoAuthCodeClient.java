package uk.co.mruoc.nac.user.cognito;

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
import uk.co.mruoc.nac.user.JwtParser;

@RequiredArgsConstructor
@Builder
public class CognitoAuthCodeClient implements AuthCodeClient {

  private final URI uri;
  private final String clientId;
  private final RestTemplate template;
  private final JwtParser jwtParser;

  @Override
  public TokenResponse create(AuthCodeRequest request) {
    HttpEntity<MultiValueMap<String, String>> entity = toRequestEntity(request);
    ResponseEntity<CognitoAuthCodeResponse> response =
        template.exchange(uri, HttpMethod.POST, entity, CognitoAuthCodeResponse.class);
    return toTokenResponse(Objects.requireNonNull(response.getBody()));
  }

  private TokenResponse toTokenResponse(CognitoAuthCodeResponse response) {
    CognitoAuthCodeResponse responseBody = Objects.requireNonNull(response);
    String accessToken = response.getAccessToken();
    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(responseBody.getRefreshToken())
        .username(jwtParser.toUsername(accessToken))
        .build();
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
}
