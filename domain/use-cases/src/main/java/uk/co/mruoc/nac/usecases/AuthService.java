package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;

@Builder
public class AuthService {

  private final TokenService tokenService;
  private final AuthCodeClient authCodeClient;
  private final ExternalUserPresentRetry externalUserPresentRetry;
  private final ExternalUserSynchronizer synchronizer;

  public TokenResponse create(CreateTokenRequest request) {
    return tokenService.create(request);
  }

  public TokenResponse refresh(RefreshTokenRequest request) {
    return tokenService.refresh(request);
  }

  public TokenResponse create(AuthCodeRequest request) {
    TokenResponse response = authCodeClient.create(request);
    externalUserPresentRetry.waitUntilExternalUserPresent(response.getUsername());
    synchronizer.synchronizeIfNotPresent(response.getUsername());
    return response;
  }
}
