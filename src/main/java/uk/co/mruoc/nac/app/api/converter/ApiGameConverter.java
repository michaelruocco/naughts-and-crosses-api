package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.ApiGame;
import uk.co.mruoc.nac.app.domain.entities.Game;

@RequiredArgsConstructor
public class ApiGameConverter {

    private final ApiStatusConverter statusConverter;
    private final ApiBoardConverter boardConverter;

    public ApiGameConverter() {
        this(new ApiStatusConverter(), new ApiBoardConverter());
    }

    public ApiGame toApiGame(Game game) {
        return ApiGame.builder()
                .id(game.getId())
                .status(statusConverter.toApiStatus(game.getStatus()))
                .board(boardConverter.toApiBoard(game.getBoard()))
                .build();
    }
}
