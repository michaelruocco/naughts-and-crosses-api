package uk.co.mruoc.nac.client;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;

class GameSessionHandlerTest {

  private final GameSessionHandler handler = new GameSessionHandler();

  @Test
  void shouldReturnObjectType() {
    StompHeaders headers = mock(StompHeaders.class);

    Type type = handler.getPayloadType(headers);

    assertThat(type).isEqualTo(Object.class);
  }

  @Test
  void shouldLogError() throws Exception {
    StompSession session = givenSession();
    StompCommand command = givenCommand();
    StompHeaders headers = givenHeaders();
    byte[] payload = "test-payload".getBytes();
    Throwable error = new Exception("boom!");

    String output =
        tapSystemErr(
            () -> {
              handler.handleException(session, command, headers, payload, error);
            });

    assertThat(output)
        .containsIgnoringWhitespaces("received error java.lang.Exception: boom!")
        .contains("session id mock-session-id")
        .contains("command CONNECT")
        .contains("headers mock-headers")
        .contains("payload test-payload");
  }

  @Test
  void shouldRetainSubscriptionWhenAddingSubscriber() {
    StompSession session = givenSession();
    givenConnectedSession(session);
    StompGameEventSubscriber<Object> subscriber =
        new FakeStompGameEventSubscriber("/topic/destination");
    givenSubscriptionForSubscriber(session, subscriber);
    String expectedSubscriptionId = "test-subscription-id";
    Subscription subscription = givenSubscriptionWithId(expectedSubscriptionId);
    when(session.subscribe(subscriber.getDestination(), subscriber)).thenReturn(subscription);

    handler.addSubscriber(subscriber);

    assertThat(handler.getSubscriptionIds()).containsExactly(expectedSubscriptionId);
  }

  @Test
  void shouldUnsubscribeAllSubscriptionsThenDisconnectSession() {
    StompSession session = givenSession();
    givenConnectedSession(session);
    StompGameEventSubscriber<Object> subscriber1 =
        new FakeStompGameEventSubscriber("destination-1");
    Subscription subscription1 = givenSubscriptionForSubscriber(session, subscriber1);
    StompGameEventSubscriber<Object> subscriber2 =
        new FakeStompGameEventSubscriber("destination-2");
    Subscription subscription2 = givenSubscriptionForSubscriber(session, subscriber2);
    handler.addSubscriber(subscriber1);
    handler.addSubscriber(subscriber2);

    handler.close();

    InOrder inOrder = inOrder(subscription1, subscription2, session);
    inOrder.verify(subscription1).unsubscribe();
    inOrder.verify(subscription2).unsubscribe();
    inOrder.verify(session).disconnect();
  }

  private Subscription givenSubscriptionForSubscriber(
      StompSession session, StompGameEventSubscriber<Object> subscriber) {
    Subscription subscription = mock(Subscription.class);
    when(session.subscribe(subscriber.getDestination(), subscriber)).thenReturn(subscription);
    return subscription;
  }

  private void givenConnectedSession(StompSession session) {
    StompHeaders headers = givenHeaders();
    handler.afterConnected(session, headers);
  }

  private Subscription givenSubscriptionWithId(String id) {
    Subscription subscription = mock(Subscription.class);
    when(subscription.getSubscriptionId()).thenReturn(id);
    return subscription;
  }

  private StompSession givenSession() {
    StompSession session = mock(StompSession.class);
    when(session.getSessionId()).thenReturn("mock-session-id");
    return session;
  }

  private StompCommand givenCommand() {
    StompCommand command = mock(StompCommand.class);
    when(command.getMessageType()).thenReturn(SimpMessageType.CONNECT);
    return command;
  }

  private StompHeaders givenHeaders() {
    StompHeaders headers = mock(StompHeaders.class);
    when(headers.toString()).thenReturn("mock-headers");
    return headers;
  }
}
