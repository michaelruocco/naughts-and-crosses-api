package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.Game;

public interface GameRepository {

    Optional<Game> find(long id);

    void save(Game game);

    Stream<Game> getAll();

    void deleteAll();
}
