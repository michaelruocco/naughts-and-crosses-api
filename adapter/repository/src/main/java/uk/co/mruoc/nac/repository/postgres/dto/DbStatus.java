package uk.co.mruoc.nac.repository.postgres.dto;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class DbStatus {

  private final long turn;
  private final boolean complete;
  private final DbPlayers players;
  private final DbPlayer winner;

  public Optional<DbPlayer> getWinner() {
    return Optional.ofNullable(winner);
  }
}
