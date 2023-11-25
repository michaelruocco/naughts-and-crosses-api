package uk.co.mruoc.nac.client;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErr;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
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
    StompSession session = givenSession();
    StompHeaders headers = givenHeaders();
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
  void shouldSendApiGamePayloadToAllListeners() {
    StompHeaders headers = givenHeaders();
    ApiGame payload = new ApiGame();

    handler.handleFrame(headers, payload);

    verify(listener1).updated(payload);
    verify(listener2).updated(payload);
  }

  @Test
  void shouldDoNothingIfPayloadIsNotApiGame() {
    StompHeaders headers = givenHeaders();
    Object payload = new Object();

    handler.handleFrame(headers, payload);

    verifyNoInteractions(listener1);
    verifyNoInteractions(listener2);
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
