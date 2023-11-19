package uk.co.mruoc.nac.client;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import uk.co.mruoc.nac.api.dto.ApiGame;

@Slf4j
@RequiredArgsConstructor
public class GameUpdateStompSessionHandler extends StompSessionHandlerAdapter {

  private final String destination;

  private final Collection<GameUpdateListener> listeners;

  private StompSession.Subscription subscription;

  public GameUpdateStompSessionHandler() {
    this("/topic/game-update", new ArrayList<>());
  }

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    log.debug("session established with id {}", session.getSessionId());
    this.subscription = session.subscribe(destination, this);
    log.debug("subscribed to {} with id {}", destination, subscription.getSubscriptionId());
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return ApiGame.class;
  }

  @Override
  public void handleException(
      StompSession session,
      StompCommand command,
      StompHeaders headers,
      byte[] payload,
      Throwable exception) {
    log.error("received error", exception);
    log.error(
        "session id {} command {} headers {} payload {}",
        session.getSessionId(),
        command.getMessageType(),
        headers,
        new String(payload));
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    log.debug("received payload {} with headers {}", payload, headers);
    if (payload instanceof ApiGame game) {
      listeners.forEach(listener -> listener.updated(game));
    }
  }

  public void unsubscribe() {
    log.debug("unsubscribing from subscription {}", subscription.getSubscriptionId());
    subscription.unsubscribe();
  }

  public void add(GameUpdateListener listener) {
    listeners.add(listener);
  }

  public void removeAllListeners() {
    listeners.clear();
  }
}
