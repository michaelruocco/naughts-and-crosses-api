package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.ApiGame;
import uk.co.mruoc.nac.app.domain.entities.Game;

@RequiredArgsConstructor
public class ApiGameConverter {

    private final ApiStatusConverter statusConverter;
    private final ApiPlayerConverter playerConverter;
    private final ApiBoardConverter boardConverter;

    public ApiGameConverter() {
        this(new ApiPlayerConverter());
    }

    public ApiGameConverter(ApiPlayerConverter playerConverter) {
        this(new ApiStatusConverter(playerConverter), playerConverter, new ApiBoardConverter());
    }

    public ApiGame toApiGame(Game game) {
        return ApiGame.builder()
                .id(game.getId())
                .status(statusConverter.toApiStatus(game.getStatus()))
                .players(playerConverter.toApiPlayers(game.getPlayers()))
                .board(boardConverter.toApiBoard(game.getBoard()))
                .build();
    }
}
