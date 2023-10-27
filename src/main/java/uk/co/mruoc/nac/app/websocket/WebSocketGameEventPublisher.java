package uk.co.mruoc.nac.app.websocket;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import uk.co.mruoc.nac.app.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.api.dto.ApiGame;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.usecases.GameEventPublisher;

@Builder
@Slf4j
public class WebSocketGameEventPublisher implements GameEventPublisher {

    private final SimpMessagingTemplate template;
    private final ApiConverter converter;

    @Override
    public void created(Game game) {
        ApiGame apiGame = converter.toApiGame(game);
        log.info("sending web socket game updated {}", game);
        template.convertAndSend("/topic/game-updated", apiGame);
    }
}
