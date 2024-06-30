package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PlayersTest {

  private final Player crossesPlayer = PlayerMother.crossesPlayer();
  private final Player naughtsPlayer = PlayerMother.naughtsPlayer();

  private final Players players = PlayerMother.of(crossesPlayer, naughtsPlayer);

  @Test
  void shouldReturnStreamOfPlayers() {
    assertThat(players.stream()).map(Player::getName).containsExactly("User One", "User Two");
  }

  @Test
  void shouldCreatePlayer1AsCrossedAnd2AsNaughts() {
    assertThat(players.stream()).map(Player::getToken).containsExactly('X', 'O');
  }

  @Test
  void shouldAlternatePlayers() {
    assertThat(players.getCurrentPlayer()).contains(crossesPlayer);
    Players updated1 = players.updateCurrentPlayer();
    assertThat(updated1.getCurrentPlayer()).contains(naughtsPlayer);
    Players updated2 = updated1.updateCurrentPlayer();
    assertThat(updated2.getCurrentPlayer()).contains(crossesPlayer);
  }

  @Test
  void shouldClearCurrentPlayers() {
    Players cleared = players.clearCurrentPlayer();

    Optional<Player> currentPlayer = cleared.getCurrentPlayer();

    assertThat(currentPlayer).isEmpty();
  }
}
