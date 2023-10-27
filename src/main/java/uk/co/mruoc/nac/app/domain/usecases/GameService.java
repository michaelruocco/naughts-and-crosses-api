package uk.co.mruoc.nac.app.domain.usecases;

import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Turn;

@Slf4j
@Builder
public class GameService {

    private final GameFactory factory;
    private final GameRepository repository;
    private final BoardFormatter formatter;
    private final GameEventPublisher eventPublisher;

    public Game createGame() {
        Game game = factory.buildGame();
        save(game);
        return game;
    }

    public Game takeTurn(long id, Turn turn) {
        Game game = get(id);
        Game updatedGame = game.take(turn);
        save(updatedGame);
        log.info("game {} board\n{}", id, formatter.format(updatedGame.getBoard()));
        return updatedGame;
    }

    public Stream<Game> getAll() {
        return repository.getAll();
    }

    public Game get(long id) {
        return repository.find(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    private void save(Game game) {
        repository.save(game);
        eventPublisher.created(game);
    }
}
