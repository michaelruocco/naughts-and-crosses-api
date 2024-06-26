package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UpdateUserRequest;
import uk.co.mruoc.nac.entities.User;

public class ApiUserConverter {

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

  public CreateUserRequest toCreateUserRequest(ApiCreateUserRequest apiRequest) {
    return CreateUserRequest.builder()
        .username(apiRequest.getUsername())
        .firstName(apiRequest.getFirstName())
        .lastName(apiRequest.getLastName())
        .email(apiRequest.getEmail())
        .emailVerified(apiRequest.isEmailVerified())
        .groups(apiRequest.getGroups())
        .build();
  }

  public Collection<ApiCreateUserRequest> toApiCreateUserRequests(
      Collection<CreateUserRequest> requests) {
    return requests.stream().map(this::toApiCreateUserRequest).toList();
  }

  public ApiCreateUserRequest toApiCreateUserRequest(CreateUserRequest request) {
    return ApiCreateUserRequest.builder()
        .username(request.getUsername())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .emailVerified(request.isEmailVerified())
        .groups(request.getGroups())
        .build();
  }

  public UpdateUserRequest toUpdateUserRequest(String username, ApiUpdateUserRequest apiRequest) {
    return UpdateUserRequest.builder()
        .username(username)
        .firstName(apiRequest.getFirstName())
        .lastName(apiRequest.getLastName())
        .email(apiRequest.getEmail())
        .emailVerified(apiRequest.isEmailVerified())
        .groups(apiRequest.getGroups())
        .build();
  }
}
