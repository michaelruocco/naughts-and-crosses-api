package uk.co.mruoc.nac.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NaughtsAndCrossesApiClientExceptionTest {

  @Test
  void shouldReturnMessage() {
    String message = "message-1";

    Throwable t = new NaughtsAndCrossesApiClientException(message);

    assertThat(t.getMessage()).isEqualTo(message);
  }
}
