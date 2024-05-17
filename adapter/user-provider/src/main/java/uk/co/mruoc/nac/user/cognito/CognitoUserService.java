package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.ExternalUserService;

@Slf4j
@Builder
public class CognitoUserService implements ExternalUserService {

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
    return response.users().stream().findFirst().map(CognitoUserService::toUser);
  }

  @Override
  public Stream<User> getAll() {
    ListUsersRequest usersRequest = ListUsersRequest.builder().userPoolId(userPoolId).build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream().map(CognitoUserService::toUser);
  }

  @Override
  public void create(Collection<CreateUserRequest> requests) {
    requests.forEach(this::create);
  }

  private void create(CreateUserRequest request) {
    AdminCreateUserRequest cognitoRequest =
        AdminCreateUserRequest.builder()
            .userPoolId(userPoolId)
            .username(request.getUsername())
            .userAttributes(toAttributes(request))
            .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
            .build();
    AdminCreateUserResponse response = client.adminCreateUser(cognitoRequest);
    User user = toUser(response.user());
    log.info("user {} created with subject {} {}", user.getUsername(), user.getId());
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

  private static Collection<AttributeType> toAttributes(CreateUserRequest request) {
    return List.of(
        toGivenNameAttribute(request.getFirstName()),
        toFamilyNameAttribute(request.getFirstName()),
        toEmailAttribute(request.getEmail()));
  }

  private static AttributeType toGivenNameAttribute(String value) {
    return AttributeType.builder().name("given_name").value(value).build();
  }

  private static AttributeType toFamilyNameAttribute(String value) {
    return AttributeType.builder().name("family_name").value(value).build();
  }

  private static AttributeType toEmailAttribute(String value) {
    return AttributeType.builder().name("email").value(value).build();
  }

  private static Map<String, String> toMap(List<AttributeType> attributes) {
    return attributes.stream().collect(Collectors.toMap(AttributeType::name, AttributeType::value));
  }
}
