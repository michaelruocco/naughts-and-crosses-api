package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Game {

  private final long id;
  private final Status status;
  private final Board board;

  public Game take(Turn turn) {
    validateGameNotComplete();
    validatePlayerTurn(turn);
    Board updatedBoard = board.update(turn);
    return toBuilder()
        .status(toUpdatedStatus(updatedBoard, turn.getToken()))
        .board(updatedBoard)
        .build();
  }

  public boolean isComplete() {
    return status.isComplete();
  }

  public Players getPlayers() {
    return status.getPlayers();
  }

  private void validatePlayerTurn(Turn turn) {
    status.validatePlayerTurn(turn);
  }

  private void validateGameNotComplete() {
    if (status.isComplete()) {
      throw new GameAlreadyCompleteException(id);
    }
  }

  private Status toUpdatedStatus(Board board, char token) {
    if (board.isPlayable(token)) {
      return status.turnTaken();
    }
    if (board.hasWinner(token)) {
      return status.winningTurnTaken(token);
    }
    return status.drawGameTurnTaken();
  }
}
