package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class StatusTest {

  private static final Player CROSSES_PLAYER = PlayerMother.crossesPlayer();
  private static final Player NAUGHTS_PLAYER = PlayerMother.naughtsPlayer();

  private final Status status = new Status(PlayerMother.of(CROSSES_PLAYER, NAUGHTS_PLAYER));

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
    assertThat(status.getCurrentPlayer()).contains(CROSSES_PLAYER);
  }

  @Test
  void shouldNotThrowExceptionIfIsPlayersTurn() {
    Turn turn = new Turn(0, 0, CROSSES_PLAYER);

    ThrowingCallable call = () -> status.validate(turn);

    assertThatCode(call).doesNotThrowAnyException();
  }

  @Test
  void shouldThrowExceptionIfTurnHasIncorrectPlayer() {
    Turn turn = new Turn(0, 0, NAUGHTS_PLAYER);

    Throwable error = catchThrowable(() -> status.validate(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for username user-2");
  }

  @Test
  void shouldThrowExceptionIfGameIsComplete() {
    Turn turn = new Turn(0, 0, CROSSES_PLAYER);
    Status completeStatus = status.winningTurnTaken(CROSSES_PLAYER.getToken());

    Throwable error = catchThrowable(() -> completeStatus.validate(turn));

    assertThat(error)
        .isInstanceOf(NotPlayersTurnException.class)
        .hasMessage("not turn for username user-1");
  }

  @Test
  void shouldIncrementTurn() {
    Status updated = status.turnTaken();

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldChangeNextPlayersTurn() {
    Status updated = status.turnTaken();

    assertThat(updated.getCurrentPlayer()).contains(NAUGHTS_PLAYER);
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

    assertThat(updated.getCurrentPlayer()).isEmpty();
  }

  @Test
  void shouldIncrementTurnOnWinningTurn() {
    char token = CROSSES_PLAYER.getToken();

    Status updated = status.winningTurnTaken(token);

    assertThat(updated.getTurn()).isOne();
  }

  @Test
  void shouldSetCompleteTrueOnWinningTurn() {
    char token = CROSSES_PLAYER.getToken();

    Status updated = status.winningTurnTaken(token);

    assertThat(updated.isComplete()).isTrue();
  }

  @Test
  void shouldReturnEmptyNextPlayerTurnAfterWinningTurn() {
    char token = CROSSES_PLAYER.getToken();

    Status updated = status.winningTurnTaken(token);

    assertThat(updated.getCurrentPlayer()).isEmpty();
  }

  @Test
  void shouldSetWinnerAfterWinningTurn() {
    char token = CROSSES_PLAYER.getToken();

    Status updated = status.winningTurnTaken(token);

    assertThat(updated.getWinner()).contains(CROSSES_PLAYER);
  }

  @Test
  void shouldReturnIsDrawFalseIfGameNotComplete() {
    assertThat(status.isDraw()).isFalse();
  }

  @Test
  void shouldReturnIsDrawTrueIfGameCompleteWithWinner() {
    char token = CROSSES_PLAYER.getToken();

    Status updated = status.winningTurnTaken(token);

    assertThat(updated.isDraw()).isFalse();
  }

  @Test
  void shouldReturnIsDrawTrueIfGameCompleteWithNoWinner() {
    Status drawStatus = status.drawGameTurnTaken();

    assertThat(drawStatus.isDraw()).isTrue();
  }
}
