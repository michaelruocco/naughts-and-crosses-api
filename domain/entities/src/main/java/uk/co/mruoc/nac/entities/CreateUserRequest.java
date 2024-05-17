package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserRequest {
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String email;
}
