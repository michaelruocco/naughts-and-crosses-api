package uk.co.mruoc.nac.entities;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class User {

  private final String username;
  private final String id;
  private final UserName name;
  private final String email;
  private final boolean emailVerified;
  private final Collection<String> groups;
  private final String status;

  public boolean hasUsername(String otherUsername) {
    return Objects.equals(username, otherUsername);
  }

  public User update(UpsertUserRequest request) {
    return toBuilder()
        .name(request.getName())
        .email(request.getEmail())
        .emailVerified(request.isEmailVerified())
        .groups(request.getGroups())
        .build();
  }

  public boolean isAdmin() {
    return isMemberOfAtLeastOneGroup(List.of("admin"));
  }

  public boolean isMemberOfAtLeastOneGroup(Collection<String> groupsToCheck) {
    return groupsToCheck.stream().anyMatch(groups::contains);
  }

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
