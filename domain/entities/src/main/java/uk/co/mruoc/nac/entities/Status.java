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
  private final Character winner;

  public Status() {
    this(new Players());
  }

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
        .winner(token)
        .build();
  }

  public Status drawGameTurnTaken() {
    return Status.builder()
        .turn(nextTurn())
        .players(players.clearCurrentPlayer())
        .complete(true)
        .build();
  }

  public Optional<Character> getCurrentPlayerToken() {
    return players.getCurrentPlayerToken();
  }

  public Optional<Character> getWinner() {
    return Optional.ofNullable(winner);
  }

  public void validateIsTurn(char token) {
    players.validateIsTurn(token);
  }

  public boolean isDraw() {
    return complete && getWinner().isEmpty();
  }

  private long nextTurn() {
    return turn + 1;
  }
}
