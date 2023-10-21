package uk.co.mruoc.nac.app.domain.usecases;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Turn;
import uk.co.mruoc.nac.app.repository.GameRepository;

@Slf4j
@RequiredArgsConstructor
public class GameService {

    private final GameFactory factory;
    private final GameRepository repository;
    private final BoardFormatter formatter;

    public GameService() {
        this(new GameFactory(), new GameRepository(), new BoardFormatter());
    }

    public Game createGame() {
        Game game = factory.buildGame();
        save(game);
        return game;
    }

    public Game takeTurn(UUID id, Turn turn) {
        Game game = findGame(id);
        Game updatedGame = game.take(turn);
        save(updatedGame);
        log.info("game {} board\n{}", id, formatter.format(updatedGame.getBoard()));
        return updatedGame;
    }

    private Game findGame(UUID id) {
        return repository.find(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    private void save(Game game) {
        repository.save(game);
    }
}
