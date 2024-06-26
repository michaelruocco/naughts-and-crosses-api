package uk.co.mruoc.nac.app;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.await;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequestMother;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiGameJsonMother;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.dto.ApiUserBatch;
import uk.co.mruoc.nac.api.dto.ApiUserJsonMother;
import uk.co.mruoc.nac.client.GameEventSubscriber;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

@Slf4j
abstract class NaughtsAndCrossesAppIntegrationTest {

  public ApiCreateGameRequest buildCreateGameRequest() {
    return getFixtures().buildCreateGameRequest();
  }

  public ApiGame givenGameExists() {
    return getFixtures().givenGameExists();
  }

  public ApiUser givenUserExists() {
    return getFixtures().givenUserExists();
  }

  public Fixtures getFixtures() {
    return new Fixtures(getAppClient());
  }

  public NaughtsAndCrossesApiClient getAppClient() {
    return getExtension().getRestClient();
  }

  public abstract NaughtsAndCrossesAppExtension getExtension();

  @Test
  public void shouldReturnAllUsers() {
    NaughtsAndCrossesApiClient client = getAppClient();

    Collection<ApiUser> users = client.getAllUsers();

    assertThat(users).hasSize(2);
  }

  @Test
  public void shouldGetUser() {
    NaughtsAndCrossesApiClient client = getAppClient();

    ApiUser user = client.getUser("user-1");

    assertThatJson(user).isEqualTo(ApiUserJsonMother.user1());
  }

  @Test
  public void shouldCreateUser() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiCreateUserRequest request = ApiCreateUserRequestMother.joeBloggs();

