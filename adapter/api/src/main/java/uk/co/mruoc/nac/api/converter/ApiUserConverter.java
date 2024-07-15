package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.dto.ApiUserPage;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserPage;
import uk.co.mruoc.nac.entities.UserPageRequest;

@RequiredArgsConstructor
public class ApiUserConverter {

  private final ApiPageRequestConverter pageConverter;

  public ApiUserConverter() {
    this(new ApiPageRequestConverter());
  }

  public UserPageRequest toRequest(ApiUserPageRequest apiRequest) {
    return UserPageRequest.builder()
        .page(pageConverter.toRequest(apiRequest.getPage()))
        .groups(apiRequest.getGroups())
        .build();
  }

  public ApiUserPage toApiUserPage(UserPage page) {
    return ApiUserPage.builder().total(page.getTotal()).items(toApiUsers(page.getItems())).build();
  }

  public Collection<ApiUser> toApiUsers(Collection<User> users) {
    return users.stream().map(this::toApiUser).toList();
  }

  public ApiUser toApiUser(User user) {
    return ApiUser.builder()
        .id(user.getId())
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .emailVerified(user.isEmailVerified())
        .status(user.getStatus())
        .groups(user.getGroups())
        .build();
  }

  public UpsertUserRequest toCreateUserRequest(ApiCreateUserRequest apiRequest) {
    return UpsertUserRequest.builder()
        .username(apiRequest.getUsername())
        .firstName(apiRequest.getFirstName())
        .lastName(apiRequest.getLastName())
        .email(apiRequest.getEmail())
        .emailVerified(apiRequest.isEmailVerified())
        .groups(apiRequest.getGroups())
        .build();
  }

  public Collection<ApiCreateUserRequest> toApiCreateUserRequests(
      Collection<UpsertUserRequest> requests) {
    return requests.stream().map(this::toApiCreateUserRequest).toList();
  }

  public ApiCreateUserRequest toApiCreateUserRequest(UpsertUserRequest request) {
    return ApiCreateUserRequest.builder()
        .username(request.getUsername())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .emailVerified(request.isEmailVerified())
        .groups(request.getGroups())
        .build();
  }

  public UpsertUserRequest toUpsertUserRequest(String username, ApiUpdateUserRequest apiRequest) {
    return UpsertUserRequest.builder()
        .username(username)
        .firstName(apiRequest.getFirstName())
        .lastName(apiRequest.getLastName())
        .email(apiRequest.getEmail())
        .emailVerified(apiRequest.isEmailVerified())
        .groups(Optional.ofNullable(apiRequest.getGroups()).orElse(Collections.emptySet()))
        .build();
  }

  public ApiUser toMinimalApiUser(User user) {
    return ApiUser.builder().username(user.getUsername()).build();
  }
}
