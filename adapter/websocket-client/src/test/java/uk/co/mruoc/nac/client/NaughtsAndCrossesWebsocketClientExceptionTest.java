package uk.co.mruoc.nac.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NaughtsAndCrossesWebsocketClientExceptionTest {

  @Test
  void shouldReturnCause() {
    Throwable cause = new Exception();

    Throwable error = new NaughtsAndCrossesWebsocketClientException(cause);

    assertThat(error.getCause()).isEqualTo(cause);
  }
}
