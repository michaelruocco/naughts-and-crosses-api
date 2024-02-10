package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Turn;

@RequiredArgsConstructor
public class ApiConverter {

  private final ApiGameConverter gameConverter;
  private final ApiTurnConverter turnConverter;
  private final ApiRequestedPlayerConverter requestedPlayerConverter;

  public ApiConverter() {
    this(new ApiGameConverter(), new ApiTurnConverter(), new ApiRequestedPlayerConverter());
  }

  public ApiGame toMinimalApiGame(Game game) {
    return gameConverter.toMinimalApiGame(game);
  }

  public ApiGame toApiGame(Game game) {
    return gameConverter.toApiGame(game);
  }

  public Turn toTurn(ApiTurn apiTurn) {
    return turnConverter.toTurn(apiTurn);
  }

  public CreateGameRequest toCreateGameRequest(ApiCreateGameRequest apiRequest) {
    return new CreateGameRequest(
        requestedPlayerConverter.toRequestedPlayers(apiRequest.getRequestedPlayers()));
  }
}
