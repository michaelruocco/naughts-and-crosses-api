package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiRefreshTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;

public class ApiTokenConverter {

  public CreateTokenRequest toRequest(ApiCreateTokenRequest apiRequest) {
    return CreateTokenRequest.builder()
        .username(apiRequest.getUsername())
        .password(apiRequest.getPassword())
        .build();
  }

  public ApiTokenResponse toApiResponse(TokenResponse response) {
    return ApiTokenResponse.builder()
        .accessToken(response.getAccessToken())
        .refreshToken(response.getRefreshToken())
        .build();
  }

  public RefreshTokenRequest toRequest(ApiRefreshTokenRequest apiRequest) {
    return new RefreshTokenRequest(apiRequest.getRefreshToken());
  }
}
