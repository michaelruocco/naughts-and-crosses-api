package uk.co.mruoc.nac.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;
import uk.co.mruoc.nac.api.dto.ApiGame;

class UpdateDeleteSubscriberTest {

  private final GameUpdateSubscriber subscriber = new GameUpdateSubscriber();

  @Test
  void shouldNotStorePayloadIfNotAnApiGameValue() {
    StompHeaders headers = mock(StompHeaders.class);
    Object payload = new Object();

    subscriber.handleFrame(headers, payload);

    assertThat(subscriber.getAll()).isEmpty();
  }

  @Test
  void shouldNotStorePayloadIfApiGameValue() {
    StompHeaders headers = mock(StompHeaders.class);
    ApiGame payload1 = ApiGame.builder().build();
    ApiGame payload2 = ApiGame.builder().build();

    subscriber.handleFrame(headers, payload1);
    subscriber.handleFrame(headers, payload2);

    assertThat(subscriber.getAll()).containsExactly(payload1, payload2);
  }
}
