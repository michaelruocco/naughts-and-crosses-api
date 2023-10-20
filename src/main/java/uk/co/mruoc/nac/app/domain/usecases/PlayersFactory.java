package uk.co.mruoc.nac.app.domain.usecases;

import uk.co.mruoc.nac.app.domain.entities.Player;
import uk.co.mruoc.nac.app.domain.entities.Players;

public class PlayersFactory {

    public Players buildPlayers() {
        return new Players(player1(), player2());
    }

    private static Player player1() {
        return Player.builder().name("Player 1").token('X').build();
    }

    private static Player player2() {
        return Player.builder().name("Player 2").token('O').build();
    }
}
