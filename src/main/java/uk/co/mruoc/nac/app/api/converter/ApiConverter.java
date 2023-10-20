package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.ApiGame;
import uk.co.mruoc.nac.app.api.ApiTurn;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Turn;

@RequiredArgsConstructor
public class ApiConverter {

    private final ApiGameConverter gameConverter;
    private final ApiTurnConverter turnConverter;

    public ApiConverter() {
        this(new ApiGameConverter(), new ApiTurnConverter());
    }

    public ApiGame toApiGame(Game game) {
        return gameConverter.toApiGame(game);
    }

    public Turn toTurn(ApiTurn apiTurn) {
        return turnConverter.toTurn(apiTurn);
    }
}
