package uk.co.mruoc.nac.app.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.app.domain.entities.Game;

class GameRepositoryTest {

    private final GameRepository repository = new GameRepository();

    @Test
    void shouldSaveGame() {
        UUID id = UUID.fromString("b601fd85-cacd-48c5-8a90-2a12d628a124");
        Game game = Game.builder().id(id).build();

        repository.save(game);

        assertThat(repository.find(id)).contains(game);
    }

    @Test
    void shouldReturnEmptyIfGameWithIdPresent() {
        UUID id = UUID.fromString("6ebca788-0239-49ed-893c-77078401dcf6");

        Optional<Game> game = repository.find(id);

        assertThat(game).isEmpty();
    }
}
