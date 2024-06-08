package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserRequest {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean emailVerified;
}
