package uk.co.mruoc.nac.api.dto;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUpdateUserRequestMother {

  public static ApiUpdateUserRequest updatedUser() {
    return ApiUpdateUserRequest.builder()
        .firstName("updated-first")
        .lastName("updated-last")
        .email("updated@email.com")
        .emailVerified(false)
        .groups(Set.of("player"))
        .build();
  }
}
