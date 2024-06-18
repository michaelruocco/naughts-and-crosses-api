package uk.co.mruoc.nac.client;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;

@RequiredArgsConstructor
@Slf4j
public class GameDeleteSubscriber implements StompGameEventSubscriber<Long> {

  private final Collection<Long> deletedIds;

  public GameDeleteSubscriber() {
    this(new ArrayList<>());
  }

  @Override
  public String getDestination() {
    return "/topic/game-delete";
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Long.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    if (payload instanceof Long id) {
      log.info("handling payload {}", payload);
      deletedIds.add(id);
      return;
    }
    log.info("handler does not support type {}", payload.getClass());
  }

  @Override
  public Stream<Long> getAll() {
    return deletedIds.stream();
  }
}
