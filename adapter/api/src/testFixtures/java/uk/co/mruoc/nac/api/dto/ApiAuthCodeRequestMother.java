package uk.co.mruoc.nac.api.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiAuthCodeRequest.ApiAuthCodeRequestBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiAuthCodeRequestMother {

  public static ApiAuthCodeRequest build() {
    return builder().build();
  }

  public static ApiAuthCodeRequestBuilder builder() {
    return ApiAuthCodeRequest.builder()
        .authCode("1b50c4fa-1f74-44f9-800a-995793dc2595")
        .redirectUri("http://localhost:3001/login/callback");
  }
}
