package uk.co.mruoc.nac.client;

import java.lang.reflect.Type;
import java.util.stream.Stream;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaders;

@RequiredArgsConstructor
@Data
public class FakeStompGameEventSubscriber implements StompGameEventSubscriber<Object> {

  private final String destination;

  public FakeStompGameEventSubscriber() {
    this("/topic/test-destination");
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Object.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    // intentionally blank
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public Stream<Object> getAll() {
    return Stream.of();
  }
}
