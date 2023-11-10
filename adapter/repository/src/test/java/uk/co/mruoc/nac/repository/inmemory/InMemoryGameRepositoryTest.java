package uk.co.mruoc.nac.repository.inmemory;

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
  void shouldCreateGame() {
    long id = 1;
    Game game = givenGameWithId(id);

    repository.create(game);

    assertThat(repository.find(id)).contains(game);
  }

  @Test
  void shouldUpdateGame() {
    long id = 1;
    Game game = givenGameWithId(id);
    repository.create(game);
    Game updatedGame = givenGameWithId(id);

    repository.update(updatedGame);

    assertThat(repository.find(id)).contains(updatedGame);
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
    repository.create(game3);
    repository.create(game2);
    repository.create(game1);

    Stream<Game> games = repository.getAll();

    assertThat(games).containsExactly(game1, game2, game3);
  }

  @Test
  void shouldDeleteAllGames() {
    repository.create(givenGameWithId(1));
    repository.create(givenGameWithId(2));
    repository.create(givenGameWithId(3));

    repository.deleteAll();

    assertThat(repository.getAll()).isEmpty();
  }

  private static Game givenGameWithId(long id) {
    Game game = mock(Game.class);
    when(game.getId()).thenReturn(id);
    return game;
  }
}
