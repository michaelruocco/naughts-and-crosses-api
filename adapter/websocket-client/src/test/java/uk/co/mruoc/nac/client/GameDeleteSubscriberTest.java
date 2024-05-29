package uk.co.mruoc.nac.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;

class GameDeleteSubscriberTest {

  private final GameDeleteSubscriber subscriber = new GameDeleteSubscriber();

  @Test
  void shouldNotStorePayloadIfNotALongValue() {
    StompHeaders headers = mock(StompHeaders.class);
    Object payload = new Object();

    subscriber.handleFrame(headers, payload);

    assertThat(subscriber.getAll()).isEmpty();
  }

  @Test
  void shouldNotStorePayloadIfLongValue() {
    StompHeaders headers = mock(StompHeaders.class);
    long payload1 = 1L;
    long payload2 = 2L;

    subscriber.handleFrame(headers, payload1);
    subscriber.handleFrame(headers, payload2);

    assertThat(subscriber.getAll()).containsExactly(payload1, payload2);
  }
}
