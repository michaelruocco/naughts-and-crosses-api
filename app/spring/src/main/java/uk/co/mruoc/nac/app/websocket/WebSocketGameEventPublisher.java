package uk.co.mruoc.nac.app.websocket;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.usecases.GameEventPublisher;

@Builder
@Slf4j
public class WebSocketGameEventPublisher implements GameEventPublisher {

  private final SimpMessagingTemplate template;
  private final ApiConverter converter;

  @Override
  public void updated(Game game) {
    ApiGame apiGame = converter.toApiGame(game);
    log.info("sending web socket update for game with id {}", game.getId());
    template.convertAndSend("/topic/game-update", apiGame);
  }

  @Override
  public void deleted(long id) {
    log.info("sending web socket delete for game with id {}", id);
    template.convertAndSend("/topic/game-delete", id);
  }
}
