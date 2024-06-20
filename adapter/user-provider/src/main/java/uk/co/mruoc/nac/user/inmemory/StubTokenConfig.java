package uk.co.mruoc.nac.user.inmemory;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StubTokenConfig {

  private final String username;
  private final String password;
  private final String subject;
}
