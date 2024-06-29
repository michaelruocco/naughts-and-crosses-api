package uk.co.mruoc.nac.environment.integrated.cognito;

import uk.co.mruoc.nac.client.TokenCredentials;

public class AdminCognitoTokenCredentials implements TokenCredentials {

  @Override
  public String getUsername() {
    return "admin";
  }

  @Override
  public String getPassword() {
    return "pwd";
  }
}
