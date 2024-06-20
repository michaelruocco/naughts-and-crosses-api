package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTokenRequest {
  private final String username;
  private final String password;
}
