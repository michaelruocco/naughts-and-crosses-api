package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.CreateTokenResponse;

public class ApiTokenConverter {

  public CreateTokenRequest toRequest(ApiCreateTokenRequest apiRequest) {
    return CreateTokenRequest.builder()
        .username(apiRequest.getUsername())
        .password(apiRequest.getPassword())
        .build();
  }

  public ApiCreateTokenResponse toApiResponse(CreateTokenResponse response) {
    return ApiCreateTokenResponse.builder()
        .accessToken(response.getAccessToken())
        .expiry(response.getExpiry())
        .build();
  }
}
