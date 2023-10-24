package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.dto.ApiGame;
import uk.co.mruoc.nac.app.domain.entities.Game;

@RequiredArgsConstructor
public class ApiGameConverter {

    private final ApiStatusConverter statusConverter;
    private final ApiBoardConverter boardConverter;
    private final ApiPlayerConverter playerConverter;

    public ApiGameConverter() {
        this(new ApiStatusConverter(), new ApiBoardConverter(), new ApiPlayerConverter());
    }

    public ApiGame toMinimalApiGame(Game game) {
        return toApiGameBuilder(game).build();
    }

    public ApiGame toApiGame(Game game) {
        return toApiGameBuilder(game)
                .board(boardConverter.toApiBoard(game.getBoard()))
                .players(playerConverter.toApiPlayers(game.getPlayers()))
                .build();
    }

    private ApiGame.ApiGameBuilder toApiGameBuilder(Game game) {
        return ApiGame.builder().id(game.getId()).status(statusConverter.toApiStatus(game.getStatus()));
    }
}
