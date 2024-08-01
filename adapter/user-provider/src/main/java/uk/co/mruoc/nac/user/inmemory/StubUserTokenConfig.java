package uk.co.mruoc.nac.user.inmemory;

import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StubUserTokenConfig {

  private final String username;
  private final String password;
  private final String subject;
  private final String authCode;

  public boolean hasUsername(String username) {
    return Objects.equals(this.username, username);
  }

  public boolean hasAuthCode(String authCode) {
    return Objects.equals(this.authCode, authCode);
  }
}
