package uk.co.mruoc.nac.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.usecases.GameRepository;

class InMemoryGameRepositoryTest {

    private final GameRepository repository = new InMemoryGameRepository();

    @Test
    void shouldSaveGame() {
        long id = 1;
        Game game = Game.builder().id(id).build();

        repository.save(game);

        assertThat(repository.find(id)).contains(game);
    }

    @Test
    void shouldReturnEmptyIfGameWithIdPresent() {
        long id = 2;

        Optional<Game> game = repository.find(id);

        assertThat(game).isEmpty();
    }
}