    try {
      ApiUser user = client.createUser(request);

      assertThatJson(user).isEqualTo(ApiUserJsonMother.joeBloggs());
    } finally {
      client.deleteUser(request.getUsername());
    }
  }

  @Test
  public void shouldUpdateUser() {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiUser originalUser = givenUserExists();

    try {
      ApiUpdateUserRequest request =
          ApiUpdateUserRequest.builder()
              .firstName("updated-first")
              .lastName("updated-last")
              .email("updated@email.com")
              .emailVerified(false)
              .groups(Set.of("player"))
              .build();
      ApiUser user = client.updateUser(originalUser.getUsername(), request);

      assertThatJson(user).isEqualTo(ApiUserJsonMother.testUserUpdated());
    } finally {
      client.deleteUser(originalUser.getUsername());
    }
  }

  @Test
  public void shouldUploadBatchOfUsers() {
    NaughtsAndCrossesApiClient client = getAppClient();
    Resource resource = loadUsersCsv();

    try {
      ApiUserBatch batch = client.uploadUserBatch(resource);

      await()
          .atMost(Duration.ofSeconds(10))
          .pollInterval(Duration.ofMillis(250))
          .until(() -> batchUploadCompleteWithoutErrors(batch.getId()));

      assertThatJson(client.getUser("jbloggs")).isEqualTo(ApiUserJsonMother.joeBloggs());
      assertThatJson(client.getUser("jdoe")).isEqualTo(ApiUserJsonMother.janeDoe());
    } finally {
      client.deleteUser("jbloggs");
      client.deleteUser("jdoe");
      client.deleteAllUserBatches();
    }
  }

  @Test
  public void shouldReturnCreatedBatchOfUsers() {
    NaughtsAndCrossesApiClient client = getAppClient();
    Resource resource = loadUsersCsv();

    try {
      ApiUserBatch createdBatch = client.uploadUserBatch(resource);

      ApiUserBatch returnedBatch = client.getUserBatch(createdBatch.getId());

      assertThat(returnedBatch)
          .usingRecursiveComparison()
          .ignoringFields("createdAt", "updatedAt", "users", "errors", "complete")
          .isEqualTo(createdBatch);
    } finally {
      client.deleteUser("jbloggs");
      client.deleteUser("jdoe");
      client.deleteAllUserBatches();
    }
  }

  @Test
  public void shouldReturnAllCreatedBatchesOfUsersOrderedByCreatedDate() {
    NaughtsAndCrossesApiClient client = getAppClient();
    Resource resource = loadUsersCsv();

    try {
      ApiUserBatch batch1 = client.uploadUserBatch(resource);
      ApiUserBatch batch2 = client.uploadUserBatch(resource);

      Collection<ApiUserBatch> batches = client.getAllUserBatches();

      assertThat(batches)
          .map(ApiUserBatch::getCreatedAt)
          .containsExactly(batch1.getCreatedAt(), batch2.getCreatedAt());
    } finally {
      client.deleteUser("jbloggs");
      client.deleteUser("jdoe");
      client.deleteAllUserBatches();
    }
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
    ApiCreateGameRequest request = buildCreateGameRequest();

    ApiGame game = client.createGame(request);

    assertThatJson(game).isEqualTo(ApiGameJsonMother.initial());
  }

  @Test
  public void shouldSendWebsocketGameEventWhenGameCreated() {
    NaughtsAndCrossesAppExtension extension = getExtension();
    extension.connectToWebsocket();
    try {
      GameEventSubscriber<ApiGame> subscriber = extension.subscribeToGameUpdateEvents();
      NaughtsAndCrossesApiClient client = getAppClient();
      ApiCreateGameRequest request = buildCreateGameRequest();

      client.createGame(request);

      String expectedJson = ApiGameJsonMother.initial();
      awaitMostRecentGameUpdateEquals(subscriber, expectedJson);
      assertThatJson(subscriber.forceGetMostRecent()).isEqualTo(expectedJson);
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
    extension.connectToWebsocket();
    try {
      GameEventSubscriber<ApiGame> subscriber = extension.subscribeToGameUpdateEvents();
      NaughtsAndCrossesApiClient client = getAppClient();
      ApiGame game = givenGameExists();

      client.takeTurn(game.getId(), new ApiTurn(0, 0, 'X'));

      String expectedJson = ApiGameJsonMother.xTurn();
      awaitMostRecentGameUpdateEquals(subscriber, expectedJson);
      assertThatJson(subscriber.forceGetMostRecent()).isEqualTo(expectedJson);
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

  @Test
  public void shouldSendWebsocketEventWhenGameDeleted() {
    NaughtsAndCrossesAppExtension extension = getExtension();
    extension.connectToWebsocket();
    try {
      GameEventSubscriber<Long> listener = extension.subscribeToGameDeleteEvents();
      NaughtsAndCrossesApiClient client = getAppClient();
      ApiGame game = givenGameExists();
      long id = game.getId();

      client.deleteGame(id);

      awaitMostRecentGameDeleteIdEquals(listener, id);
      assertThat(listener.forceGetMostRecent()).isEqualTo(id);
    } finally {
      extension.disconnectWebsocket();
    }
  }

  private Resource loadUsersCsv() {
    Path path = Paths.get("src/testFixtures/resources/users/users.csv");
    return new FileSystemResource(path.toFile());
  }

  private boolean batchUploadCompleteWithoutErrors(String id) {
    NaughtsAndCrossesApiClient client = getAppClient();
    ApiUserBatch batch = client.getUserBatch(id);
    log.info("got api user batch {}", batch);
    return batch.isComplete() && !batch.hasErrors();
  }

  private static void awaitMostRecentGameUpdateEquals(
      GameEventSubscriber<ApiGame> listener, String json) {
    await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(250))
        .until(() -> mostRecentUpdateEquals(listener, json));
  }

  private static boolean mostRecentUpdateEquals(
      GameEventSubscriber<ApiGame> listener, String json) {
    return listener.getMostRecent().map(update -> gameEqualsJson(update, json)).orElse(false);
  }

  private static boolean gameEqualsJson(ApiGame game, String json) {
    try {
      assertThatJson(game).isEqualTo(json);
      return true;
    } catch (AssertionError error) {
      log.debug(error.getMessage(), error);
      return false;
    }
  }

  private static void awaitMostRecentGameDeleteIdEquals(
      GameEventSubscriber<Long> listener, long id) {
    await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(250))
        .ignoreExceptionsInstanceOf(NoSuchElementException.class)
        .until(() -> mostRecentDeleteIdEquals(listener, id));
  }

  private static boolean mostRecentDeleteIdEquals(GameEventSubscriber<Long> listener, long id) {
    return listener.getMostRecent().map(recentId -> recentId == id).orElse(false);
  }
}
