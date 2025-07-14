package uk.co.mruoc.nac.user.cognito;

import java.time.Clock;
import java.util.Map;
import java.util.Objects;
import lombok.Builder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChallengeNameType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.usecases.AccessTokenClient;

@Builder
public class CognitoAccessTokenClient implements AccessTokenClient {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;
  private final String clientId;
  private final Clock clock;
  private final AccessTokenResponseFactory responseFactory;

  @Override
  public AccessTokenResponse create(CreateTokenRequest request) {
    InitiateAuthRequest authRequest =
        InitiateAuthRequest.builder()
            .clientId(clientId)
            .authParameters(toAuthParameters(request))
            .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
            .build();
    InitiateAuthResponse response = client.initiateAuth(authRequest);
    AuthenticationResultType result = response.authenticationResult();
    if (Objects.nonNull(result)) {
      return responseFactory.toResponse(result);
    }
    ChallengeNameType challengeName = response.challengeName();
    return AccessTokenResponse.builder()
        .challenge(challengeName.name())
        .session(response.session())
        .build();
  }

  @Override
  public AccessTokenResponse refresh(RefreshTokenRequest request) {
    AdminInitiateAuthRequest authRequest =
        AdminInitiateAuthRequest.builder()
            .clientId(clientId)
            .userPoolId(userPoolId)
            .authParameters(toAuthParameters(request))
            .authFlow(AuthFlowType.REFRESH_TOKEN)
            .build();
    AdminInitiateAuthResponse response = client.adminInitiateAuth(authRequest);
    AuthenticationResultType type = response.authenticationResult();
    return responseFactory.toResponse(type);
  }

  private static Map<String, String> toAuthParameters(CreateTokenRequest request) {
    return Map.of(
        "USERNAME", request.getUsername(),
        "PASSWORD", request.getPassword());
  }

  private static Map<String, String> toAuthParameters(RefreshTokenRequest request) {
    return Map.of("REFRESH_TOKEN", request.getRefreshToken());
  }
}
