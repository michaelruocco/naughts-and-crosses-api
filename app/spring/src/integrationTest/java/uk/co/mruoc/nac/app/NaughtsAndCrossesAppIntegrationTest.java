package uk.co.mruoc.nac.app;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.GameJsonMother;
import uk.co.mruoc.nac.client.DefaultGameUpdateListener;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

@Slf4j
abstract class NaughtsAndCrossesAppIntegrationTest {

  public abstract NaughtsAndCrossesAppExtension getExtension();

  public NaughtsAndCrossesApiClient getAppClient() {
    return getExtension().getRestClient();
  }

  @Test
  public void shouldReturnNoGamesInitially() {
    NaughtsAndCrossesApiClient client = getAppClient();

    Collection<ApiGame> games = client.getAllGames();

    assertThat(games).isEmpty();
  }

  @Test
  public void shouldCreateGame() {
    NaughtsAndCrossesApiClient client = getAppClient();

    ApiGame game = client.createGame();

    assertThatJson(game).isEqualTo(GameJsonMother.initial());
  }

  @Test
  public void shouldSendWebsocketGameEventWhenGameCreated() {
    DefaultGameUpdateListener listener = new DefaultGameUpdateListener();
    getExtension().add(listener);
    NaughtsAndCrossesApiClient client = getAppClient();

    client.createGame();

    String expectedJson = GameJsonMother.initial();
    awaitMostRecentGameUpdateEquals(listener, expectedJson);
    assertThatJson(listener.forceGetMostRecentUpdate()).isEqualTo(expectedJson);
  }

  @Test
  public void shouldReturnGame() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame createdGame = client.createGame();

    ApiGame game = client.getGame(createdGame.getId());

    assertThatJson(game).isEqualTo(createdGame);
  }

  @Test
  public void shouldReturnMinimalGame() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame createdGame = client.createGame();

    ApiGame game = client.getMinimalGame(createdGame.getId());

    assertThatJson(game).whenIgnoringPaths("board", "players").isEqualTo(createdGame);
    assertThat(game.getBoard()).isNull();
    assertThat(game.getPlayers()).isNull();
  }

  @Test
  public void shouldSendWebsocketGameEventWhenTurnTaken() {
    DefaultGameUpdateListener listener = new DefaultGameUpdateListener();
    getExtension().add(listener);
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame game = client.createGame();

    client.takeTurn(game.getId(), new ApiTurn(0, 0, 'X'));

    String expectedJson = GameJsonMother.xTurn();
    awaitMostRecentGameUpdateEquals(listener, expectedJson);
    assertThatJson(listener.forceGetMostRecentUpdate()).isEqualTo(expectedJson);
  }

  @Test
  public void gameShouldCompleteWithXWinner() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame game = client.createGame();
    long id = game.getId();

    client.takeTurn(id, new ApiTurn(0, 0, 'X'));
    client.takeTurn(id, new ApiTurn(0, 1, 'O'));
    client.takeTurn(id, new ApiTurn(1, 0, 'X'));
    client.takeTurn(id, new ApiTurn(1, 1, 'O'));
    ApiGame updatedGame = client.takeTurn(id, new ApiTurn(2, 0, 'X'));

    assertThatJson(updatedGame).isEqualTo(GameJsonMother.xWinner());
  }

  @Test
  public void gameShouldCompleteWithDraw() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame game = client.createGame();
    long id = game.getId();

    client.takeTurn(id, new ApiTurn(0, 0, 'X'));
    client.takeTurn(id, new ApiTurn(1, 1, 'O'));
    client.takeTurn(id, new ApiTurn(0, 2, 'X'));
    client.takeTurn(id, new ApiTurn(1, 0, 'O'));
    client.takeTurn(id, new ApiTurn(1, 2, 'X'));
    client.takeTurn(id, new ApiTurn(0, 1, 'O'));
    client.takeTurn(id, new ApiTurn(2, 1, 'X'));
    client.takeTurn(id, new ApiTurn(2, 2, 'O'));
    ApiGame updatedGame = client.takeTurn(id, new ApiTurn(2, 0, 'X'));

    assertThatJson(updatedGame).isEqualTo(GameJsonMother.draw());
  }

  private void awaitMostRecentGameUpdateEquals(DefaultGameUpdateListener listener, String json) {
    await()
        .atMost(Duration.ofSeconds(5))
        .pollInterval(Duration.ofMillis(250))
        .until(() -> mostRecentUpdateEquals(listener, json));
  }

  private boolean mostRecentUpdateEquals(DefaultGameUpdateListener listener, String json) {
    try {
      assertThatJson(listener.forceGetMostRecentUpdate()).isEqualTo(json);
      return true;
    } catch (AssertionError error) {
      log.debug(error.getMessage(), error);
      return false;
    }
  }
}
