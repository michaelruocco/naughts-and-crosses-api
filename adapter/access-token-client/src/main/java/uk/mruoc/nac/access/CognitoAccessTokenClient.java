package uk.mruoc.nac.access;

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

@Builder
public class CognitoAccessTokenClient implements AccessTokenClient {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;
  private final String clientId;
  private final Clock clock;

  @Override
  public AccessToken generateToken(TokenCredentials credentials) {
    AdminInitiateAuthRequest authRequest =
        AdminInitiateAuthRequest.builder()
            .clientId(clientId)
            .userPoolId(userPoolId)
            .authParameters(toAuthParameters(credentials))
            .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
            .build();
    AdminInitiateAuthResponse response = client.adminInitiateAuth(authRequest);
    AuthenticationResultType type = response.authenticationResult();
    return toAccessToken(type);
  }

  private AccessToken toAccessToken(AuthenticationResultType type) {
    Instant expiry =
        clock
            .instant()
            .plusSeconds(Optional.ofNullable(type.expiresIn()).orElse(Integer.MAX_VALUE));
    return AccessToken.builder().type("Bearer").value(type.accessToken()).expiry(expiry).build();
  }

  private static Map<String, String> toAuthParameters(TokenCredentials credentials) {
    return Map.of(
        "USERNAME", credentials.getUsername(),
        "PASSWORD", credentials.getPassword());
  }
}
