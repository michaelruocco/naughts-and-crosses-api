package uk.co.mruoc.nac.client;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RequiredArgsConstructor
public class HttpEntityFactory {

  private final HttpHeaders headers;

  public HttpEntityFactory(String token) {
    this(buildHeaders(token));
  }

  public <T> HttpEntity<T> buildRequest(T body) {
    return new HttpEntity<>(body, headers);
  }

  public HttpEntity<Void> buildRequest() {
    return new HttpEntity<>(null, headers);
  }

  private static HttpHeaders buildHeaders(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    Optional.ofNullable(token).ifPresent(headers::setBearerAuth);
    return headers;
  }
}
