package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.RespondToChallengeRequest;
import uk.co.mruoc.nac.entities.VerifySoftwareTokenRequest;

public interface SoftwareTokenClient {

  String create(String accessToken);

  void verify(VerifySoftwareTokenRequest request);

  void update(MfaPreferences preferences);

  AccessTokenResponse respondToChallenge(RespondToChallengeRequest request);
}
