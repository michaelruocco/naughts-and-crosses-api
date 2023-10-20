package uk.co.mruoc.nac.app.domain.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder(toBuilder = true)
@Data
public class Status {

    private final int turn;
    private final boolean complete;
    private final Players players;
    private final Character winningPlayerToken;

    public Status() {
        this(new Players());
    }

    public Status(Players players) {
        this(0, false, players, null);
    }

    public Status turnTaken() {
        return toBuilder()
                .turn(nextTurn())
                .players(players.updateCurrentPlayer())
                .build();
    }

    public Status winningTurnTaken(char token) {
        return toBuilder()
                .turn(nextTurn())
                .complete(true)
                .winningPlayerToken(token)
                .build();
    }

    public char getCurrentPlayerToken() {
        return players.getCurrentPlayerToken();
    }

    public void validateIsTurn(char token) {
        players.validateIsTurn(token);
    }

    private int nextTurn() {
        return turn + 1;
    }
}
