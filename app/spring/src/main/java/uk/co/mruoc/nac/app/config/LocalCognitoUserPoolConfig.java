package uk.co.mruoc.nac.app.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import uk.co.mruoc.cognito.CognitoUserPoolConfig;

public class LocalCognitoUserPoolConfig implements CognitoUserPoolConfig {

  @Override
  public String getUserPoolName() {
    return "nac-user-pool";
  }

  @Override
  public String getUserPoolClientName() {
    return "nac-user-pool-client";
  }

  @Override
  public Collection<String> getGroups() {
    return Collections.emptyList();
  }

  @Override
  public Collection<UserParams> getUserParams() {
    return List.of(user1(), user2());
  }

  @Override
  public String getConfirmationCode() {
    return "9999";
  }

  private static UserParams user1() {
    return UserParams.builder()
        .subject("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .username("user-1")
        .password("pwd1")
        .givenName("User")
        .familyName("One")
        .email("user-1@email.com")
        .emailVerified(true)
        .groups(Collections.emptyList())
        .confirmed(true)
        .build();
  }

  private static UserParams user2() {
    return UserParams.builder()
        .subject("dadfde25-9924-4982-802d-dfd0bce2218d")
        .username("user-2")
        .password("pwd2")
        .givenName("User")
        .familyName("Two")
        .email("user-2@email.com")
        .emailVerified(true)
        .groups(Collections.emptyList())
        .confirmed(true)
        .build();
  }
}
