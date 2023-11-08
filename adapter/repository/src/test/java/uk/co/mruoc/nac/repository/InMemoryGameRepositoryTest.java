package uk.co.mruoc.nac.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.usecases.GameRepository;

class InMemoryGameRepositoryTest {

  private final GameRepository repository = new InMemoryGameRepository();

  @Test
  void shouldSaveGame() {
    long id = 1;
    Game game = givenGameWithId(id);

    repository.save(game);

    assertThat(repository.find(id)).contains(game);
  }

  @Test
  void shouldReturnEmptyIfGameWithIdPresent() {
    long id = 2;

    Optional<Game> game = repository.find(id);

    assertThat(game).isEmpty();
  }

  @Test
  void shouldReturnAllGamesSortedById() {
    Game game3 = givenGameWithId(3);
    Game game2 = givenGameWithId(2);
    Game game1 = givenGameWithId(1);
    repository.save(game3);
    repository.save(game2);
    repository.save(game1);

    Stream<Game> games = repository.getAll();

    assertThat(games).containsExactly(game1, game2, game3);
  }

  @Test
  void shouldDeleteAllGames() {
    repository.save(givenGameWithId(1));
    repository.save(givenGameWithId(2));
    repository.save(givenGameWithId(3));

    repository.deleteAll();

    assertThat(repository.getAll()).isEmpty();
  }

  private static Game givenGameWithId(long id) {
    Game game = mock(Game.class);
    when(game.getId()).thenReturn(id);
    return game;
  }
}
