package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import org.junit.jupiter.api.Test;

class StatusTest {

  private final Status status = new Status(PlayerMother.players());

  @Test
  void shouldReturnInitialTurnAsZero() {
    assertThat(status.getTurn()).isZero();
  }

  @Test
  void shouldReturnCompletedFalse() {
    assertThat(status.isComplete()).isFalse();
  }

  @Test
  void shouldReturnInitialCurrentPlayerAsCrosses() {
    assertThat(status.getCurrentPlayerToken()).contains('X');
  }

  @Test
  void shouldNotThrowExceptionIfIsPlayersTurn() {
    assertThatCode(() -> status.validateIsTurn('X')).doesNotThrowAnyException();
  }

  @Test
  void shouldThrowExceptionIfNotPlayersTurn() {
    Throwable error = catchThrowable(() -> status.validateIsTurn('O'));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("player with token O is not next player so cannot take turn");
  }

  @Test
  void shouldIncrementTurn() {
    Status updated = status.turnTaken();

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldChangeNextPlayersTurn() {
    Status updated = status.turnTaken();

    assertThat(updated.getCurrentPlayerToken()).contains('O');
  }

  @Test
  void shouldIncrementTurnOnDrawGameTurn() {
    Status updated = status.drawGameTurnTaken();

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldSetCompleteTrueOnDrawGameTurn() {
    Status updated = status.drawGameTurnTaken();

    assertThat(updated.isComplete()).isTrue();
  }

  @Test
  void shouldReturnEmptyNextPlayerTurnAfterDrawGameTurn() {
    Status updated = status.drawGameTurnTaken();

    assertThat(updated.getCurrentPlayerToken()).isEmpty();
  }

  @Test
  void shouldIncrementTurnOnWinningTurn() {
    char winner = 'X';

    Status updated = status.winningTurnTaken(winner);

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldSetCompleteTrueOnWinningTurn() {
    char winner = 'X';

    Status updated = status.winningTurnTaken(winner);

    assertThat(updated.isComplete()).isTrue();
  }

  @Test
  void shouldReturnEmptyNextPlayerTurnAfterWinningTurn() {
    char winner = 'X';

    Status updated = status.winningTurnTaken(winner);

    assertThat(updated.getCurrentPlayerToken()).isEmpty();
  }

  @Test
  void shouldSetWinnerAfterWinningTurn() {
    char winner = 'X';

    Status updated = status.winningTurnTaken(winner);

    assertThat(updated.getWinner()).contains(winner);
  }

  @Test
  void shouldReturnIsDrawFalseIfGameNotComplete() {
    assertThat(status.isDraw()).isFalse();
  }

  @Test
  void shouldReturnIsDrawTrueIfGameCompleteWithWinner() {
    Status winnerStatus = status.winningTurnTaken('X');

    assertThat(winnerStatus.isDraw()).isFalse();
  }

  @Test
  void shouldReturnIsDrawTrueIfGameCompleteWithNoWinner() {
    Status drawStatus = status.drawGameTurnTaken();

    assertThat(drawStatus.isDraw()).isTrue();
  }
}
