package uk.co.mruoc.nac.repository.postgres.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class DbLocation {

  private final DbCoordinates coordinates;
  private final char token;
  private final boolean winner;
}
