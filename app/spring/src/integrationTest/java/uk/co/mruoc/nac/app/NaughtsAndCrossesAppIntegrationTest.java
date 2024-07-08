package uk.co.mruoc.nac.app;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.await;
import static uk.co.mruoc.nac.api.dto.ApiPlayerMother.buildMinimalCrossesPlayer;
import static uk.co.mruoc.nac.api.dto.ApiPlayerMother.buildMinimalNaughtsPlayer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequestMother;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiGameJsonMother;
import uk.co.mruoc.nac.api.dto.ApiTokenJsonMother;
import uk.co.mruoc.nac.api.dto.ApiTokenResponse;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequestMother;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.dto.ApiUserBatch;
import uk.co.mruoc.nac.api.dto.ApiUserJsonMother;
import uk.co.mruoc.nac.client.GameEventSubscriber;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClientException;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiTokenClient;
import uk.co.mruoc.nac.environment.integrated.cognito.AdminCognitoTokenCredentials;

@Slf4j
abstract class NaughtsAndCrossesAppIntegrationTest {

  private static final char X = 'X';
  private static final char O = 'O';
  private static final String JBLOGGS = "jbloggs";
  private static final String JDOE = "jdoe";

  public ApiCreateGameRequest buildCreateGameRequest() {
    return getFixtures().buildCreateGameRequest();
  }

  public ApiGame givenGameExists() {
    return getFixtures().givenGameExists();
  }

  public ApiUser givenUserExists() {
    return getFixtures().givenUserExists();
  }

  public void givenUserExists(ApiCreateUserRequest request) {
    getFixtures().givenUserExists(request);
  }

  public Fixtures getFixtures() {
    return new Fixtures(getAdminAppClient());
  }

  public NaughtsAndCrossesApiTokenClient getTokenClient() {
    return getExtension().buildTokenClient();
  }

  public NaughtsAndCrossesApiClient getAdminAppClient() {
    return getExtension().buildAdminRestClient();
  }

  public NaughtsAndCrossesApiClient getUser1AppClient() {
    return getExtension().buildUser1RestClient();
  }

  public NaughtsAndCrossesApiClient getUser2AppClient() {
    return getExtension().buildUser2RestClient();
  }

  public abstract NaughtsAndCrossesAppExtension getExtension();

  @Test
  public void shouldRefreshAccessToken() {
    NaughtsAndCrossesApiTokenClient client = getTokenClient();
    ApiTokenResponse createResponse = client.createToken(new AdminCognitoTokenCredentials());

    ApiTokenResponse refreshResponse = client.refreshToken(createResponse.getRefreshToken());

    assertThatJson(refreshResponse).isEqualTo(ApiTokenJsonMother.refreshResponse());
    assertThat(refreshResponse.getAccessToken()).isNotEqualTo(createResponse.getAccessToken());
  }

  @Test
  public void shouldReturnAllUserGroups() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<String> groups = client.getUserGroups();

