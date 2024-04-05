package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import org.junit.jupiter.api.Test;

class BoardTest {

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
    Turn turn = Turn.builder().coordinates(coordinates).token('X').build();

    Board updatedBoard = board.update(turn);

    assertThat(updatedBoard.getLocation(coordinates)).contains(new Location(coordinates, 'X'));
  }

  @Test
  void shouldThrowErrorOnUpdateBoardIfLocationIsNotAvailable() {
    Turn turn = new Turn(0, 0, 'X');
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
    char token = 'X';
    Board updatedBoard =
        board
            .update(new Turn(0, 0, token))
            .update(new Turn(1, 0, token))
            .update(new Turn(2, 0, token));

    boolean winner = updatedBoard.hasWinner(token);

    assertThat(winner).isTrue();
  }

  @Test
  void shouldWinningCoordinatesIfHorizontalWinner() {
    char token = 'X';
    Board updatedBoard =
        board
            .update(new Turn(0, 0, token))
            .update(new Turn(1, 0, token))
            .update(new Turn(2, 0, token));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(token);

    assertThat(winner)
        .containsExactly(new Coordinates(0, 0), new Coordinates(1, 0), new Coordinates(2, 0));
  }

  @Test
  void shouldReturnWinningCoordinatesIfVerticalWinner() {
    char token = 'O';
    Board updatedBoard =
        board
            .update(new Turn(0, 0, token))
            .update(new Turn(0, 1, token))
            .update(new Turn(0, 2, token));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(token);

    assertThat(winner)
        .containsExactly(new Coordinates(0, 0), new Coordinates(0, 1), new Coordinates(0, 2));
  }

  @Test
  void shouldCoordinatesIfForwardSlashWinner() {
    char token = 'X';
    Board updatedBoard =
        board
            .update(new Turn(0, 2, token))
            .update(new Turn(1, 1, token))
            .update(new Turn(2, 0, token));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(token);

    assertThat(winner)
        .containsExactlyInAnyOrder(
            new Coordinates(0, 2), new Coordinates(1, 1), new Coordinates(2, 0));
  }

  @Test
  void shouldCoordinatesIfBackSlashWinner() {
    char token = 'X';
    Board updatedBoard =
        board
            .update(new Turn(0, 0, token))
            .update(new Turn(1, 1, token))
            .update(new Turn(2, 2, token));

    Collection<Coordinates> winner = updatedBoard.findWinningLine(token);

    assertThat(winner)
        .containsExactly(new Coordinates(0, 0), new Coordinates(1, 1), new Coordinates(2, 2));
  }
}
