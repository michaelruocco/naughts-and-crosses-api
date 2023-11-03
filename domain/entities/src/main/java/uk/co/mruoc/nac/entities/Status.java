package uk.co.mruoc.nac.entities;

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

    public Status() {
        this(new Players());
    }

    public Status(Players players) {
        this(0, false, players);
    }

    public Status turnTaken() {
        return toBuilder()
                .turn(nextTurn())
                .players(players.updateCurrentPlayer())
                .build();
    }

    public Status winningTurnTaken() {
        return toBuilder().turn(nextTurn()).complete(true).build();
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
