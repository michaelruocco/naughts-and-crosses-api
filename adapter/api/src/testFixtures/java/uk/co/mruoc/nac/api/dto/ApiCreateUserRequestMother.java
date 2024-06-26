package uk.co.mruoc.nac.api.dto;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCreateUserRequestMother {

  public static ApiCreateUserRequest joeBloggs() {
    return ApiCreateUserRequest.builder()
        .username("jbloggs")
        .firstName("Joe")
        .lastName("Bloggs")
        .email("joe.bloggs@hotmail.com")
        .emailVerified(true)
        .groups(Set.of("player"))
        .build();
  }
}
