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
  private final String name;
  private final String email;
}
