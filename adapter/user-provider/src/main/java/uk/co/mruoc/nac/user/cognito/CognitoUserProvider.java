package uk.co.mruoc.nac.user.cognito;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.UserProvider;

@Slf4j
@Builder
public class CognitoUserProvider implements UserProvider {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;

  @Override
  public Optional<User> get(String id) {
    ListUsersRequest usersRequest =
        ListUsersRequest.builder()
            .userPoolId(userPoolId)
            .filter(String.format("sub = \"%s\"", id))
            .build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream().findFirst().map(CognitoUserProvider::toUser);
  }

  @Override
  public Stream<User> getAll() {
    ListUsersRequest usersRequest = ListUsersRequest.builder().userPoolId(userPoolId).build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream().map(CognitoUserProvider::toUser);
  }

  private static User toUser(UserType user) {
    Map<String, String> attributes = toMap(user.attributes());
    return User.builder()
        .id(attributes.get("sub"))
        .username(user.username())
        .firstName(attributes.get("given_name"))
        .lastName(attributes.get("family_name"))
        .email(attributes.get("email"))
        .build();
  }

  private static Map<String, String> toMap(List<AttributeType> attributes) {
    return attributes.stream().collect(Collectors.toMap(AttributeType::name, AttributeType::value));
  }
}
