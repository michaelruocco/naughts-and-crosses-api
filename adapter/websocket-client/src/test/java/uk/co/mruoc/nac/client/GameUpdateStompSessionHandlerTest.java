package uk.co.mruoc.nac.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import uk.co.mruoc.nac.api.dto.ApiGame;

class GameUpdateStompSessionHandlerTest {

  private final String destination = "/test/destination";

  private final GameUpdateListener listener1 = mock(GameUpdateListener.class);
  private final GameUpdateListener listener2 = mock(GameUpdateListener.class);
  private final GameUpdateStompSessionHandler handler =
      new GameUpdateStompSessionHandler(destination, List.of(listener1, listener2));

  @Test
  void shouldSubscribeToDestination() {
    StompSession session = mock(StompSession.class);
    StompHeaders headers = mock(StompHeaders.class);
    Subscription subscription = mock(Subscription.class);
    when(session.subscribe(destination, handler)).thenReturn(subscription);

    handler.afterConnected(session, headers);

    verify(session).subscribe(destination, handler);
  }

  @Test
  void shouldReturnApiGameType() {
    StompHeaders headers = mock(StompHeaders.class);

    Type type = handler.getPayloadType(headers);

    assertThat(type).isEqualTo(ApiGame.class);
  }
}
