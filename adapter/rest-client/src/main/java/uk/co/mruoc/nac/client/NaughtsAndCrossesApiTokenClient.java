package uk.co.mruoc.nac.client;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiRefreshTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiTokenResponse;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesApiTokenClient {

  private final String baseUrl;
  private final RestTemplate template;

  public NaughtsAndCrossesApiTokenClient(String baseUrl) {
    this(baseUrl, new RestTemplate());
  }

  public ApiTokenResponse createToken(TokenCredentials credentials) {
    ApiCreateTokenRequest request =
        ApiCreateTokenRequest.builder()
            .username(credentials.getUsername())
            .password(credentials.getPassword())
            .build();
    return createToken(request);
  }

  public ApiTokenResponse refreshToken(String refreshToken) {
    return refreshToken(new ApiRefreshTokenRequest(refreshToken));
  }

  private ApiTokenResponse createToken(ApiCreateTokenRequest request) {
    var entity = toHttpEntity(request);
    return performUpdate(POST, entity);
  }

  private ApiTokenResponse refreshToken(ApiRefreshTokenRequest request) {
    var entity = toHttpEntity(request);
    return performUpdate(PUT, entity);
  }

  private ApiTokenResponse performUpdate(HttpMethod method, HttpEntity<?> request) {
    try {
      var uri = String.format("%s/v1/tokens", baseUrl);
      ResponseEntity<ApiTokenResponse> response =
          template.exchange(uri, method, request, ApiTokenResponse.class);
      return toBodyIfNotNull(response);
    } catch (HttpClientErrorException e) {
      throw new NaughtsAndCrossesApiClientException(e.getMessage());
    }
  }

  private static ApiTokenResponse toBodyIfNotNull(HttpEntity<ApiTokenResponse> response) {
    return Optional.ofNullable(response)
        .map(HttpEntity::getBody)
        .orElseThrow(
            () -> new NaughtsAndCrossesApiClientException("null response body returned from api"));
  }

  private static <T> HttpEntity<T> toHttpEntity(T request) {
    return new HttpEntity<>(request, buildHeaders());
  }

  private static HttpHeaders buildHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
