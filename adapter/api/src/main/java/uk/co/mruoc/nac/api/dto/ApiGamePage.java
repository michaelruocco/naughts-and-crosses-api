package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiGamePage {

  private final long total;
  private final Collection<ApiGame> games;
}
