package uk.co.mruoc.nac.app.domain.entities;

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
        validateIsPlayerTurn(turn);
        Board updatedBoard = board.update(turn);
        return toBuilder()
                .status(toUpdatedStatus(updatedBoard, turn))
                .board(updatedBoard)
                .build();
    }

    public boolean isComplete() {
        return status.isComplete();
    }

    public Players getPlayers() {
        return status.getPlayers();
    }

    private void validateGameNotComplete() {
        if (status.isComplete()) {
            throw new GameAlreadyCompleteException(id);
        }
    }

    private void validateIsPlayerTurn(Turn turn) {
        status.validateIsTurn(turn.getToken());
    }

    private Status toUpdatedStatus(Board updatedBoard, Turn turn) {
        if (updatedBoard.hasWinner(turn.getToken())) {
            return status.winningTurnTaken(turn.getToken());
        }
        return status.turnTaken();
    }
}
