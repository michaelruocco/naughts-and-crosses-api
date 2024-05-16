package uk.co.mruoc.nac.user.cognito;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CognitoUserPoolAndClientIds {
  private final String poolId;
  private final String clientId;
}
