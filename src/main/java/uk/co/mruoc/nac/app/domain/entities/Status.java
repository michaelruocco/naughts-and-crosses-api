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

    public Status(Players players) {
        this(0, false, players);
    }

    public Status turnTaken() {
        return toBuilder().turn(turn + 1).players(players.updateCurrentPlayer()).build();
    }

    public char getCurrentPlayerToken() {
        return players.getCurrentPlayerToken();
    }

    public void validateIsTurn(char token) {
        players.validateIsTurn(token);
    }
}
