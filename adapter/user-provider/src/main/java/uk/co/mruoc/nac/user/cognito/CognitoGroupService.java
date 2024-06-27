package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminRemoveUserFromGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GroupType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListGroupsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListGroupsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersInGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersInGroupResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.AdminListGroupsForUserIterable;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.ListGroupsIterable;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.ListUsersInGroupIterable;
import uk.co.mruoc.nac.entities.User;

@Slf4j
@Builder
public class CognitoGroupService {

  private final CognitoIdentityProviderClient client;
  private final String userPoolId;

  public Collection<String> getAllGroups() {
    ListGroupsRequest request = ListGroupsRequest.builder().userPoolId(userPoolId).build();
    ListGroupsIterable responses = client.listGroupsPaginator(request);
    return responses.stream()
        .map(ListGroupsResponse::groups)
        .flatMap(Collection::stream)
        .map(GroupType::groupName)
        .toList();
  }

  public Map<String, Collection<String>> getUsernamesWithGroups() {
    return getUsernamesWithGroups(getAllGroups());
  }

  public void updateUserGroups(User user) {
    Collection<String> existingGroups = getGroupsForUser(user.getUsername());

    Collection<String> groupsToAdd = CollectionUtils.subtract(user.getGroups(), existingGroups);
    addUserToGroups(user.getUsername(), groupsToAdd);

    Collection<String> groupsToRemove = CollectionUtils.subtract(existingGroups, user.getGroups());
    removeUserFromGroups(user.getUsername(), groupsToRemove);
  }

  public Collection<String> getGroupsForUser(String username) {
    AdminListGroupsForUserRequest request =
        AdminListGroupsForUserRequest.builder().userPoolId(userPoolId).username(username).build();
    AdminListGroupsForUserIterable responses = client.adminListGroupsForUserPaginator(request);
    return responses.stream()
        .map(AdminListGroupsForUserResponse::groups)
        .flatMap(Collection::stream)
        .map(GroupType::groupName)
        .toList();
  }

  public void addUserToGroups(String username, Collection<String> groups) {
    groups.forEach(group -> addUserToGroup(username, group));
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

  private void addUserToGroup(String username, String group) {
    AdminAddUserToGroupRequest request =
        AdminAddUserToGroupRequest.builder()
            .userPoolId(userPoolId)
            .username(username)
            .groupName(group)
            .build();
    client.adminAddUserToGroup(request);
    log.info("added user {} to group {}", username, group);
  }

  private void removeUserFromGroups(String username, Collection<String> groups) {
    groups.forEach(group -> removeUserFromGroup(username, group));
  }

  private void removeUserFromGroup(String username, String group) {
    AdminRemoveUserFromGroupRequest request =
        AdminRemoveUserFromGroupRequest.builder()
            .userPoolId(userPoolId)
            .username(username)
            .groupName(group)
            .build();
    client.adminRemoveUserFromGroup(request);
    log.info("added user {} to group {}", username, group);
  }
}
