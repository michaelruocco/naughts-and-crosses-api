package uk.co.mruoc.nac.app;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.Collection;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiGameJsonMother;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.client.DefaultGameUpdateListener;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

@Slf4j
abstract class NaughtsAndCrossesAppIntegrationTest {

  public ApiCreateGameRequest buildCreateGameRequest() {
    return getFixtures().buildCreateGameRequest();
  }

  public ApiGame givenGameExists() {
    return getFixtures().givenGameExists();
  }

  public Fixtures getFixtures() {
    return new Fixtures(getAppClient());
  }

  public NaughtsAndCrossesApiClient getAppClient() {
    return getExtension().getRestClient();
  }

  public abstract NaughtsAndCrossesAppExtension getExtension();

  @Test
  public void shouldReturnNoGamesInitially() {
    NaughtsAndCrossesApiClient client = getAppClient();

    Collection<ApiGame> games = client.getAllGames();

    assertThat(games).isEmpty();
  }

  @Test
  public void shouldCreateGame() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiCreateGameRequest request = buildCreateGameRequest();

    ApiGame game = client.createGame(request);

    assertThatJson(game).isEqualTo(ApiGameJsonMother.initial());
  }

  @Test
  public void shouldSendWebsocketGameEventWhenGameCreated() {
    NaughtsAndCrossesAppExtension extension = getExtension();
    DefaultGameUpdateListener listener = extension.connectAndListenToWebsocket();
    try {
      NaughtsAndCrossesApiClient client = getAppClient();
      ApiCreateGameRequest request = buildCreateGameRequest();

      client.createGame(request);

      String expectedJson = ApiGameJsonMother.initial();
      awaitMostRecentGameUpdateEquals(listener, expectedJson);
      assertThatJson(listener.forceGetMostRecentUpdate()).isEqualTo(expectedJson);
    } finally {
      extension.disconnectWebsocket();
    }
  }

  @Test
  public void shouldReturnGame() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame createdGame = givenGameExists();

    ApiGame game = client.getGame(createdGame.getId());

    assertThatJson(game).isEqualTo(createdGame);
  }

  @Test
  public void shouldReturnMinimalGame() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame createdGame = givenGameExists();

    ApiGame game = client.getMinimalGame(createdGame.getId());

    assertThatJson(game).whenIgnoringPaths("board", "players").isEqualTo(createdGame);
    assertThat(game.getBoard()).isNull();
    assertThat(game.getPlayers()).isNull();
  }

  @Test
  public void shouldSendWebsocketGameEventWhenTurnTaken() {
    NaughtsAndCrossesAppExtension extension = getExtension();
    DefaultGameUpdateListener listener = extension.connectAndListenToWebsocket();
    try {
      NaughtsAndCrossesApiClient client = getAppClient();
      ApiGame game = givenGameExists();

      client.takeTurn(game.getId(), new ApiTurn(0, 0, 'X'));

      String expectedJson = ApiGameJsonMother.xTurn();
      awaitMostRecentGameUpdateEquals(listener, expectedJson);
      assertThatJson(listener.forceGetMostRecentUpdate()).isEqualTo(expectedJson);
    } finally {
      extension.disconnectWebsocket();
    }
  }

  @Test
  public void gameShouldCompleteWithXWinner() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame game = givenGameExists();
    long id = game.getId();

    client.takeTurn(id, new ApiTurn(0, 0, 'X'));
    client.takeTurn(id, new ApiTurn(0, 1, 'O'));
    client.takeTurn(id, new ApiTurn(1, 0, 'X'));
    client.takeTurn(id, new ApiTurn(1, 1, 'O'));
    ApiGame updatedGame = client.takeTurn(id, new ApiTurn(2, 0, 'X'));

    assertThatJson(updatedGame).isEqualTo(ApiGameJsonMother.xWinner());
  }

  @Test
  public void gameShouldCompleteWithDraw() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame game = givenGameExists();
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

    assertThatJson(updatedGame).isEqualTo(ApiGameJsonMother.draw());
  }

  @Test
  public void shouldDeleteGame() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiGame createdGame = givenGameExists();

    client.deleteGame(createdGame.getId());

    Throwable error = catchThrowable(() -> client.getGame(createdGame.getId()));
    assertThat(error).isInstanceOf(HttpServerErrorException.InternalServerError.class);
  }

  private void awaitMostRecentGameUpdateEquals(DefaultGameUpdateListener listener, String json) {
    await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(250))
        .ignoreExceptionsInstanceOf(NoSuchElementException.class)
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
