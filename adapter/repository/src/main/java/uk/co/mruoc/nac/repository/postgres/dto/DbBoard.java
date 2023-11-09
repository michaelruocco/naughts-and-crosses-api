package uk.co.mruoc.nac.repository.postgres.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class DbBoard {

  private final int size;
  private final Collection<DbLocation> locations;
}
