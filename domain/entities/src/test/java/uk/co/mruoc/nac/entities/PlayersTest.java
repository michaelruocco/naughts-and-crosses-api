package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PlayersTest {

  private final Player crossesPlayer = PlayerMother.crossesPlayer();
  private final Player naughtsPlayer = PlayerMother.naughtsPlayer();

  private final Players players = PlayerMother.of(crossesPlayer, naughtsPlayer);

  @Test
  void shouldReturnStreamOfPlayers() {
    assertThat(players.stream()).map(Player::getFullName).containsExactly("User One", "User Two");
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

  @Test
  void shouldReturnDuplicateTokensIfAnyPresent() {
    Players duplicateTokenPlayers = new Players(crossesPlayer, crossesPlayer);

    Collection<Character> duplicateTokens = duplicateTokenPlayers.getDuplicateTokens();

    assertThat(duplicateTokens).contains(crossesPlayer.getToken());
  }

  @Test
  void shouldThrowErrorIfCurrentPlayerHasBeenCleared() {
    Turn turn = new Turn(0, 0, crossesPlayer);
    Players clearedPlayers = players.clearCurrentPlayer();

    Throwable error = catchThrowable(() -> clearedPlayers.validate(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for username %s", crossesPlayer.getUsername());
  }

  @Test
  void shouldThrowErrorIfTurnPlayerIsNotCurrentPlayer() {
    Turn turn = new Turn(0, 0, naughtsPlayer);

    Throwable error = catchThrowable(() -> players.validate(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for username %s", naughtsPlayer.getUsername());
  }

  @Test
  void shouldThrowErrorIfTurnPlayerTokenDoesNotMatchCurrentPlayerToken() {
    Player player = PlayerMother.withToken('O');
    Turn turn = new Turn(0, 0, player);

    Throwable error = catchThrowable(() -> players.validate(turn));

    assertThat(error)
        .isInstanceOf(IncorrectTokenForPlayerException.class)
        .hasMessage("incorrect token %s for user %s", player.getToken(), player.getUsername());
  }

  @Test
  void shouldThrowErrorIfPlayerWithTokenIsNotFound() {
    char token = '-';

    Throwable error = catchThrowable(() -> players.getPlayerByToken(token));

    assertThat(error)
        .isInstanceOf(PlayerWithTokenNotFound.class)
        .hasMessage(Character.toString(token));
  }
}
