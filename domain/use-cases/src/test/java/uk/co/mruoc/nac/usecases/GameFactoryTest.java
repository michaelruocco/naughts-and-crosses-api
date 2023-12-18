package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.LongSupplier;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.Board;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.PlayerMother;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.Status;

class GameFactoryTest {

  private final LongSupplier idSupplier = mock(LongSupplier.class);

  private final Players players = PlayerMother.players();

  private final GameFactory factory = new GameFactory(idSupplier);

  @Test
  void shouldPopulateIdFromSupplier() {
    long expectedId = 5;
    when(idSupplier.getAsLong()).thenReturn(expectedId);

    Game game = factory.buildGame(players);

    assertThat(game.getId()).isEqualTo(expectedId);
  }

  @Test
  void shouldCreateIncompleteGame() {
    Game game = factory.buildGame(players);

    Status status = game.getStatus();
    assertThat(status.isComplete()).isFalse();
  }

  @Test
  void initialTurnShouldBeZero() {
    Game game = factory.buildGame(players);

    Status status = game.getStatus();
    assertThat(status.getTurn()).isZero();
  }

  @Test
  void firstPlayerShouldBeCrosses() {
    Game game = factory.buildGame(players);

    Status status = game.getStatus();
    assertThat(status.getCurrentPlayerToken()).contains('X');
  }

  @Test
  void boardShouldBeEmpty() {
    Game game = factory.buildGame(players);

    Board board = game.getBoard();
    assertThat(board.isEmpty()).isTrue();
  }
}
