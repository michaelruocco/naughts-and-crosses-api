package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class ApiGameConverter {

  private final ApiStatusConverter statusConverter;
  private final ApiBoardConverter boardConverter;
  private final ApiPlayerConverter playerConverter;

  public ApiGameConverter() {
    this(new ApiStatusConverter(), new ApiBoardConverter(), new ApiPlayerConverter());
  }

  public ApiGame toMinimalApiGame(Game game) {
    return ApiGame.builder()
        .id(game.getId())
        .status(statusConverter.toApiStatus(game.getStatus()))
        .players(playerConverter.toMinimalPlayers(game.getPlayers()))
        .build();
  }

  public ApiGame toApiGame(Game game) {
    return ApiGame.builder()
        .id(game.getId())
        .status(statusConverter.toApiStatus(game.getStatus()))
        .board(boardConverter.toApiBoard(game.getBoard()))
        // TODO can we use minimal players here
        .players(playerConverter.toApiPlayers(game.getPlayers()))
        .build();
  }
}
