package uk.co.mruoc.nac.client;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Slf4j
@RequiredArgsConstructor
public class GameSessionHandler extends StompSessionHandlerAdapter implements AutoCloseable {

  private final Collection<Subscription> subscriptions;

  private StompSession session;

  public GameSessionHandler() {
    this(new ArrayList<>());
  }

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    log.info("session connected with id {}", session.getSessionId());
    setSession(session);
  }

  public void setSession(StompSession session) {
    this.session = session;
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Object.class;
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
    log.info("received payload {} with headers {}", payload, headers);
  }

  @Override
  public void close() {
    subscriptions.forEach(Subscription::unsubscribe);
    Optional.ofNullable(session).ifPresent(GameSessionHandler::disconnect);
  }

  public void addSubscriber(StompGameEventSubscriber<?> subscriber) {
    String destination = subscriber.getDestination();
    Subscription subscription = session.subscribe(destination, subscriber);
    subscriptions.add(subscription);
    log.info(
        "subscribed to {} for session {} with subscription id {}",
        destination,
        session.getSessionId(),
        subscription.getSubscriptionId());
  }

  public Collection<String> getSubscriptionIds() {
    return subscriptions.stream().map(Subscription::getSubscriptionId).toList();
  }

  private static void disconnect(StompSession s) {
    log.debug("disconnecting session with id {}", s.getSessionId());
    s.disconnect();
  }
}
