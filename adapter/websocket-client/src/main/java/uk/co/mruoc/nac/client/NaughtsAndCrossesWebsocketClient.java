package uk.co.mruoc.nac.client;

import java.net.URI;
import java.util.List;
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
import uk.co.mruoc.nac.api.dto.ApiGame;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesWebsocketClient implements AutoCloseable {

  private final URI uri;
  private final WebSocketStompClient stompClient;
  private final StompHeaders connectHeaders;
  private final GameSessionHandler sessionHandler;

  public NaughtsAndCrossesWebsocketClient(String baseUrl) {
    this(baseUrl, new StompHeaders());
  }

  public NaughtsAndCrossesWebsocketClient(String baseUrl, String token) {
    this(baseUrl, new AuthorizationStompHeaders(token));
  }

  public NaughtsAndCrossesWebsocketClient(String baseUrl, StompHeaders connectHeaders) {
    this(toUri(baseUrl), buildStompWebsocketClient(), connectHeaders, new GameSessionHandler());
  }

  public void connect() {
    try {
      StompSession session =
          stompClient.connectAsync(uri, null, connectHeaders, sessionHandler).get();
      sessionHandler.setSession(session);
      log.info("connected to session with id {}", session.getSessionId());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new NaughtsAndCrossesWebsocketClientException(e);
    } catch (ExecutionException e) {
      throw new NaughtsAndCrossesWebsocketClientException(e);
    }
  }

  public GameEventSubscriber<ApiGame> subscribeToGameUpdateEvents() {
    StompGameEventSubscriber<ApiGame> subscriber = new GameUpdateSubscriber();
    sessionHandler.addSubscriber(subscriber);
    return subscriber;
  }

  public GameEventSubscriber<Long> subscribeToGameDeleteEvents() {
    StompGameEventSubscriber<Long> subscriber = new GameDeleteSubscriber();
    sessionHandler.addSubscriber(subscriber);
    return subscriber;
  }

  public void close() {
    sessionHandler.close();
  }

  private static URI toUri(String baseUrl) {
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
