package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GroupType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListGroupsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListGroupsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersInGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersInGroupResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.AdminListGroupsForUserIterable;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.ListGroupsIterable;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.ListUsersInGroupIterable;
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
  public Stream<User> getAllUsers() {
    Map<String, Collection<String>> usernamesWithGroups = getUsernamesWithGroups();
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
    Collection<String> groups = getGroupsForUser(username);
    return response.users().stream().findFirst().map(user -> converter.toUser(user, groups));
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
    User user = converter.toUser(response.user(), request.getGroups());
    log.info("user {} created with id {}", user.getUsername(), user.getId());
    return user;
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
  }

  @Override
  public void delete(String username) {
    AdminDeleteUserRequest cognitoRequest =
        AdminDeleteUserRequest.builder().userPoolId(userPoolId).username(username).build();
    client.adminDeleteUser(cognitoRequest);
  }

  @Override
  public Collection<String> getAllGroups() {
    ListGroupsRequest request = ListGroupsRequest.builder().userPoolId(userPoolId).build();
    ListGroupsIterable responses = client.listGroupsPaginator(request);
    return responses.stream()
        .map(ListGroupsResponse::groups)
        .flatMap(Collection::stream)
        .map(GroupType::groupName)
        .toList();
  }

  private Map<String, Collection<String>> getUsernamesWithGroups() {
    return getUsernamesWithGroups(getAllGroups());
  }

  private Map<String, Collection<String>> getUsernamesWithGroups(Collection<String> groups) {
    Map<String, Collection<String>> usernamesAndGroups = new HashMap<>();
    for (String group : groups) {
      Collection<String> usernames = getGroupUsernames(group);
      for (String username : usernames) {
        Collection<String> userGroups = usernamesAndGroups.getOrDefault(username, new HashSet<>());
        userGroups.add(group);
        usernamesAndGroups.put(username, userGroups);
      }
    }
    return usernamesAndGroups;
  }

  private Collection<String> getGroupUsernames(String group) {
    ListUsersInGroupRequest request =
        ListUsersInGroupRequest.builder().userPoolId(userPoolId).groupName(group).build();
    ListUsersInGroupIterable responses = client.listUsersInGroupPaginator(request);
    return responses.stream()
        .map(ListUsersInGroupResponse::users)
        .flatMap(Collection::stream)
        .map(UserType::username)
        .toList();
  }

  private Collection<String> getGroupsForUser(String username) {
    AdminListGroupsForUserRequest request =
        AdminListGroupsForUserRequest.builder().userPoolId(userPoolId).username(username).build();
    AdminListGroupsForUserIterable responses = client.adminListGroupsForUserPaginator(request);
    return responses.stream()
        .map(AdminListGroupsForUserResponse::groups)
        .flatMap(Collection::stream)
        .map(GroupType::groupName)
        .toList();
  }
}
