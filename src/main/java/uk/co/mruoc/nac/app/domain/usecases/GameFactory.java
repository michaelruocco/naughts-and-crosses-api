package uk.co.mruoc.nac.app.domain.usecases;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import uk.co.mruoc.nac.app.domain.entities.Board;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Location;
import uk.co.mruoc.nac.app.domain.entities.Player;
import uk.co.mruoc.nac.app.domain.entities.Players;
import uk.co.mruoc.nac.app.domain.entities.Status;

public class GameFactory {

    public Game buildGame() {
        Player player1 = player1();
        Player player2 = player2();
        return Game.builder()
                .id(UUID.fromString("5b6bff66-7361-4d8b-9eb1-d7b3500a6bf3"))
                .status(initialStatus(player1))
                .players(new Players(player1, player2))
                .board(initialBoard())
                .build();
    }

    private static Player player1() {
        return Player.builder().name("Player 1").token('X').build();
    }

    private static Player player2() {
        return Player.builder().name("Player 2").token('O').build();
    }

    private static Status initialStatus(Player nextPlayer) {
        return Status.builder().complete(false).nextPlayer(nextPlayer).build();
    }

    private static Board initialBoard() {
        return Board.builder().size(3).locations(locations()).build();
    }

    private static Collection<Location> locations() {
        return List.of(
                new Location(0, 0),
                new Location(1, 0),
                new Location(2, 0),
                new Location(0, 1),
                new Location(1, 1),
                new Location(2, 1),
                new Location(0, 2),
                new Location(1, 2),
                new Location(2, 2));
    }
}
