package uk.co.mruoc.nac.app.domain.entities;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import uk.co.mruoc.nac.app.domain.usecases.GameAlreadyCompleteException;
import uk.co.mruoc.nac.app.domain.usecases.LocationNotAvailableException;
import uk.co.mruoc.nac.app.domain.usecases.NotPlayersTurnException;

@Builder(toBuilder = true)
@Data
public class Game {

    private final UUID id;
    private final Status status;
    private final Players players;
    private final Board board;

    public Game take(Turn turn) {
        validateGameNotComplete();

        Player player = getNextPlayerIfTurn(turn.getToken());
        Location location = getLocationIfAvailable(turn.getCoordinates());

        Board updatedBoard = board.update(location.withToken(player.getToken()));
        Status updatedStatus = Status.builder()
                .complete(false)
                .nextPlayer(players.getNextPlayer(turn.getToken()))
                .build();
        return toBuilder().board(updatedBoard).status(updatedStatus).build();
    }

    private void validateGameNotComplete() {
        if (status.isComplete()) {
            throw new GameAlreadyCompleteException(id);
        }
    }

    private Player getNextPlayerIfTurn(char token) {
        if (!status.isNextPlayer(token)) {
            throw new NotPlayersTurnException(token);
        }
        return players.getPlayer(token);
    }

    private Location getLocationIfAvailable(Coordinates coordinates) {
        return board.getLocationIfAvailable(coordinates)
                .orElseThrow(() -> new LocationNotAvailableException(coordinates));
    }
}
