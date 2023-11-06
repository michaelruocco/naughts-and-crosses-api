package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Turn;

class GameServiceTest {

    private final GameFactory factory = mock(GameFactory.class);
    private final GameRepository repository = mock(GameRepository.class);
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
    void shouldPublishGameUpdateEventWhenGameCreated() {
        Game expectedGame = givenGameCreated();

        service.createGame();

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(eventPublisher).updated(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedGame);
    }

    @Test
    void shouldThrowExceptionIfGameNotFoundTakingTurn() {
        long id = 1;
        Turn turn = mock(Turn.class);

        Throwable error = catchThrowable(() -> service.takeTurn(id, turn));

        assertThat(error).isInstanceOf(GameNotFoundException.class).hasMessage("game with id 1 not found");
    }

    @Test
    void shouldReturnUpdatedGameAfterTakingTurn() {
        long id = 2;
        Turn turn = mock(Turn.class);
        Game game = givenGameFound(id);
        Game expectedGame = givenGameUpdated(game, turn);

        Game updatedGame = service.takeTurn(id, turn);

        assertThat(updatedGame).isEqualTo(expectedGame);
    }

    @Test
    void shouldSaveUpdatedGameAfterTakingTurn() {
        long id = 3;
        Turn turn = mock(Turn.class);
        Game game = givenGameFound(id);
        Game expectedGame = givenGameUpdated(game, turn);

        service.takeTurn(id, turn);

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedGame);
    }

    @Test
    void shouldPublishGameUpdateEventWhenTurnTaken() {
        long id = 4;
        Turn turn = mock(Turn.class);
        Game game = givenGameFound(id);
        Game expectedGame = givenGameUpdated(game, turn);

        service.takeTurn(id, turn);

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(eventPublisher).updated(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedGame);
    }

    @Test
    void shouldReturnAllGames() {
        Stream<Game> expectedGames = Stream.of(mock(Game.class), mock(Game.class));
        when(repository.getAll()).thenReturn(expectedGames);

        Stream<Game> games = service.getAll();

        assertThat(games).isEqualTo(expectedGames);
    }

    @Test
    void shouldDeleteAllGames() {
        service.deleteAll();

        verify(repository).deleteAll();
    }

    private Game givenGameCreated() {
        Game game = mock(Game.class);
        when(factory.buildGame()).thenReturn(game);
        return game;
    }

    private Game givenGameFound(long id) {
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
