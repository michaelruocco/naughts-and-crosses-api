package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.PlayerMother;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.Turn;

class GameServiceTest {

  private final AuthenticatedUserValidator userValidator = mock(AuthenticatedUserValidator.class);
  private final GameFactory factory = mock(GameFactory.class);
  private final GameRepository repository = mock(GameRepository.class);
  private final BoardFormatter formatter = mock(BoardFormatter.class);
  private final GameEventPublisher eventPublisher = mock(GameEventPublisher.class);
  private final Players players = PlayerMother.players();

  private final GameService service =
      GameService.builder()
          .userValidator(userValidator)
          .factory(factory)
          .repository(repository)
          .formatter(formatter)
          .eventPublisher(eventPublisher)
          .build();

  @Test
  void shouldReturnCreatedGame() {
    Game expectedGame = givenGameCreated();

    Game game = service.createGame(players);

    assertThat(game).isEqualTo(expectedGame);
  }

  @Test
  void shouldCreateGame() {
    Game expectedGame = givenGameCreated();

    service.createGame(players);

    ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
    verify(repository).create(captor.capture());
    assertThat(captor.getValue()).isEqualTo(expectedGame);
  }

  @Test
  void shouldPublishGameUpdateEventWhenGameCreated() {
    Game expectedGame = givenGameCreated();

    service.createGame(players);

    ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
    verify(eventPublisher).updated(captor.capture());
    assertThat(captor.getValue()).isEqualTo(expectedGame);
  }

  @Test
  void shouldThrowExceptionIfGameNotFoundTakingTurn() {
    long id = 1;
    Turn turn = mock(Turn.class);

    Throwable error = catchThrowable(() -> service.takeTurn(id, turn));

    assertThat(error)
        .isInstanceOf(GameNotFoundException.class)
        .hasMessage("game with id 1 not found");
  }

  @Test
  void shouldReturnUpdatedGameAfterTakingTurn() {
    long id = 2;
    Turn turn = mock(Turn.class);
    Game game = givenGameFound(id);
    Game expectedGame = givenTurnTaken(game, turn);

    Game updatedGame = service.takeTurn(id, turn);

    assertThat(updatedGame).isEqualTo(expectedGame);
  }

  @Test
  void shouldUpdatedGameAfterTakingTurn() {
    long id = 3;
    Turn turn = mock(Turn.class);
    Game game = givenGameFound(id);
    Game expectedGame = givenTurnTaken(game, turn);

    service.takeTurn(id, turn);

    ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
    verify(repository).update(captor.capture());
    assertThat(captor.getValue()).isEqualTo(expectedGame);
  }

  @Test
  void shouldPublishGameUpdateEventWhenTurnTaken() {
    long id = 4;
    Turn turn = mock(Turn.class);
    Game game = givenGameFound(id);
    Game expectedGame = givenTurnTaken(game, turn);

    service.takeTurn(id, turn);

    ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
    verify(eventPublisher).updated(captor.capture());
    assertThat(captor.getValue()).isEqualTo(expectedGame);
  }

  @Test
  void shouldReturnAllGames() {
    Stream<Game> expectedGames = givenGamesWithIds(5L, 6L);
    when(repository.getAll()).thenReturn(expectedGames);

    Stream<Game> games = service.getAll();

    assertThat(games).isEqualTo(expectedGames);
  }

  @Test
  void shouldDeleteGameById() {
    long id = 7;

    service.delete(id);

    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
    verify(repository).delete(captor.capture());
    assertThat(captor.getValue()).isEqualTo(id);
  }

  @Test
  void shouldPublishGameDeleteEventWhenGameDeletedById() {
    long id = 8;

    service.delete(id);

    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
    verify(eventPublisher).deleted(captor.capture());
    assertThat(captor.getValue()).isEqualTo(id);
  }

  @Test
  void shouldDeleteAllGames() {
    service.deleteAll();

    verify(repository).deleteAll();
  }

  @Test
  void shouldPublishGameDeleteEventsWhenAllGamesDeleted() {
    Stream<Game> games = givenGamesWithIds(9L, 10L);
    when(repository.getAll()).thenReturn(games);

    service.deleteAll();

    InOrder inOrder = inOrder(eventPublisher);
    inOrder.verify(eventPublisher).deleted(9);
    inOrder.verify(eventPublisher).deleted(10);
  }

  private Game givenGameCreated() {
    Game game = mock(Game.class);
    when(factory.buildGame(players)).thenReturn(game);
    return game;
  }

  private Game givenGameFound(long id) {
    Game game = mock(Game.class);
    when(repository.get(id)).thenReturn(Optional.of(game));
    return game;
  }

  private Game givenTurnTaken(Game game, Turn turn) {
    Game updated = mock(Game.class);
    Player player = mock(Player.class);
    when(game.take(turn)).thenReturn(updated);
    return updated;
  }

  private Stream<Game> givenGamesWithIds(Long... ids) {
    return Arrays.stream(ids).map(GameServiceTest::givenGameWithId);
  }

  private static Game givenGameWithId(long id) {
    Game game = mock(Game.class);
    when(game.getId()).thenReturn(id);
    return game;
  }
}
