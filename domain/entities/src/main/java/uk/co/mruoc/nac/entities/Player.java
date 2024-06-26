package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Player {

  private final User user;
  private final char token;

  public boolean hasUsername(String otherUsername) {
    return user.hasUsername(otherUsername);
  }

  public boolean hasToken(char otherToken) {
    return token == otherToken;
  }

  public String getName() {
    return user.getFullName();
  }
}
