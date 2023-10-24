package uk.co.mruoc.nac.app.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Turn;
import uk.co.mruoc.nac.app.repository.InMemoryGameRepository;

class GameServiceTest {

    private final GameFactory factory = mock(GameFactory.class);
    private final InMemoryGameRepository repository = mock(InMemoryGameRepository.class);
    private final BoardFormatter formatter = mock(BoardFormatter.class);
    private final GameEventPublisher eventPublisher = mock(GameEventPublisher.class);

    private final GameService service = GameService.builder()
            .factory(factory)
            .repository(repository)
            .formatter(formatter)
            .eventPublisher(eventPublisher)
            .build();

    @Test
    void shouldReturnCreatedGame() {
        Game expectedGame = givenGameCreated();

        Game game = service.createGame();

        assertThat(game).isEqualTo(expectedGame);
    }

    @Test
    void shouldSaveCreatedGame() {
        Game expectedGame = givenGameCreated();

        service.createGame();

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedGame);
    }

    @Test
    void shouldPublishCreatedGame() {
        Game expectedGame = givenGameCreated();

        service.createGame();

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(eventPublisher).created(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedGame);
    }

    @Test
    void shouldThrowExceptionIfGameNotFoundTakingTurn() {
        UUID id = UUID.fromString("ffe1e508-5679-46c1-92bb-43eaa11393f7");
        Turn turn = mock(Turn.class);

        Throwable error = catchThrowable(() -> service.takeTurn(id, turn));

        assertThat(error)
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("game with id ffe1e508-5679-46c1-92bb-43eaa11393f7 not found");
    }

    @Test
    void shouldReturnUpdatedGameAfterTakingTurn() {
        UUID id = UUID.fromString("ffe1e508-5679-46c1-92bb-43eaa11393f7");
        Turn turn = mock(Turn.class);
        Game game = givenGameFound(id);
        Game expectedGame = givenGameUpdated(game, turn);

        Game updatedGame = service.takeTurn(id, turn);

        assertThat(updatedGame).isEqualTo(expectedGame);
    }

    @Test
    void shouldSaveUpdatedGameAfterTakingTurn() {
        UUID id = UUID.fromString("ffe1e508-5679-46c1-92bb-43eaa11393f7");
        Turn turn = mock(Turn.class);
        Game game = givenGameFound(id);
        Game expectedGame = givenGameUpdated(game, turn);

        service.takeTurn(id, turn);

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedGame);
    }

    private Game givenGameCreated() {
        Game game = mock(Game.class);
        when(factory.buildGame()).thenReturn(game);
        return game;
    }

    private Game givenGameFound(UUID id) {
        Game game = mock(Game.class);
        when(repository.find(id)).thenReturn(Optional.of(game));
        return game;
    }

    private Game givenGameUpdated(Game game, Turn turn) {
        Game updated = mock(Game.class);
        when(game.take(turn)).thenReturn(updated);
        return updated;
    }
}
