package uk.co.mruoc.nac.user.inmemory;

import lombok.Builder;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.RespondToChallengeRequest;
import uk.co.mruoc.nac.entities.VerifySoftwareTokenRequest;
import uk.co.mruoc.nac.usecases.RespondToChallengeFailedException;
import uk.co.mruoc.nac.usecases.SoftwareTokenClient;
import uk.co.mruoc.nac.usecases.VerifySoftwareTokenFailedException;

@Builder
public class StubSoftwareTokenClient implements SoftwareTokenClient {

  private final StubUserTokenConfigs userConfigs;
  private final StubRefreshTokens refreshTokens;
  private final StubAccessTokenFactory tokenFactory;

  @Override
  public String create(String accessToken) {
    return "FQ5SITPAZVTP34BMSTAU3XCBTAXJXVFDEAHFJKR47ZKEFAPURC5Q";
  }

  @Override
  public void verify(VerifySoftwareTokenRequest request) {
    String code = request.getUserCode();
    if (isInvalid(code)) {
      throw new VerifySoftwareTokenFailedException(toInvalidCodeMessage(code));
    }
  }

  @Override
  public void update(MfaPreferences preferences) {
    // TODO add mfa settings to stub user config and update here
  }

  @Override
  public AccessTokenResponse respondToChallenge(RespondToChallengeRequest request) {
    String code = request.getUserCode();
    if (isInvalid(code)) {
      throw new RespondToChallengeFailedException(toInvalidCodeMessage(code));
    }
    String username = request.getUsername();
    StubUserTokenConfig config =
        userConfigs
            .getByUsername(username)
            .orElseThrow(() -> new RespondToChallengeFailedException(username));
    AccessTokenResponse response = tokenFactory.toAccessAndRefreshToken(config);
    refreshTokens.add(response.getRefreshToken(), config);
    return response;
  }

  private static boolean isInvalid(String code) {
    return "999999".equals(code);
  }

  private static String toInvalidCodeMessage(String code) {
    return String.format("invalid stub user code %s", code);
  }
}
