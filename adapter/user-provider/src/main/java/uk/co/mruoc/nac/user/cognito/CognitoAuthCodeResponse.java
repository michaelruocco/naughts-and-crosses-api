package uk.co.mruoc.nac.user.cognito;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Builder
@Data
public class CognitoAuthCodeResponse {

  @JsonProperty("access_token")
  private final String accessToken;

  @JsonProperty("id")
  private final String idToken;

  @JsonProperty("refresh")
  private final String refreshToken;

  @JsonProperty("token_type")
  private final String tokenType;

  @JsonProperty("expires_in")
  private final int expiresIn;
}
