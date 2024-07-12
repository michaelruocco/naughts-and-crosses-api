package uk.co.mruoc.nac.user.cognito;

import java.time.Clock;
import java.util.Map;
import lombok.Builder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.usecases.TokenService;

@Builder
public class CognitoTokenService implements TokenService {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;
  private final String clientId;
  private final Clock clock;

  @Override
  public TokenResponse create(CreateTokenRequest request) {
    AdminInitiateAuthRequest authRequest =
        AdminInitiateAuthRequest.builder()
            .clientId(clientId)
            .userPoolId(userPoolId)
            .authParameters(toAuthParameters(request))
            .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
            .build();
    AdminInitiateAuthResponse response = client.adminInitiateAuth(authRequest);
    AuthenticationResultType type = response.authenticationResult();
    return toResponse(type);
  }

  @Override
  public TokenResponse refresh(RefreshTokenRequest request) {
    AdminInitiateAuthRequest authRequest =
        AdminInitiateAuthRequest.builder()
            .clientId(clientId)
            .userPoolId(userPoolId)
            .authParameters(toAuthParameters(request))
            .authFlow(AuthFlowType.REFRESH_TOKEN)
            .build();
    AdminInitiateAuthResponse response = client.adminInitiateAuth(authRequest);
    AuthenticationResultType type = response.authenticationResult();
    return toResponse(type);
  }

  private TokenResponse toResponse(AuthenticationResultType type) {
    return TokenResponse.builder()
        .accessToken(type.accessToken())
        .refreshToken(type.refreshToken())
        .build();
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
