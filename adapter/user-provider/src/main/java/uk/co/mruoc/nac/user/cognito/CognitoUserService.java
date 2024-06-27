package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.UserAlreadyExistsException;

@Slf4j
@Builder
public class CognitoUserService implements ExternalUserService {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;
  private final CognitoUserConverter converter;
  private final CognitoGroupService groupService;

  @Override
  public Stream<User> getAllUsers() {
    Map<String, Collection<String>> usernamesWithGroups = groupService.getUsernamesWithGroups();
    ListUsersRequest usersRequest = ListUsersRequest.builder().userPoolId(userPoolId).build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream().map(user -> converter.toUser(user, usernamesWithGroups));
  }

  @Override
  public Optional<User> getByUsername(String username) {
    ListUsersRequest usersRequest =
        ListUsersRequest.builder()
            .userPoolId(userPoolId)
            .filter(converter.toUsernameFilter(username))
            .build();
    ListUsersResponse response = client.listUsers(usersRequest);
    return response.users().stream()
        .findFirst()
        .map(user -> converter.toUser(user, groupService.getGroupsForUser(username)));
  }

  @Override
  public User create(UpsertUserRequest request) {
    try {
      log.info("attempting to create user {}", request.getUsername());
      AdminCreateUserRequest cognitoRequest =
          AdminCreateUserRequest.builder()
              .userPoolId(userPoolId)
              .username(request.getUsername())
              .userAttributes(converter.toAttributes(request))
              .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
              .build();
      AdminCreateUserResponse response = client.adminCreateUser(cognitoRequest);
      User user = converter.toUser(response.user(), request.getGroups());
      log.info("user {} created with id {}", user.getUsername(), user.getId());
      groupService.addUserToGroups(user.getUsername(), request.getGroups());
      return user;
    } catch (UsernameExistsException e) {
      throw new UserAlreadyExistsException(request.getUsername());
    }
  }

  @Override
  public void update(User user) {
    AdminUpdateUserAttributesRequest cognitoRequest =
        AdminUpdateUserAttributesRequest.builder()
            .userPoolId(userPoolId)
            .username(user.getUsername())
            .userAttributes(converter.toAttributes(user))
            .build();
    client.adminUpdateUserAttributes(cognitoRequest);
    groupService.updateUserGroups(user);
  }

  @Override
  public void delete(String username) {
    AdminDeleteUserRequest cognitoRequest =
        AdminDeleteUserRequest.builder().userPoolId(userPoolId).username(username).build();
    client.adminDeleteUser(cognitoRequest);
  }

  @Override
  public Collection<String> getAllGroups() {
    return groupService.getAllGroups();
  }
}
