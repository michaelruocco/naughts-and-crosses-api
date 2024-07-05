package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import org.junit.jupiter.api.Test;

class BoardTest {

  private final Player crossesPlayer = PlayerMother.crossesPlayer();

  private final Board board = new Board();

  @Test
  void shouldReturnSize() {
    assertThat(board.getSize()).isEqualTo(3);
  }

  @Test
  void shouldContainSizeSquaredNumberOfLocations() {
    assertThat(board.getLocations())
        .hasSize(9)
        .contains(new Location(0, 0))
        .contains(new Location(1, 0))
        .contains(new Location(2, 0))
        .contains(new Location(0, 1))
        .contains(new Location(1, 1))
        .contains(new Location(2, 1))
        .contains(new Location(0, 2))
        .contains(new Location(1, 2))
        .contains(new Location(2, 2));
  }

  @Test
  void shouldUpdateBoardIfLocationAvailable() {
    Coordinates coordinates = new Coordinates(0, 0);
    Turn turn = new Turn(coordinates, crossesPlayer);

    Board updatedBoard = board.update(turn);

    assertThat(updatedBoard.getLocation(coordinates))
        .contains(new Location(coordinates, crossesPlayer.getToken()));
  }

  @Test
  void shouldThrowErrorOnUpdateBoardIfLocationIsNotAvailable() {
    Turn turn = new Turn(0, 0, crossesPlayer);
    Board updatedBoard = board.update(turn);

    Throwable error = catchThrowable(() -> updatedBoard.update(turn));

    assertThat(error).isInstanceOf(LocationNotAvailableException.class);
  }

  @Test
  void shouldThrowErrorOnGetTokenIfLocationIsNotFound() {
    int x = 3;
    int y = 2;

    Throwable error = catchThrowable(() -> board.getToken(x, y));

    assertThat(error).isInstanceOf(LocationNotFoundException.class);
  }

  @Test
  void shouldEmptyCollectionIfNoWinner() {
    char token = 'O';

    Collection<Coordinates> winner = board.findWinningLine(token);

    assertThat(winner).isEmpty();
  }

  @Test
  void shouldReturnFalseIfNoWinner() {
    char token = 'O';

    boolean winner = board.hasWinner(token);

    assertThat(winner).isFalse();
  }

  @Test
  void shouldReturnTrueIfWinner() {
    Board updatedBoard =
        board
            .update(new Turn(0, 0, crossesPlayer))
            .update(new Turn(1, 0, crossesPlayer))
            .update(new Turn(2, 0, crossesPlayer));

    boolean winner = updatedBoard.hasWinner(crossesPlayer.getToken());

    assertThat(winner).isTrue();
  }

  @Test
  void shouldWinningCoordinatesIfHorizontalWinner() {
    Board updatedBoard =
        board
            .update(new Turn(0, 0, crossesPlayer))
            .update(new Turn(1, 0, crossesPlayer))
            .update(new Turn(2, 0, crossesPlayer));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(crossesPlayer.getToken());

    assertThat(winner)
        .containsExactly(new Coordinates(0, 0), new Coordinates(1, 0), new Coordinates(2, 0));
  }

  @Test
  void shouldReturnWinningCoordinatesIfVerticalWinner() {
    Board updatedBoard =
        board
            .update(new Turn(0, 0, crossesPlayer))
            .update(new Turn(0, 1, crossesPlayer))
            .update(new Turn(0, 2, crossesPlayer));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(crossesPlayer.getToken());

    assertThat(winner)
        .containsExactly(new Coordinates(0, 0), new Coordinates(0, 1), new Coordinates(0, 2));
  }

  @Test
  void shouldCoordinatesIfForwardSlashWinner() {
    Board updatedBoard =
        board
            .update(new Turn(0, 2, crossesPlayer))
            .update(new Turn(1, 1, crossesPlayer))
            .update(new Turn(2, 0, crossesPlayer));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(crossesPlayer.getToken());

    assertThat(winner)
        .containsExactlyInAnyOrder(
            new Coordinates(0, 2), new Coordinates(1, 1), new Coordinates(2, 0));
  }

  @Test
  void shouldCoordinatesIfBackSlashWinner() {
    Board updatedBoard =
        board
            .update(new Turn(0, 0, crossesPlayer))
            .update(new Turn(1, 1, crossesPlayer))
            .update(new Turn(2, 2, crossesPlayer));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(crossesPlayer.getToken());

    assertThat(winner)
        .containsExactly(new Coordinates(0, 0), new Coordinates(1, 1), new Coordinates(2, 2));
  }
}
