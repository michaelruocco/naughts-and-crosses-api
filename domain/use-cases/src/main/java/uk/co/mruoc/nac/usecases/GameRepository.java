package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePage;
import uk.co.mruoc.nac.entities.GamePageRequest;

public interface GameRepository {

  void create(Game game);

  Optional<Game> get(long id);

  void update(Game game);

  Stream<Game> getAll();

  void deleteAll();

  void delete(long id);

  GamePage getPage(GamePageRequest request);
}
