package uk.co.mruoc.nac.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlayersTest {

    private final Players players = new Players();

    @Test
    void shouldCreateTwoPlayers() {
        assertThat(players.stream()).map(Player::getName).containsExactly("Player 1", "Player 2");
    }

    @Test
    void shouldCreatePlayer1AsCrossedAnd2AsNaughts() {
        assertThat(players.stream()).map(Player::getToken).containsExactly('X', 'O');
    }
}
