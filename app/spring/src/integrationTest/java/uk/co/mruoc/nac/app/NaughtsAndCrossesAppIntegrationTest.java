package uk.co.mruoc.nac.app;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.await;
import static uk.co.mruoc.nac.api.dto.ApiPlayerMother.buildMinimalCrossesPlayer;
import static uk.co.mruoc.nac.api.dto.ApiPlayerMother.buildMinimalNaughtsPlayer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mruoc.nac.api.dto.ApiAuthCodeRequest;
import uk.co.mruoc.nac.api.dto.ApiAuthCodeRequestMother;
import uk.co.mruoc.nac.api.dto.ApiCandidateGamePlayer;
import uk.co.mruoc.nac.api.dto.ApiCandidateGamePlayerMother;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequestMother;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiGameJsonMother;
import uk.co.mruoc.nac.api.dto.ApiGamePage;
import uk.co.mruoc.nac.api.dto.ApiGamePageRequest;
import uk.co.mruoc.nac.api.dto.ApiSortOrder;
import uk.co.mruoc.nac.api.dto.ApiTokenJsonMother;
import uk.co.mruoc.nac.api.dto.ApiTokenResponse;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequestMother;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.dto.ApiUserBatch;
import uk.co.mruoc.nac.api.dto.ApiUserJsonMother;
import uk.co.mruoc.nac.api.dto.ApiUserPage;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequestMother;
import uk.co.mruoc.nac.api.factory.ApiCreateGameRequestFactory;
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

  @Test
  public void shouldRefreshAccessToken() {
    NaughtsAndCrossesApiTokenClient client = getTokenClient();
    ApiTokenResponse createResponse = client.createToken(new AdminCognitoTokenCredentials());

    ApiTokenResponse refreshResponse = client.refreshToken(createResponse.getRefreshToken());

    assertThatJson(refreshResponse).isEqualTo(ApiTokenJsonMother.refreshResponse());
    assertThat(refreshResponse.getAccessToken()).isNotEqualTo(createResponse.getAccessToken());
  }

  @Test
  public void shouldReturnTokensForAuthCode() {
    NaughtsAndCrossesApiTokenClient client = getTokenClient();
    ApiAuthCodeRequest request = ApiAuthCodeRequestMother.build();

    ApiTokenResponse response = client.createToken(request);

    assertThatJson(response).isEqualTo(ApiTokenJsonMother.authCodeResponse());
  }

  @Test
  public void shouldReturnAllUserGroups() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<String> groups = client.getUserGroups();

    assertThat(groups).containsExactly("admin", "player");
  }

  @Test
  public void shouldReturnAllUsers() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<ApiUser> users = client.getAllUsers();

    assertThat(users).hasSize(3);
  }

  @ParameterizedTest
  @MethodSource("offsetsAndUsernames")
  public void shouldReturnUsersPaginated(int offset, String username) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUserPageRequest request = ApiUserPageRequestMother.builder().offset(offset).limit(1).build();

    ApiUserPage page = client.getUserPage(request);

    assertThat(page.getTotal()).isEqualTo(3);
    assertThat(page.getUsers()).map(ApiUser::getUsername).containsExactly(username);
  }

  @ParameterizedTest
  @MethodSource("sortDirectionsAndUsernames")
  public void shouldReturnPaginatedUsersSorted(String direction, Collection<String> usernames) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUserPageRequest request =
        ApiUserPageRequestMother.withSorts(new ApiSortOrder("username", direction));

    ApiUserPage page = client.getUserPage(request);

    assertThat(page.getUsers()).map(ApiUser::getUsername).containsExactlyElementsOf(usernames);
  }

  @Test
  public void shouldFilterPaginatedUsersByGroup() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUserPageRequest request = ApiUserPageRequestMother.withGroups("player");

    ApiUserPage page = client.getUserPage(request);

    assertThat(page.getUsers()).map(ApiUser::getUsername).containsExactly("user-1", "user-2");
  }

  @ParameterizedTest
  @ValueSource(strings = {"ad", "min@em", "Admin"})
  public void shouldFilterPaginatedUsersSearchTerm(String searchTerm) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUserPageRequest request = ApiUserPageRequestMother.withSearchTerm(searchTerm);

    ApiUserPage page = client.getUserPage(request);

    assertThat(page.getUsers()).map(ApiUser::getUsername).containsExactly("admin");
  }

  @Test
  public void shouldReturnCandidatePlayers() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    Collection<ApiCandidateGamePlayer> players = client.getAllCandidatePlayers();

    assertThat(players)
        .containsExactly(
            ApiCandidateGamePlayerMother.admin(),
            ApiCandidateGamePlayerMother.user1(),
            ApiCandidateGamePlayerMother.user2());
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
          .hasMessageContaining("user jbloggs already exists");
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
  public void shouldSynchronizeSpecificExternalUser() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();

    ThrowableAssert.ThrowingCallable call = () -> client.synchronizeExternalUser("admin");

    assertThatCode(call).doesNotThrowAnyException();
  }

  @Test
  public void shouldReturnErrorIfAttemptToUpdateUserThatDoesNotExist() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiUpdateUserRequest request = ApiUpdateUserRequestMother.updatedUser();

    Throwable error = catchThrowable(() -> client.updateUser("non-existent-user", request));

    assertThat(error)
        .isInstanceOf(NaughtsAndCrossesApiClientException.class)
        .hasMessageContaining("user non-existent-user not found");
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
  public void shouldReturnErrorIfBatchFileIsMalicious() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    Resource resource = loadMaliciousUsersCsv();

    Throwable error = catchThrowable(() -> client.uploadUserBatch(resource));

    assertThat(error)
        .isInstanceOf(NaughtsAndCrossesApiClientException.class)
        .hasMessageContaining("failed with signature Win.Test.EICAR_HDB-1");
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
  public void shouldReturnAllGamesWithMostRecentlyCreatedFirst() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    ApiGame game1 = givenGameExists();
    ApiGame game2 = givenGameExists();

    Collection<ApiGame> games = client.getAllGames();

    assertThat(games).containsExactly(game2, game1);
  }

  @Test
  public void shouldReturnPagedGamesWithMostRecentlyCreatedFirst() {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    givenGameExists();
    ApiGame game = givenGameExists();
    ApiGamePageRequest request = ApiGamePageRequest.builder().limit(1).offset(0).build();

    ApiGamePage page = client.getGamePage(request);

    assertThat(page.getTotal()).isEqualTo(2);
    assertThat(page.getGames()).map(ApiGame::getId).containsExactly(game.getId());
  }

  @ParameterizedTest
  @MethodSource("completeFilterAndExpectedGameIds")
  public void shouldReturnPagedGamesFilteredByComplete(
      Boolean complete, Collection<Long> expectedGameIds) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    givenGameExists();
    givenCompletedGameExists();
    ApiGamePageRequest request =
        ApiGamePageRequest.builder().limit(2).offset(0).complete(complete).build();

    ApiGamePage page = client.getGamePage(request);

    assertThat(page.getTotal()).isEqualTo(expectedGameIds.size());
    assertThat(page.getGames()).map(ApiGame::getId).containsExactlyElementsOf(expectedGameIds);
  }

  @ParameterizedTest
  @MethodSource("usernameFilterAndExpectedGameIds")
  public void shouldReturnPagedGamesFilteredByUsername(
      String username, Collection<Long> expectedGameIds) {
    NaughtsAndCrossesApiClient client = getAdminAppClient();
    givenGameExists();
    givenGameExists();
    ApiGamePageRequest request =
        ApiGamePageRequest.builder().limit(2).offset(0).username(username).build();

    ApiGamePage page = client.getGamePage(request);

    assertThat(page.getTotal()).isEqualTo(expectedGameIds.size());
    assertThat(page.getGames()).map(ApiGame::getId).containsExactlyElementsOf(expectedGameIds);
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

  private Resource loadMaliciousUsersCsv() {
    return loadUsersCsv("users-malicious.csv");
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

  public ApiCreateGameRequest buildCreateGameRequest() {
    return getFixtures().buildCreateGameRequest();
  }

  public ApiGame givenGameExists() {
    return getFixtures().givenGameExists();
  }

  public ApiGame givenCompletedGameExists() {
    return getFixtures().givenCompletedGameExists();
  }

  public ApiUser givenUserExists() {
    return getFixtures().givenUserExists();
  }

  public void givenUserExists(ApiCreateUserRequest request) {
    getFixtures().givenUserExists(request);
  }

  public Fixtures getFixtures() {
    return Fixtures.builder()
        .adminClient(getAdminAppClient())
        .user1Client(getUser1AppClient())
        .user2Client(getUser2AppClient())
        .requestFactory(new ApiCreateGameRequestFactory())
        .build();
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

  private static Stream<Arguments> offsetsAndUsernames() {
    return Stream.of(
        Arguments.of(0, "admin"), Arguments.of(1, "user-1"), Arguments.of(2, "user-2"));
  }

  private static Stream<Arguments> sortDirectionsAndUsernames() {
    return Stream.of(
        Arguments.of("ASC", List.of("admin", "user-1", "user-2")),
        Arguments.of("DESC", List.of("user-2", "user-1", "admin")));
  }

  private static Stream<Arguments> completeFilterAndExpectedGameIds() {
    return Stream.of(
        Arguments.of(true, List.of(2L)),
        Arguments.of(false, List.of(1L)),
        Arguments.of(null, List.of(2L, 1L)));
  }

  private static Stream<Arguments> usernameFilterAndExpectedGameIds() {
    return Stream.of(
        Arguments.of("admin", Collections.emptyList()),
        Arguments.of("user-1", List.of(2L, 1L)),
        Arguments.of(null, List.of(2L, 1L)));
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
