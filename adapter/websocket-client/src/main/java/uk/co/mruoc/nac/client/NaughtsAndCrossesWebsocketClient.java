package uk.co.mruoc.nac.client;

import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesWebsocketClient implements AutoCloseable {

  private final String url;
  private final WebSocketStompClient stompClient;
  private final GameUpdateStompSessionHandler sessionHandler;

  private StompSession session;

  public NaughtsAndCrossesWebsocketClient(String baseUrl) {
    this(toGameEventUrl(baseUrl), buildStompWebsocketClient(), new GameUpdateStompSessionHandler());
  }

  public void connect() {
    try {
      session = stompClient.connectAsync(url, sessionHandler).get();
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

  public void removeAllListeners() {
    sessionHandler.removeAllListeners();
  }

  public void close() {
    sessionHandler.unsubscribe();
    log.debug("disconnecting session {}", session.getSessionId());
    session.disconnect();
  }

  private static String toGameEventUrl(String baseUrl) {
    return String.format("%s/v1/game-events", baseUrl);
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
