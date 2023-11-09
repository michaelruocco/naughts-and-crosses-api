package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import org.junit.jupiter.api.Test;

class StatusTest {

  private final Status status = new Status();

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
  void shouldIncrementTurnOnGameEndingTurn() {
    Status updated = status.gameEndingTurnTaken();

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldSetCompleteTrueOnWinningTurn() {
    Status updated = status.gameEndingTurnTaken();

    assertThat(updated.isComplete()).isTrue();
  }

  @Test
  void shouldReturnEmptyNextPlayerTurnAfterGameEndingTurn() {
    Status updated = status.gameEndingTurnTaken();

    assertThat(updated.getCurrentPlayerToken()).isEmpty();
  }
}
