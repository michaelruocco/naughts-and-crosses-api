package uk.co.mruoc.nac.api.converter;

import java.io.InputStream;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class ApiUserConverter {

  private final ApiCsvUserConverter csvConverter;

  public ApiUserConverter() {
    this(new ApiCsvUserConverter());
  }

  public Collection<CreateUserRequest> toCreateUserRequests(InputStream inputStream) {
    return csvConverter.toCreateUserRequests(inputStream);
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
        .build();
  }
}
