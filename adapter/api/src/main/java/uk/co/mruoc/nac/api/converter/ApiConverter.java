package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiGamePage;
import uk.co.mruoc.nac.api.dto.ApiGamePageRequest;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePage;
import uk.co.mruoc.nac.entities.GamePageRequest;
import uk.co.mruoc.nac.entities.Turn;
import uk.co.mruoc.nac.usecases.AuthenticatedUserSupplier;

@RequiredArgsConstructor
public class ApiConverter {

  private final ApiGameConverter gameConverter;
  private final ApiTurnConverter turnConverter;
  private final ApiRequestedPlayerConverter requestedPlayerConverter;

  public ApiConverter(AuthenticatedUserSupplier userSupplier) {
    this(
        new ApiGameConverter(),
        new ApiTurnConverter(userSupplier),
        new ApiRequestedPlayerConverter());
  }

  public ApiGame toMinimalApiGame(Game game) {
    return gameConverter.toMinimalApiGame(game);
  }

  public ApiGamePage toApiGamePage(GamePage page, Function<Game, ApiGame> converter) {
    return ApiGamePage.builder()
        .total(page.getTotal())
        .games(toApiGames(page.getGames(), converter))
        .build();
  }

  private Collection<ApiGame> toApiGames(
      Collection<Game> games, Function<Game, ApiGame> converter) {
    return games.stream().map(converter).toList();
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

  public GamePageRequest toRequest(ApiGamePageRequest apiRequest) {
    return GamePageRequest.builder()
        .limit(apiRequest.getLimit())
        .offset(apiRequest.getOffset())
        .complete(apiRequest.getComplete())
        .username(apiRequest.getUsername())
        .build();
  }
}
