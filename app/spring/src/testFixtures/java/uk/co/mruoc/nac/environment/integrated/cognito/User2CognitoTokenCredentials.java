package uk.co.mruoc.nac.environment.integrated.cognito;

import uk.co.mruoc.nac.client.TokenCredentials;

public class User2CognitoTokenCredentials implements TokenCredentials {

  @Override
  public String getUsername() {
    return "user-2";
  }

  @Override
  public String getPassword() {
    return "pwd2";
  }
}
