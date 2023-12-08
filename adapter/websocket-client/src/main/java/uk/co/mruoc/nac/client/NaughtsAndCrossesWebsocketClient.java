package uk.co.mruoc.nac.client;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesWebsocketClient implements AutoCloseable {

  private final URI uri;
  private final WebSocketStompClient stompClient;
  private final GameUpdateStompSessionHandler sessionHandler;
  private final StompHeaders connectHeaders;

  private StompSession session;

  public NaughtsAndCrossesWebsocketClient(String baseUrl) {
    this(baseUrl, new StompHeaders());
  }

  public NaughtsAndCrossesWebsocketClient(String baseUrl, String token) {
    this(baseUrl, new AuthorizationStompHeaders(token));
  }

  public NaughtsAndCrossesWebsocketClient(String baseUrl, StompHeaders connectHeaders) {
    this(
        toGameEventUri(baseUrl),
        buildStompWebsocketClient(),
        new GameUpdateStompSessionHandler(),
        connectHeaders);
  }

  public void connect() {
    try {
      CompletableFuture<StompSession> future =
          stompClient.connectAsync(uri, null, connectHeaders, sessionHandler);
      session = future.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new NaughtsAndCrossesWebsocketClientException(e);
    } catch (ExecutionException e) {
      throw new NaughtsAndCrossesWebsocketClientException(e);
    }
  }

  public void add(GameUpdateListener listener) {
    sessionHandler.add(listener);
  }

  public void close() {
    sessionHandler.unsubscribe();
    log.debug("disconnecting session {}", session.getSessionId());
    session.disconnect();
  }

  private static URI toGameEventUri(String baseUrl) {
    return URI.create(String.format("%s/v1/game-events", baseUrl));
  }

  private static WebSocketStompClient buildStompWebsocketClient() {
    WebSocketStompClient client = new WebSocketStompClient(buildWebsocketClient());
    client.setMessageConverter(new MappingJackson2MessageConverter());
    return client;
  }

  private static SockJsClient buildWebsocketClient() {
    List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
    return new SockJsClient(transports);
  }
}
