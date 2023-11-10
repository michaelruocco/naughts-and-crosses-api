package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.Game;

public interface GameRepository {

  void create(Game game);

  Optional<Game> find(long id);

  void update(Game game);

  Stream<Game> getAll();

  void deleteAll();
}
