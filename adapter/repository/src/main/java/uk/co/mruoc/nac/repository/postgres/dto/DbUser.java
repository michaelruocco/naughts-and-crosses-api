package uk.co.mruoc.nac.repository.postgres.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class DbUser {

  private final String id;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String email;
}
