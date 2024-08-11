package uk.co.mruoc.nac.api.dto;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiGamePageRequest {

  private final long limit;
  private final long offset;
  private final Boolean complete;
  private final String username;
  private final Boolean minimal;

  public boolean getMinimal() {
    return Optional.ofNullable(minimal).orElse(true);
  }
}
