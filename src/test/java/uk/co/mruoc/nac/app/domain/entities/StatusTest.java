package uk.co.mruoc.nac.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.app.domain.usecases.NotPlayersTurnException;

class StatusTest {

    private static final char CROSSES = 'X';
    private static final char NAUGHTS = 'O';

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
        assertThat(status.getCurrentPlayerToken()).isEqualTo(CROSSES);
    }

    @Test
    void shouldNotThrowExceptionIfIsPlayersTurn() {
        assertThatCode(() -> status.validateIsTurn(CROSSES)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionIfNotPlayersTurn() {
        Throwable error = catchThrowable(() -> status.validateIsTurn(NAUGHTS));

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

        assertThat(updated.getCurrentPlayerToken()).isEqualTo(NAUGHTS);
    }
}
