package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserRequest {
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean emailVerified;
  private final Collection<String> groups;
}
