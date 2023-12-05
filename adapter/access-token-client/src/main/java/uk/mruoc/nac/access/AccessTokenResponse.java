package uk.mruoc.nac.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(force = true)
@Builder
@RequiredArgsConstructor
@Data
public class AccessTokenResponse {

  @JsonProperty("token_type")
  private final String type;

  @JsonProperty("access_token")
  private final String value;

  @JsonProperty("expires_in")
  private final long expiresIn;
}
