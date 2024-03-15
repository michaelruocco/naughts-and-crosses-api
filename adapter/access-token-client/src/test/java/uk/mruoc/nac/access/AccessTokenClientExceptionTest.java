package uk.mruoc.nac.access;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AccessTokenClientExceptionTest {

  @Test
  void shouldReturnMessage() {
    String message = "message-1";

    Throwable t = new AccessTokenClientException(message);

    assertThat(t.getMessage()).isEqualTo(message);
  }

  @Test
  void shouldReturnMessageWhenCausePassed() {
    String message = "message-2";
    Throwable cause = new Exception();

    Throwable t = new AccessTokenClientException(message, cause);

    assertThat(t.getMessage()).isEqualTo(message);
  }

  @Test
  void shouldReturnCause() {
    Throwable cause = new Exception();

    Throwable t = new AccessTokenClientException("message-3", cause);

    assertThat(t.getCause()).isEqualTo(cause);
  }
}