    assertThat(groups).containsExactlyInAnyOrder("player", "admin");
  }

  @Test
  public void shouldReturnAllUsers() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<ApiUser> users = client.getAllUsers();

    assertThat(users).hasSize(3);
  }

  @Test
  public void shouldReturnCandidatePlayerUsernames() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<String> usernames = client.getAllCandidatePlayerUsernames();

    assertThat(usernames).contains("admin", "user-1", "user-2");
  }

  @Test
  public void shouldGetUser() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    ApiUser user = client.getUser("user-1");

    assertThatJson(user).isEqualTo(ApiUserJsonMother.user1());
  }

  @Test
  public void shouldCreateUser() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiCreateUserRequest request = ApiCreateUserRequestMother.joeBloggs();

    try {
      ApiUser user = client.createUser(request);

      assertThatJson(user).isEqualTo(ApiUserJsonMother.joeBloggs());
    } finally {
      client.deleteUser(request.getUsername());
    }
  }

  @Test
  public void shouldReturnErrorIfAttemptToCreateUserThatAlreadyExists() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiCreateUserRequest request = ApiCreateUserRequestMother.joeBloggs();
    givenUserExists(request);

    try {
      Throwable error = catchThrowable(() -> client.createUser(request));

      assertThat(error)
          .isInstanceOf(NaughtsAndCrossesApiClientException.class)
          .hasMessage("409 : \"user jbloggs already exists\"");
    } finally {
      client.deleteUser(request.getUsername());
    }
  }

  @Test
  public void shouldUpdateUser() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUser originalUser = givenUserExists();

    try {
      ApiUpdateUserRequest request = ApiUpdateUserRequestMother.updatedUser();
      ApiUser user = client.updateUser(originalUser.getUsername(), request);

      assertThatJson(user).isEqualTo(ApiUserJsonMother.testUserUpdated());
    } finally {
      client.deleteUser(originalUser.getUsername());
    }
  }

  @Test
  public void shouldReturnErrorIfAttemptToUpdateUserThatDoesNotExist() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUpdateUserRequest request = ApiUpdateUserRequestMother.updatedUser();

    Throwable error = catchThrowable(() -> client.updateUser("non-existent-user", request));

    assertThat(error)
        .isInstanceOf(NaughtsAndCrossesApiClientException.class)
        .hasMessage("404 : \"user non-existent-user not found\"");
  }

  @Test
  public void shouldUploadBatchOfUsers() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    Resource resource = loadUsers1Csv();

    try {
      ApiUserBatch batch = client.uploadUserBatch(resource);

      awaitBatchUploadCompleteWithoutErrors(batch.getId());

      assertThatJson(client.getUser(JBLOGGS)).isEqualTo(ApiUserJsonMother.joeBloggs());
      assertThatJson(client.getUser(JDOE)).isEqualTo(ApiUserJsonMother.janeDoe());
    } finally {
      cleanUpUsersAndBatches();
    }
  }

  @Test
  public void userBatchShouldUpdateUsersIfUsersAlreadyExists() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    try {
      ApiUserBatch batch = client.uploadUserBatch(loadUsers1Csv());
      awaitBatchUploadCompleteWithoutErrors(batch.getId());

      ApiUserBatch updatedBatch = client.uploadUserBatch(loadUsers1UpdatedCsv());
      awaitBatchUploadCompleteWithoutErrors(updatedBatch.getId());

      assertThatJson(client.getUser(JBLOGGS)).isEqualTo(ApiUserJsonMother.joeBloggsUpdated());
      assertThatJson(client.getUser(JDOE)).isEqualTo(ApiUserJsonMother.janeDoeUpdated());
    } finally {
      cleanUpUsersAndBatches();
    }
  }

  @Test
  public void shouldReturnCreatedBatchOfUsers() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    Resource resource = loadUsers1Csv();

    try {
      ApiUserBatch createdBatch = client.uploadUserBatch(resource);
      awaitBatchUploadCompleteWithoutErrors(createdBatch.getId());

      ApiUserBatch returnedBatch = client.getUserBatch(createdBatch.getId());

      assertThat(returnedBatch)
          .usingRecursiveComparison()
          .ignoringFields("createdAt", "updatedAt", "users", "errors", "complete")
          .isEqualTo(createdBatch);
    } finally {
      cleanUpUsersAndBatches();
    }
  }

  @Test
  public void shouldReturnAllCreatedBatchesOfUsersOrderedByCreatedDate() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    try {
      ApiUserBatch batch1 = client.uploadUserBatch(loadUsers1Csv());
      awaitBatchUploadCompleteWithoutErrors(batch1.getId());

      ApiUserBatch batch2 = client.uploadUserBatch(loadUsers2Csv());
      awaitBatchUploadCompleteWithoutErrors(batch2.getId());

      Collection<ApiUserBatch> batches = client.getAllUserBatches();

      assertThat(batches)
          .map(ApiUserBatch::getCreatedAt)
          .containsExactly(batch1.getCreatedAt(), batch2.getCreatedAt());
    } finally {
      cleanUpUsersAndBatches(JBLOGGS, JDOE, "batch-test-1", "batch-test-2");
    }
  }

  @Test
  public void shouldReturnNoGamesInitially() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<ApiGame> games = client.getAllGames();

    assertThat(games).isEmpty();
  }

  @Test
  public void shouldCreateGame() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
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
      NaughtsAndCrossesApiClient client = getAdminAppClient();
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
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiGame createdGame = givenGameExists();

    ApiGame game = client.getGame(createdGame.getId());

    assertThatJson(game).isEqualTo(createdGame);
  }

  @Test
  public void shouldReturnMinimalGame() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiGame createdGame = givenGameExists();

    ApiGame game = client.getMinimalGame(createdGame.getId());

    assertThatJson(game).whenIgnoringPaths("board", "players").isEqualTo(createdGame);
    assertThatJson(game.getPlayers())
        .isEqualTo(List.of(buildMinimalCrossesPlayer(), buildMinimalNaughtsPlayer()));
    assertThat(game.getBoard()).isNull();
  }

  @Test
  public void shouldSendWebsocketGameEventWhenTurnTaken() {
    NaughtsAndCrossesAppExtension extension = getExtension();
    extension.connectToWebsocket();
    try {
      GameEventSubscriber<ApiGame> subscriber = extension.subscribeToGameUpdateEvents();
      NaughtsAndCrossesApiClient client = getUser1AppClient();
      ApiGame game = givenGameExists();

      client.takeTurn(game.getId(), new ApiTurn(0, 0, X));

      String expectedJson = ApiGameJsonMother.xTurn();
      awaitMostRecentGameUpdateEquals(subscriber, expectedJson);
      assertThatJson(subscriber.forceGetMostRecent()).isEqualTo(expectedJson);
    } finally {
      extension.disconnectWebsocket();
    }
  }

  @Test
  public void gameShouldCompleteWithXWinner() {
    NaughtsAndCrossesApiClient user1Client = getUser1AppClient();
    NaughtsAndCrossesApiClient user2Client = getUser2AppClient();
    ApiGame game = givenGameExists();
    long id = game.getId();

    user1Client.takeTurn(id, new ApiTurn(0, 0, X));
    user2Client.takeTurn(id, new ApiTurn(0, 1, O));
    user1Client.takeTurn(id, new ApiTurn(1, 0, X));
    user2Client.takeTurn(id, new ApiTurn(1, 1, O));
    ApiGame updatedGame = user1Client.takeTurn(id, new ApiTurn(2, 0, X));

    assertThatJson(updatedGame).isEqualTo(ApiGameJsonMother.xWinner());
  }

  @Test
  public void gameShouldCompleteWithDraw() {
    NaughtsAndCrossesApiClient user1Client = getUser1AppClient();
    NaughtsAndCrossesApiClient user2Client = getUser2AppClient();
    ApiGame game = givenGameExists();
    long id = game.getId();

    user1Client.takeTurn(id, new ApiTurn(0, 0, X));
    user2Client.takeTurn(id, new ApiTurn(1, 1, O));
    user1Client.takeTurn(id, new ApiTurn(0, 2, X));
    user2Client.takeTurn(id, new ApiTurn(1, 0, O));
    user1Client.takeTurn(id, new ApiTurn(1, 2, X));
    user2Client.takeTurn(id, new ApiTurn(0, 1, O));
    user1Client.takeTurn(id, new ApiTurn(2, 1, X));
    user2Client.takeTurn(id, new ApiTurn(2, 2, O));
    ApiGame updatedGame = user1Client.takeTurn(id, new ApiTurn(2, 0, X));

    assertThatJson(updatedGame).isEqualTo(ApiGameJsonMother.draw());
  }

  @Test
  public void shouldDeleteGame() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
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
      NaughtsAndCrossesApiClient client = getAdminAppClient();
      ApiGame game = givenGameExists();
      long id = game.getId();

      client.deleteGame(id);

      awaitMostRecentGameDeleteIdEquals(listener, id);
      assertThat(listener.forceGetMostRecent()).isEqualTo(id);
    } finally {
      extension.disconnectWebsocket();
    }
  }

  private Resource loadUsers1Csv() {
    return loadUsersCsv("users-1.csv");
  }

  private Resource loadUsers1UpdatedCsv() {
    return loadUsersCsv("users-1-updated.csv");
  }

  private Resource loadUsers2Csv() {
    return loadUsersCsv("users-2.csv");
  }

  private Resource loadUsersCsv(String filename) {
    return new ClassPathResource(String.format("user/%s", filename));
  }

  private void cleanUpUsersAndBatches() {
    cleanUpUsersAndBatches(JBLOGGS, JDOE);
  }

  private void cleanUpUsersAndBatches(String... usernames) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    Arrays.stream(usernames).forEach(client::deleteUser);
    client.deleteAllUserBatches();
  }

  private void awaitBatchUploadCompleteWithoutErrors(String id) {
    waitUntilBatchUpload(id, ApiUserBatch::isCompleteWithoutErrors);
  }

  private void waitUntilBatchUpload(String id, Predicate<ApiUserBatch> predicate) {
    await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(250))
        .until(() -> batchUploadComplete(id, predicate));
  }

  private boolean batchUploadComplete(String id, Predicate<ApiUserBatch> predicate) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUserBatch batch = client.getUserBatch(id);
    return predicate.test(batch);
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
