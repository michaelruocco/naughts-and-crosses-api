package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.RespondToChallengeRequest;

@Builder
public class AuthService {

  private final AccessTokenClient accessTokenClient;
  private final SoftwareTokenClient softwareTokenClient;
  private final AuthCodeClient authCodeClient;
  private final ExternalUserPresentRetry externalUserPresentRetry;
  private final ExternalUserSynchronizer synchronizer;

  public AccessTokenResponse create(CreateTokenRequest request) {
    return accessTokenClient.create(request);
  }

  public AccessTokenResponse refresh(RefreshTokenRequest request) {
    return accessTokenClient.refresh(request);
  }

  public AccessTokenResponse create(AuthCodeRequest request) {
    AccessTokenResponse response = authCodeClient.create(request);
    externalUserPresentRetry.waitUntilExternalUserPresent(response.getUsername());
    synchronizer.synchronizeIfNotPresent(response.getUsername());
    return response;
  }

  public AccessTokenResponse respondToAuthChallenge(RespondToChallengeRequest request) {
    return softwareTokenClient.respondToChallenge(request);
  }
}
