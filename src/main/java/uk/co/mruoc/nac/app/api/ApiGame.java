package uk.co.mruoc.nac.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.rest.GameAlreadyCompleteException;
import uk.co.mruoc.nac.app.rest.LocationNotAvailableException;
import uk.co.mruoc.nac.app.rest.NotPlayersTurnException;
import uk.co.mruoc.nac.app.rest.PlayerNotFoundException;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
@Data
public class ApiGame {

    private final UUID id;
    private final ApiStatus status;
    private final Collection<ApiPlayer> players;
    private final ApiBoard board;

    public ApiGame take(ApiTurn turn) {
        validateGameNotComplete();

        ApiPlayer player = getNextPlayerIfTurn(turn.getToken());
        ApiLocation location = getLocationIfAvailable(turn.getCoordinates());

        ApiBoard updatedBoard = board.update(location.withToken(player.getToken()));
        ApiStatus updatedStatus = ApiStatus.builder()
                .complete(false)
                .nextPlayer(players.stream().filter(p -> p.getToken() != turn.getToken()).findFirst().orElseThrow())
                .build();
        return toBuilder()
                .board(updatedBoard)
                .status(updatedStatus)
                .build();
    }

    private void validateGameNotComplete() {
        if (status.isComplete()) {
            throw new GameAlreadyCompleteException(id);
        }
    }

    private ApiPlayer getNextPlayerIfTurn(char token) {
        if (!status.isNextPlayer(token)) {
            throw new NotPlayersTurnException(token);
        }
        return players.stream()
                .filter(p -> p.getToken() == token)
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(token));
    }

    private ApiLocation getLocationIfAvailable(ApiCoordinates coordinates) {
        return board.getLocationIfAvailable(coordinates)
                .orElseThrow(() -> new LocationNotAvailableException(coordinates));
    }
}
