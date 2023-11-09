package uk.co.mruoc.nac.entities;

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

  @Test
  void shouldAlternatePlayers() {
    assertThat(players.getCurrentPlayerToken()).contains('X');
    Players updated1 = players.updateCurrentPlayer();
    assertThat(updated1.getCurrentPlayerToken()).contains('O');
    Players updated2 = updated1.updateCurrentPlayer();
    assertThat(updated2.getCurrentPlayerToken()).contains('X');
  }

  @Test
  void shouldClearCurrentPlayers() {
    Players cleared = players.clearCurrentPlayer();
    assertThat(cleared.getCurrentPlayerToken()).isEmpty();
  }
}
