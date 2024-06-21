package uk.co.mruoc.nac.client;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class HttpEntityFactory {

  private final String token;

  public <T> HttpEntity<T> buildRequest(T body) {
    return new HttpEntity<>(body, buildHeaders(token));
  }

  public HttpEntity<MultiValueMap<String, Object>> buildRequest(Resource resource) {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("data", resource);
    return new HttpEntity<>(body, buildHeaders(token, MediaType.MULTIPART_FORM_DATA));
  }

  public HttpEntity<Void> buildRequest() {
    return new HttpEntity<>(null, buildHeaders(token));
  }

  private static HttpHeaders buildHeaders(String token) {
    return buildHeaders(token, MediaType.APPLICATION_JSON);
  }

  private static HttpHeaders buildHeaders(String token, MediaType mediaType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(mediaType);
    Optional.ofNullable(token).ifPresent(headers::setBearerAuth);
    return headers;
  }
}
