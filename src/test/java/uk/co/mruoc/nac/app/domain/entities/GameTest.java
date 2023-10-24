package uk.co.mruoc.nac.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class GameTest {

    private final UUID id = UUID.fromString("c470a2d6-a1db-4f7b-8d2c-e76e2332680c");
    private final Status status = mock(Status.class);
    private final Board board = mock(Board.class);

    private final Turn turn =
            Turn.builder().coordinates(new Coordinates(0, 0)).token('X').build();

    private final Game game = Game.builder().id(id).status(status).board(board).build();

    @Test
    void shouldThrowErrorIfGameIsComplete() {
        when(status.isComplete()).thenReturn(true);

        Throwable error = catchThrowable(() -> game.take(turn));

        assertThat(error)
                .isInstanceOf(GameAlreadyCompleteException.class)
                .hasMessage("game c470a2d6-a1db-4f7b-8d2c-e76e2332680c is already complete");
    }

    @Test
    void shouldThrowErrorIfNotPlayersTurn() {
        Throwable expectedError = new NotPlayersTurnException(turn.getToken());
        doThrow(expectedError).when(status).validateIsTurn(turn.getToken());

        Throwable error = catchThrowable(() -> game.take(turn));

        assertThat(error).isEqualTo(expectedError);
    }

    @Test
    void shouldUpdateBoardWhenTurnTaken() {
        Board expectedBoard = givenUpdatedBoard();

        Game updatedGame = game.take(turn);

        assertThat(updatedGame.getBoard()).isEqualTo(expectedBoard);
    }

    @Test
    void shouldUpdateStatusWhenNonWinningTurnTaken() {
        Status expectedStatus = mock(Status.class);
        when(status.turnTaken()).thenReturn(expectedStatus);
        givenNonWinningTurnTaken();

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

    private void givenNonWinningTurnTaken() {
        Board updatedBoard = givenUpdatedBoard();
        when(updatedBoard.hasWinner(turn.getToken())).thenReturn(false);
    }

    private void givenWinningTurnTaken() {
        Board updatedBoard = givenUpdatedBoard();
        when(updatedBoard.hasWinner(turn.getToken())).thenReturn(true);
    }

    private Board givenUpdatedBoard() {
        Board updatedBoard = mock(Board.class);
        when(board.update(turn)).thenReturn(updatedBoard);
        return updatedBoard;
    }
}
