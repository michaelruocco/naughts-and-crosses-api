package uk.co.mruoc.nac.client;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import uk.co.mruoc.nac.api.dto.ApiGame;

@RequiredArgsConstructor
@Slf4j
public class GameUpdateSubscriber implements StompGameEventSubscriber<ApiGame> {

  private final Collection<ApiGame> updates;

  public GameUpdateSubscriber() {
    this(new ArrayList<>());
  }

  @Override
  public String getDestination() {
    return "/topic/game-update";
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return ApiGame.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    if (payload instanceof ApiGame game) {
      log.info("handling payload {}", payload);
      updates.add(game);
      return;
    }
    log.info("subscriber does not support type {}", payload.getClass());
  }

  @Override
  public Stream<ApiGame> getAll() {
    return updates.stream();
  }
}
