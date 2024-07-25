package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpsertUserRequest {
  private final String username;
  private final UserName name;
  private final String email;
  private final boolean emailVerified;
  private final Collection<String> groups;

  public String getFullName() {
    return name.getFull();
  }

  public String getFirstName() {
    return name.getFirst();
  }

  public String getLastName() {
    return name.getLast();
  }
}
