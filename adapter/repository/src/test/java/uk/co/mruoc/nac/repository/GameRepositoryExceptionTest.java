package uk.co.mruoc.nac.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameRepositoryExceptionTest {

  @Test
  void shouldReturnCause() {
    Throwable cause = new Exception();

    Throwable error = new GameRepositoryException(cause);

    assertThat(error.getCause()).isEqualTo(cause);
  }

  @Test
  void shouldReturnMessage() {
    String message = "error message";

    Throwable error = new GameRepositoryException(message);

    assertThat(error.getMessage()).isEqualTo(message);
  }
}
