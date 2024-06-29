package uk.co.mruoc.nac.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class User {

  private final String username;
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean emailVerified;
  private final Collection<String> groups;
  private final String status;

  public String getFullName() {
    Collection<String> names = new ArrayList<>();
    if (Objects.nonNull(firstName)) {
      names.add(firstName);
    }
    if (Objects.nonNull(lastName)) {
      names.add(lastName);
    }
    return String.join(" ", names);
  }

  public User update(UpsertUserRequest request) {
    return toBuilder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .emailVerified(request.isEmailVerified())
        .groups(request.getGroups())
        .build();
  }

  public boolean isMemberOfAtLeastOneGroup(Collection<String> groupsToCheck) {
    return groupsToCheck.stream().anyMatch(groups::contains);
  }
}
