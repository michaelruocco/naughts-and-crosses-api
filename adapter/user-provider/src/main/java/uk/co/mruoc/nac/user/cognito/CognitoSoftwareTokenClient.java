package uk.co.mruoc.nac.user.cognito;

import java.util.Map;
import lombok.Builder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserMfaPreferenceRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserMfaPreferenceResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AssociateSoftwareTokenRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AssociateSoftwareTokenResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.RespondToAuthChallengeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.RespondToAuthChallengeResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SoftwareTokenMfaSettingsType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.VerifySoftwareTokenResponse;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.MfaSetting;
import uk.co.mruoc.nac.entities.RespondToChallengeRequest;
import uk.co.mruoc.nac.entities.VerifySoftwareTokenRequest;
import uk.co.mruoc.nac.usecases.RespondToChallengeFailedException;
import uk.co.mruoc.nac.usecases.SetMfaPreferenceFailedException;
import uk.co.mruoc.nac.usecases.SoftwareTokenClient;
import uk.co.mruoc.nac.usecases.VerifySoftwareTokenFailedException;

@Builder
public class CognitoSoftwareTokenClient implements SoftwareTokenClient {

  private final CognitoIdentityProviderClient client;
  private final String clientId;
  private final String userPoolId;
  private final AccessTokenResponseFactory responseFactory;

  @Override
  public String create(String accessToken) {
    var cognitoRequest = AssociateSoftwareTokenRequest.builder().accessToken(accessToken).build();
    AssociateSoftwareTokenResponse response = client.associateSoftwareToken(cognitoRequest);
    return response.secretCode();
  }

  @Override
  public void verify(VerifySoftwareTokenRequest request) {
    var cognitoRequest =
        software.amazon.awssdk.services.cognitoidentityprovider.model.VerifySoftwareTokenRequest
            .builder()
            .accessToken(request.getAccessToken())
            .userCode(request.getUserCode())
            .build();
    VerifySoftwareTokenResponse response = client.verifySoftwareToken(cognitoRequest);
    String status = response.statusAsString();
    if ("SUCCESS".equals(status)) {
      return;
    }
    throw new VerifySoftwareTokenFailedException(status);
  }

  @Override
  public void update(MfaPreferences preferences) {
    String username = preferences.getUsername();
    AdminSetUserMfaPreferenceRequest request =
        AdminSetUserMfaPreferenceRequest.builder()
            .userPoolId(userPoolId)
            .username(username)
            .softwareTokenMfaSettings(
                toSoftwareTokenSettings(preferences.getSoftwareTokenSetting()))
            .build();
    AdminSetUserMfaPreferenceResponse response = client.adminSetUserMFAPreference(request);
    if (!response.sdkHttpResponse().isSuccessful()) {
      throw new SetMfaPreferenceFailedException(username);
    }
  }

  @Override
  public AccessTokenResponse respondToChallenge(RespondToChallengeRequest request) {
    try {
      RespondToAuthChallengeRequest cognitoRequest =
          RespondToAuthChallengeRequest.builder()
              .clientId(clientId)
              .session(request.getSession())
              .challengeName(request.getChallenge())
              .challengeResponses(toChallengeResponses(request))
              .build();
      RespondToAuthChallengeResponse response = client.respondToAuthChallenge(cognitoRequest);
      AuthenticationResultType type = response.authenticationResult();
      return responseFactory.toResponse(type);
    } catch (CognitoIdentityProviderException e) {
      throw new RespondToChallengeFailedException(e);
    }
  }

  private SoftwareTokenMfaSettingsType toSoftwareTokenSettings(MfaSetting setting) {
    return SoftwareTokenMfaSettingsType.builder()
        .preferredMfa(setting.isPreferred())
        .enabled(setting.isEnabled())
        .build();
  }

  private static Map<String, String> toChallengeResponses(RespondToChallengeRequest request) {
    String userCodeKey = String.format("%s_CODE", request.getChallenge());
    return Map.of("USERNAME", request.getUsername(), userCodeKey, request.getUserCode());
  }
}
