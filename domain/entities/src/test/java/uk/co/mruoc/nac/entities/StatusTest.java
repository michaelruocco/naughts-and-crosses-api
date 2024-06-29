package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class StatusTest {

  private static final char CROSS = 'X';
  private static final char NAUGHT = 'O';

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
    assertThat(status.getCurrentPlayerToken()).contains(CROSS);
  }

  @Test
  void shouldNotThrowExceptionIfIsPlayersTurn() {
    Turn turn = Turn.builder().token(CROSS).username("user-1").build();

    ThrowingCallable call = () -> status.validatePlayerTurn(turn);

    assertThatCode(call).doesNotThrowAnyException();
  }

  @Test
  void shouldThrowExceptionIfTurnHasIncorrectToken() {
    Turn turn = Turn.builder().token(NAUGHT).username("user-1").build();

    Throwable error = catchThrowable(() -> status.validatePlayerTurn(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for token O");
  }

  @Test
  void shouldThrowExceptionIfTurnHasIncorrectUsername() {
    Turn turn = Turn.builder().token(CROSS).username("user-2").build();

    Throwable error = catchThrowable(() -> status.validatePlayerTurn(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for username user-2");
  }

  @Test
  void shouldThrowExceptionIfGameIsComplete() {
    Turn turn = Turn.builder().token(CROSS).username("user-1").build();
    Status completeStatus = status.winningTurnTaken(CROSS);

    Throwable error = catchThrowable(() -> completeStatus.validatePlayerTurn(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for token X and username user-1");
  }

  @Test
  void shouldIncrementTurn() {
    Status updated = status.turnTaken();

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldChangeNextPlayersTurn() {
    Status updated = status.turnTaken();

    assertThat(updated.getCurrentPlayerToken()).contains(NAUGHT);
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
    Status updated = status.winningTurnTaken(CROSS);

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldSetCompleteTrueOnWinningTurn() {
    Status updated = status.winningTurnTaken(CROSS);

    assertThat(updated.isComplete()).isTrue();
  }

  @Test
  void shouldReturnEmptyNextPlayerTurnAfterWinningTurn() {
    Status updated = status.winningTurnTaken(CROSS);

    assertThat(updated.getCurrentPlayerToken()).isEmpty();
  }

  @Test
  void shouldSetWinnerAfterWinningTurn() {
    char winner = CROSS;

    Status updated = status.winningTurnTaken(winner);

    assertThat(updated.getWinner()).contains(winner);
  }

  @Test
  void shouldReturnIsDrawFalseIfGameNotComplete() {
    assertThat(status.isDraw()).isFalse();
  }

  @Test
  void shouldReturnIsDrawTrueIfGameCompleteWithWinner() {
    Status winnerStatus = status.winningTurnTaken(CROSS);

    assertThat(winnerStatus.isDraw()).isFalse();
  }

  @Test
  void shouldReturnIsDrawTrueIfGameCompleteWithNoWinner() {
    Status drawStatus = status.drawGameTurnTaken();

    assertThat(drawStatus.isDraw()).isTrue();
  }
}
