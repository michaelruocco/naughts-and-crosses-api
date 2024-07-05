package uk.co.mruoc.nac.entities;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder(toBuilder = true)
@Data
public class Status {

  private final long turn;
  private final Players players;
  private final boolean complete;
  private final Player winner;

  public Status(Players players) {
    this(0, players, false, null);
  }

  public Status turnTaken() {
    return toBuilder().turn(nextTurn()).players(players.updateCurrentPlayer()).build();
  }

  public Status winningTurnTaken(char token) {
    return Status.builder()
        .turn(nextTurn())
        .players(players.clearCurrentPlayer())
        .complete(true)
        .winner(players.getPlayerByToken(token))
        .build();
  }

  public Status drawGameTurnTaken() {
    return Status.builder()
        .turn(nextTurn())
        .players(players.clearCurrentPlayer())
        .complete(true)
        .build();
  }

  public Optional<Player> getCurrentPlayer() {
    return players.getCurrentPlayer();
  }

  public void validate(Turn turn) {
    players.validate(turn);
  }

  public Optional<Player> getWinner() {
    return Optional.ofNullable(winner);
  }

  public boolean isDraw() {
    return complete && getWinner().isEmpty();
  }

  public boolean containsPlayer(User user) {
    return players.containsUsername(user.getUsername());
  }

  private long nextTurn() {
    return turn + 1;
  }
}
