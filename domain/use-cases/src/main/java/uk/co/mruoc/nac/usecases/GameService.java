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

  private final AuthenticatedUserValidator userValidator;
  private final GameFactory factory;
  private final TurnTaker turnTaker;
  private final GameRepository repository;
  private final BoardFormatter formatter;
  private final GameEventPublisher eventPublisher;

  public Game createGame(Players players) {
    Game game = factory.buildGame(players);
    create(game);
    return game;
  }

  public Game takeTurn(long id, Turn turn) {
    Game game = get(id);
    Game updatedGame = turnTaker.takeTurn(game, turn);
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

  public void deleteAll() {
    userValidator.validateIsAdmin();
    Stream<Game> games = getAll();
    repository.deleteAll();
    games.forEach(game -> eventPublisher.deleted(game.getId()));
  }

  public void delete(long id) {
    userValidator.validateIsAdmin();
    repository.delete(id);
    eventPublisher.deleted(id);
  }

  private void create(Game game) {
    repository.create(game);
    eventPublisher.updated(game);
  }

  private void update(Game game) {
    repository.update(game);
    eventPublisher.updated(game);
  }
}
