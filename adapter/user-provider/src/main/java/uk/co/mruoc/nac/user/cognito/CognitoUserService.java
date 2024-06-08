package uk.co.mruoc.nac.user.cognito;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.ExternalUserService;

@Slf4j
@Builder
public class CognitoUserService implements ExternalUserService {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;
  private final CognitoUserConverter converter;

  @Override
  public Stream<User> getAll() {
    ListUsersRequest usersRequest = ListUsersRequest.builder().userPoolId(userPoolId).build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream().map(converter::toUser);
  }

  @Override
  public Optional<User> getById(String id) {
    ListUsersRequest usersRequest =
        ListUsersRequest.builder().userPoolId(userPoolId).filter(converter.toSubFilter(id)).build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream().findFirst().map(converter::toUser);
  }

  @Override
  public User create(CreateUserRequest request) {
    AdminCreateUserRequest cognitoRequest =
        AdminCreateUserRequest.builder()
            .userPoolId(userPoolId)
            .username(request.getUsername())
            .userAttributes(converter.toAttributes(request))
            .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
            .build();
    AdminCreateUserResponse response = client.adminCreateUser(cognitoRequest);
    User user = converter.toUser(response.user());
    log.info("user {} created with id {}", user.getUsername(), user.getId());
    return user;
  }
}
