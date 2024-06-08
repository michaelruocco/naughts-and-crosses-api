package uk.co.mruoc.nac.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

  private final String id;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String email;

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

  public boolean hasUsername(String otherUsername) {
    return username.equals(otherUsername);
  }

  public boolean hasId(String otherId) {
    return id.equals(otherId);
  }
}
