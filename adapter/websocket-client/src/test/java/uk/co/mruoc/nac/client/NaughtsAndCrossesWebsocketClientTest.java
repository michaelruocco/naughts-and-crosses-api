package uk.co.mruoc.nac.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;

class NaughtsAndCrossesWebsocketClientTest {

  private static final URI WEB_SOCKET_URI = URI.create("http://localhost:8080");

  private final WebSocketStompClient stompClient = mock(WebSocketStompClient.class);
  private final GameUpdateStompSessionHandler sessionHandler =
      mock(GameUpdateStompSessionHandler.class);
  private final StompHeaders connectHeaders = new StompHeaders();

  private final NaughtsAndCrossesWebsocketClient client =
      new NaughtsAndCrossesWebsocketClient(
          WEB_SOCKET_URI, stompClient, sessionHandler, connectHeaders);

  @Test
  void shouldThrowExceptionIfConnectionThrowsInterruptedException() {
    Throwable cause = new InterruptedException("interrupted-error");
    CompletableFuture<StompSession> future = CompletableFuture.failedFuture(cause);
    when(stompClient.connectAsync(WEB_SOCKET_URI, null, connectHeaders, sessionHandler))
        .thenReturn(future);

    Throwable error = catchThrowable(client::connect);

    assertThat(error)
        .isInstanceOf(NaughtsAndCrossesWebsocketClientException.class)
        .hasMessageEndingWith(cause.getMessage());
  }

  @Test
  void shouldThrowExceptionIfConnectionThrowsExecutionException() {
    Throwable cause = new ExecutionException(new Exception("execution-error"));
    CompletableFuture<StompSession> future = CompletableFuture.failedFuture(cause);
    when(stompClient.connectAsync(WEB_SOCKET_URI, null, connectHeaders, sessionHandler))
        .thenReturn(future);

    Throwable error = catchThrowable(client::connect);

    assertThat(error)
        .isInstanceOf(NaughtsAndCrossesWebsocketClientException.class)
        .hasMessageEndingWith(cause.getMessage());
  }
}
