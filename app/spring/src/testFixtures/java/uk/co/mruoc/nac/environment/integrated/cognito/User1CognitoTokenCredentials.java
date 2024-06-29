package uk.co.mruoc.nac.environment.integrated.cognito;

import uk.co.mruoc.nac.client.TokenCredentials;

public class User1CognitoTokenCredentials implements TokenCredentials {

  @Override
  public String getUsername() {
    return "user-1";
  }

  @Override
  public String getPassword() {
    return "pwd1";
  }
}
