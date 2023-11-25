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
  private final boolean draw;
  private final Character nextPlayerToken;
  private final Character winner;

  public Optional<Character> getNextPlayerToken() {
    return Optional.ofNullable(nextPlayerToken);
  }

  public Optional<Character> getWinner() {
    return Optional.ofNullable(winner);
  }
}
