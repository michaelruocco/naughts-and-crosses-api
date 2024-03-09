package uk.co.mruoc.nac.usecases;

import java.util.stream.Stream;
import lombok.Builder;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.Turn;

@Builder
public class GameFacade {

  private final UserService userService;
  private final GameService gameService;

  public Game createGame(CreateGameRequest request) {
    Players players = userService.toPlayers(request);
    return gameService.createGame(players);
  }

  public Game takeTurn(long id, Turn turn) {
    return gameService.takeTurn(id, turn);
  }

  public Stream<Game> getAll() {
    return gameService.getAll();
  }

  public Game get(long id) {
    return gameService.get(id);
  }

  public void deleteAll() {
    gameService.deleteAll();
  }

    public void delete(long id) {
        gameService.delete(id);
    }
}
