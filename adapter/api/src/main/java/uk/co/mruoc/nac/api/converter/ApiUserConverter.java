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
        .build();
  }

  public Collection<User> toUsers(Collection<ApiUser> apiUsers) {
    return apiUsers.stream().map(this::toUser).toList();
  }

  public User toUser(ApiUser apiUser) {
    return User.builder()
        .id(apiUser.getId())
        .username(apiUser.getUsername())
        .firstName(apiUser.getFirstName())
        .lastName(apiUser.getLastName())
        .email(apiUser.getEmail())
        .emailVerified(apiUser.isEmailVerified())
        .build();
  }

  public CreateUserRequest toCreateUserRequest(ApiCreateUserRequest apiRequest) {
    return CreateUserRequest.builder()
        .username(apiRequest.getUsername())
        .firstName(apiRequest.getFirstName())
        .lastName(apiRequest.getLastName())
        .email(apiRequest.getEmail())
        .emailVerified(apiRequest.isEmailVerified())
        .build();
  }

  public UpdateUserRequest toUpdateUserRequest(String id, ApiUpdateUserRequest apiRequest) {
    return UpdateUserRequest.builder()
        .id(id)
        .firstName(apiRequest.getFirstName())
        .lastName(apiRequest.getLastName())
        .email(apiRequest.getEmail())
        .emailVerified(apiRequest.isEmailVerified())
        .build();
  }
}
