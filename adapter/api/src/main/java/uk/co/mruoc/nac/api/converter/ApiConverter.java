package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiPlayer;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.Turn;
import uk.co.mruoc.nac.entities.User;

import java.util.Collection;

@RequiredArgsConstructor
public class ApiConverter {

  private final ApiGameConverter gameConverter;
  private final ApiTurnConverter turnConverter;
  private final ApiPlayerConverter playerConverter;
  private final ApiUserConverter userConverter;

  public ApiConverter() {
    this(
        new ApiGameConverter(),
        new ApiTurnConverter(),
        new ApiPlayerConverter(),
        new ApiUserConverter());
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

  public Players toPlayers(Collection<ApiPlayer> apiPlayers) {
    return playerConverter.toPlayers(apiPlayers);
  }
}
