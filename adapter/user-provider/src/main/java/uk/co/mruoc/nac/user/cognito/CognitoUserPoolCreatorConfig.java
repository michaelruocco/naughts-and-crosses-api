package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Builder
@Data
public class CognitoUserPoolCreatorConfig {

  private final CognitoIdentityProviderClient client;
  private final String userPoolName;
  private final String userPoolClientName;
  private final Collection<UserParams> userParams;
  private final String confirmationCode;
  private final String poolIdPlaceHolder;

  public Collection<String> getGroups() {
    return userParams.stream()
        .map(UserParams::getGroups)
        .flatMap(Collection::stream)
        .distinct()
        .toList();
  }

  public static class CognitoUserPoolCreatorConfigBuilder {
    // intentionally blank to resolve javadoc creation issues for lombok builder
  }

  @Builder
  @Data
  public static class UserParams {
    private final String username;
    private final String password;
    private final String subject;
    private final String givenName;
    private final String familyName;
    private final String email;
    private final boolean emailVerified;
    private final Collection<String> groups;
  }
}
