package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiAuthCodeRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiRefreshTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiRespondToChallengeRequest;
import uk.co.mruoc.nac.api.dto.ApiTokenResponse;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.RespondToChallengeRequest;

public class ApiAuthConverter {

  public CreateTokenRequest toRequest(ApiCreateTokenRequest apiRequest) {
    return CreateTokenRequest.builder()
        .username(apiRequest.getUsername())
        .password(apiRequest.getPassword())
        .build();
  }

  public ApiTokenResponse toApiResponse(AccessTokenResponse response) {
    return ApiTokenResponse.builder()
        .accessToken(response.getAccessToken())
        .refreshToken(response.getRefreshToken())
        .username(response.getUsername())
        .challenge(response.getChallenge())
        .session(response.getSession())
        .build();
  }

  public RefreshTokenRequest toRequest(ApiRefreshTokenRequest apiRequest) {
    return new RefreshTokenRequest(apiRequest.getRefreshToken());
  }

  public AuthCodeRequest toRequest(ApiAuthCodeRequest apiRequest) {
    return AuthCodeRequest.builder()
        .authCode(apiRequest.getAuthCode())
        .redirectUri(apiRequest.getRedirectUri())
        .build();
  }

  public RespondToChallengeRequest toRequest(ApiRespondToChallengeRequest apiRequest) {
    return RespondToChallengeRequest.builder()
        .challenge(apiRequest.getChallenge())
        .session(apiRequest.getSession())
        .username(apiRequest.getUsername())
        .userCode(apiRequest.getUserCode())
        .build();
  }
}
