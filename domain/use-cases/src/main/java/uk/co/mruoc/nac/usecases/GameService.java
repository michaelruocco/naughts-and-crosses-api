package uk.co.mruoc.nac.usecases;

import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.Turn;

@Slf4j
@Builder
public class GameService {

  private final GameFactory factory;
  private final GameRepository repository;
  private final BoardFormatter formatter;
  private final GameEventPublisher eventPublisher;

  public Game createGame(Players players) {
    Game game = factory.buildGame(players);
    create(game);
    return game;
  }

  public Game takeTurn(long id, Turn turn) {
    log.info("taking turn for game with id {} {}", id, turn);
    Game game = get(id);
    Game updatedGame = game.take(turn);
    update(updatedGame);
    log.info("game {} board\n{}", id, formatter.format(updatedGame.getBoard()));
    return updatedGame;
  }

  public Stream<Game> getAll() {
    return repository.getAll();
  }

  public Game get(long id) {
    return repository.get(id).orElseThrow(() -> new GameNotFoundException(id));
  }

  private void create(Game game) {
    repository.create(game);
    eventPublisher.updated(game);
  }

  private void update(Game game) {
    repository.update(game);
    eventPublisher.updated(game);
  }

  public void deleteAll() {
    repository.deleteAll();
  }
}
