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
public class ApiStatus {

  private final long turn;
  private final boolean complete;
  private final Character nextPlayerToken;

  public Optional<Character> getNextPlayerToken() {
    return Optional.ofNullable(nextPlayerToken);
  }
}
