package uk.co.mruoc.nac.app.domain.entities;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import uk.co.mruoc.nac.app.domain.usecases.GameAlreadyCompleteException;

@Builder(toBuilder = true)
@Data
public class Game {

    private final UUID id;
    private final Status status;
    private final Board board;

    public Game take(Turn turn) {
        validateGameNotComplete();
        validateIsPlayerTurn(turn);
        return toBuilder().status(status.turnTaken()).board(board.update(turn)).build();
    }

    private void validateGameNotComplete() {
        if (status.isComplete()) {
            throw new GameAlreadyCompleteException(id);
        }
    }

    private void validateIsPlayerTurn(Turn turn) {
        status.validateIsTurn(turn.getToken());
    }
}
