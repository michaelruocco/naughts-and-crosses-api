package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class GameTest {

  private final Status status = mock(Status.class);
  private final Board board = mock(Board.class);

  private final Player crossesPlayer = PlayerMother.crossesPlayer();
  private final Turn turn =
      Turn.builder()
          .coordinates(new Coordinates(0, 0))
          .username(crossesPlayer.getUsername())
          .build();

  private final Game game = Game.builder().id(1).status(status).board(board).build();

  @Test
  void shouldThrowErrorIfGameIsComplete() {
    when(status.isComplete()).thenReturn(true);

    Throwable error = catchThrowable(() -> game.take(turn));

    assertThat(error)
        .isInstanceOf(GameAlreadyCompleteException.class)
        .hasMessage("game 1 is already complete");
  }

  @Test
  void shouldThrowErrorIfNotPlayersTurn() {
    Throwable expectedError = new NotPlayersTurnException(crossesPlayer.getUsername());
    doThrow(expectedError).when(status).validate(turn);

    Throwable error = catchThrowable(() -> game.take(turn));

    assertThat(error).isEqualTo(expectedError);
  }

  @Test
  void shouldUpdateBoardWhenTurnTaken() {
    Board expectedBoard = givenUpdatedBoard();
    givenTurnTaken(expectedBoard);

    Game updatedGame = game.take(turn);

    assertThat(updatedGame.getBoard()).isEqualTo(expectedBoard);
  }

  @Test
  void shouldUpdateStatusWhenTurnTaken() {
    Status expectedStatus = mock(Status.class);
    when(status.turnTaken()).thenReturn(expectedStatus);
    givenTurnTaken();

    Game updatedGame = game.take(turn);

    assertThat(updatedGame.getStatus()).isEqualTo(expectedStatus);
  }

  @Test
  void shouldUpdateStatusWhenDrawGameTurnTaken() {
    Status expectedStatus = mock(Status.class);
    when(status.drawGameTurnTaken()).thenReturn(expectedStatus);
    givenDrawGameTurnTaken();

    Game updatedGame = game.take(turn);

    assertThat(updatedGame.getStatus()).isEqualTo(expectedStatus);
  }

  @Test
  void shouldUpdateStatusWhenWinningTurnTaken() {
    Status expectedStatus = mock(Status.class);
    when(status.winningTurnTaken(turn.getToken())).thenReturn(expectedStatus);
    givenWinningTurnTaken();

    Game updatedGame = game.take(turn);

    assertThat(updatedGame.getStatus()).isEqualTo(expectedStatus);
  }

  @Test
  void shouldReturnIsCompleteFromStatus() {
    when(status.isComplete()).thenReturn(true);

    assertThat(game.isComplete()).isTrue();
  }

  @Test
  void shouldReturnPlayersFromStatus() {
    Players expectedPlayers = mock(Players.class);
    when(status.getPlayers()).thenReturn(expectedPlayers);

    assertThat(game.getPlayers()).isEqualTo(expectedPlayers);
  }

  @Test
  void shouldThrowErrorIfStatusDoesNotContainPlayer() {
    User user = UserMother.user1();
    when(status.containsPlayer(user)).thenReturn(false);

    Throwable error = catchThrowable(() -> game.validateIsPlayer(user));

    assertThat(error)
            .isInstanceOf(UserNotGamePlayerException.class)
            .hasMessage("user %s is not a player of game %d", user.getUsername(), game.getId());
  }

  private void givenTurnTaken() {
    Board updatedBoard = givenUpdatedBoard();
    givenTurnTaken(updatedBoard);
  }

  private void givenTurnTaken(Board board) {
    when(board.isPlayable(turn.getToken())).thenReturn(true);
  }

  private void givenDrawGameTurnTaken() {
    Board gameEndingTurnBoard = givenGameEndingTurnTaken();
    when(gameEndingTurnBoard.hasWinner(turn.getToken())).thenReturn(false);
  }

  private void givenWinningTurnTaken() {
    Board gameEndingTurnBoard = givenGameEndingTurnTaken();
    when(gameEndingTurnBoard.hasWinner(turn.getToken())).thenReturn(true);
  }

  private Board givenGameEndingTurnTaken() {
    Board updatedBoard = givenUpdatedBoard();
    when(updatedBoard.isPlayable(turn.getToken())).thenReturn(false);
    return updatedBoard;
  }

  private Board givenUpdatedBoard() {
    Board updatedBoard = mock(Board.class);
    when(board.update(turn)).thenReturn(updatedBoard);
    return updatedBoard;
  }
}
