package uk.co.mruoc.nac.user.cognito;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.user.JwtParser;

@RequiredArgsConstructor
public class AccessTokenResponseFactory {

  private final JwtParser jwtParser;

  public AccessTokenResponse toResponse(AuthenticationResultType type) {
    String accessToken = type.accessToken();
    return AccessTokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(type.refreshToken())
        .username(jwtParser.toUsername(accessToken))
        .build();
  }
}
