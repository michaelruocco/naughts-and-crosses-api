package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiUpdateUserRequest {

  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean emailVerified;
  private final Collection<String> groups;
}
