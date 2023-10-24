package uk.co.mruoc.nac.app.websocket;

import lombok.Builder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import uk.co.mruoc.nac.app.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.api.dto.ApiGame;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.usecases.GameEventPublisher;

@Builder
public class WebSocketGameEventPublisher implements GameEventPublisher {

    private final SimpMessagingTemplate template;
    private final ApiConverter converter;

    @Override
    public void created(Game game) {
        ApiGame apiGame = converter.toMinimalApiGame(game);
        template.convertAndSend("/topic/game-updated", apiGame);
    }
}
