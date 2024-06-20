package uk.co.mruoc.nac.client;

import static org.springframework.http.HttpMethod.POST;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenResponse;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesApiTokenClient {

  private final String baseUrl;
  private final RestTemplate template;

  public NaughtsAndCrossesApiTokenClient(String baseUrl) {
    this(baseUrl, new RestTemplate());
  }

  public ApiCreateTokenResponse createToken(TokenCredentials credentials) {
    ApiCreateTokenRequest request =
        ApiCreateTokenRequest.builder()
            .username(credentials.getUsername())
            .password(credentials.getPassword())
            .build();
    return createToken(request);
  }

  public ApiCreateTokenResponse createToken(ApiCreateTokenRequest request) {
    var url = String.format("%s/v1/tokens", baseUrl);
    var entity = toHttpEntity(request);
    var response = template.exchange(url, POST, entity, ApiCreateTokenResponse.class);
    return toBodyIfNotNull(response);
  }

  private static ApiCreateTokenResponse toBodyIfNotNull(
      HttpEntity<ApiCreateTokenResponse> response) {
    return Optional.ofNullable(response)
        .map(HttpEntity::getBody)
        .orElseThrow(
            () -> new NaughtsAndCrossesApiClientException("null response body returned from api"));
  }

  private static HttpEntity<ApiCreateTokenRequest> toHttpEntity(ApiCreateTokenRequest request) {
    return new HttpEntity<>(request, buildHeaders());
  }

  private static HttpHeaders buildHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
