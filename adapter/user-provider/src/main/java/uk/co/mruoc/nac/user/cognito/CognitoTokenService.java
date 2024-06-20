package uk.co.mruoc.nac.user.cognito;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.CreateTokenResponse;
import uk.co.mruoc.nac.usecases.TokenService;

@Builder
public class CognitoTokenService implements TokenService {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;
  private final String clientId;
  private final Clock clock;

  @Override
  public CreateTokenResponse create(CreateTokenRequest request) {
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

  private CreateTokenResponse toResponse(AuthenticationResultType type) {
    return CreateTokenResponse.builder()
        .accessToken(type.accessToken())
        .expiry(toExpiry(type))
        .build();
  }

  private Instant toExpiry(AuthenticationResultType type) {
    return clock
        .instant()
        .plusSeconds(Optional.ofNullable(type.expiresIn()).orElse(Integer.MAX_VALUE));
  }

  private static Map<String, String> toAuthParameters(CreateTokenRequest request) {
    return Map.of(
        "USERNAME", request.getUsername(),
        "PASSWORD", request.getPassword());
  }
}
