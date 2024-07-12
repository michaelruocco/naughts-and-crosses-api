package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class VirusScanExceptionTest {

  @Test
  void shouldReturnMessage() {
    String message = "error-message";

    Throwable error = new VirusScanException(message);

    assertThat(error.getMessage()).isEqualTo(message);
  }

  @Test
  void shouldReturnCause() {
    Throwable cause = new RuntimeException();

    Throwable error = new VirusScanException(cause);

    assertThat(error.getCause()).isEqualTo(cause);
  }
}
