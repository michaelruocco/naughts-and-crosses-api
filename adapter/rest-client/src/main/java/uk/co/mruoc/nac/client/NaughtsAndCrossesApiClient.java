package uk.co.mruoc.nac.client;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.api.dto.ApiCandidateGamePlayer;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.dto.ApiUserBatch;
import uk.co.mruoc.nac.api.dto.ApiUserPage;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesApiClient {

  private final UriFactory uriFactory;
  private final HttpEntityFactory entityFactory;
  private final RestTemplate template;

  public NaughtsAndCrossesApiClient(String baseUrl, String token) {
    this(new UriFactory(baseUrl), new HttpEntityFactory(token), new RestTemplate());
  }

  public Collection<String> getUserGroups() {
    String uri = uriFactory.buildUserGroupsUri();
    String[] groups = performGet(uri, String[].class);
    return toCollection(groups);
  }

  public ApiUser createUser(ApiCreateUserRequest request) {
    String uri = uriFactory.buildUsersUri();
    return performPost(uri, entityFactory.buildRequest(request), ApiUser.class);
  }

  public ApiUser updateUser(String username, ApiUpdateUserRequest request) {
    String uri = uriFactory.buildUserUri(username);
    return performPut(uri, entityFactory.buildRequest(request));
  }

  public ApiUser getUser(String username) {
    String uri = uriFactory.buildUserUri(username);
    return performGet(uri, ApiUser.class);
  }

  public void deleteUser(String username) {
    String uri = uriFactory.buildUserUri(username);
    performDelete(uri);
  }

  public ApiUserBatch uploadUserBatch(Resource resource) {
    return performPost(
        uriFactory.buildUserBatchesUri(), entityFactory.buildRequest(resource), ApiUserBatch.class);
  }

  public ApiUserBatch getUserBatch(String id) {
    return performGet(uriFactory.buildUserBatchUri(id), ApiUserBatch.class);
  }

  public Collection<ApiUserBatch> getAllUserBatches() {
    ApiUserBatch[] batches = performGet(uriFactory.buildUserBatchesUri(), ApiUserBatch[].class);
    return toCollection(batches);
  }

  public Collection<ApiCandidateGamePlayer> getAllCandidatePlayers() {
    ApiCandidateGamePlayer[] players =
        performGet(uriFactory.buildGetCandidatePlayersUri(), ApiCandidateGamePlayer[].class);
    return toCollection(players);
  }

  public void deleteAllUserBatches() {
    performDelete(uriFactory.buildUserBatchesUri());
  }

  public void synchronizeExternalUsers() {
    String uri = uriFactory.buildExternalUserSynchronizationsUri();
    performPost(uri, entityFactory.buildRequest());
  }

  public Collection<ApiUser> getAllUsers() {
    ApiUser[] users = performGet(uriFactory.buildUsersUri(), ApiUser[].class);
    return toCollection(users);
  }

  public ApiUserPage getUserPage(ApiUserPageRequest request) {
    return performPost(
        uriFactory.buildUsersPagesUri(), entityFactory.buildRequest(request), ApiUserPage.class);
  }

  public Collection<ApiGame> getAllGames() {
    ApiGame[] games = performGet(uriFactory.buildGamesUri(), ApiGame[].class);
    return toCollection(games);
  }

  public ApiGame createGame(ApiCreateGameRequest request) {
    return performPostGame(uriFactory.buildGamesUri(), entityFactory.buildRequest(request));
  }

  public ApiGame takeTurn(long gameId, ApiTurn turn) {
    HttpEntity<ApiTurn> request = entityFactory.buildRequest(turn);
    return performPostGame(uriFactory.buildTakeTurnUri(gameId), request);
  }

  public ApiGame getGame(long gameId) {
    return performGetGame(uriFactory.buildGameUri(gameId));
  }

  public ApiGame getMinimalGame(long gameId) {
    return performGetGame(uriFactory.buildGetMinimalGameUri(gameId));
  }

  public void deleteAllGames() {
    performDelete(uriFactory.buildGamesUri());
  }

  public void deleteGame(long id) {
    performDelete(uriFactory.buildGameUri(id));
  }

  public void resetIds() {
    performDelete(uriFactory.buildIdsUri());
  }

  private ApiGame performGetGame(String uri) {
    return performGet(uri, ApiGame.class);
  }

  private <T> T performGet(String uri, Class<T> responseType) {
    ResponseEntity<T> response =
        template.exchange(uri, HttpMethod.GET, entityFactory.buildRequest(), responseType);
    return toBodyIfNotNull(response);
  }

  private void performPost(String uri, HttpEntity<?> request) {
    template.exchange(uri, HttpMethod.POST, request, Void.class);
  }

  private ApiGame performPostGame(String uri, HttpEntity<?> request) {
    return performPost(uri, request, ApiGame.class);
  }

  private <T> T performPost(String uri, HttpEntity<?> request, Class<T> responseType) {
    return performUpdate(uri, HttpMethod.POST, request, responseType);
  }

  private ApiUser performPut(String uri, HttpEntity<?> request) {
    return performUpdate(uri, HttpMethod.PUT, request, ApiUser.class);
  }

  private <T> T performUpdate(
      String uri, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
    try {
      ResponseEntity<T> response = template.exchange(uri, method, request, responseType);
      return toBodyIfNotNull(response);
    } catch (HttpClientErrorException e) {
      throw new NaughtsAndCrossesApiClientException(e.getMessage());
    }
  }

  private void performDelete(String uri) {
    template.exchange(uri, HttpMethod.DELETE, entityFactory.buildRequest(), Void.class);
  }

  private static <T> T toBodyIfNotNull(HttpEntity<T> response) {
    return Optional.ofNullable(response)
        .map(HttpEntity::getBody)
        .orElseThrow(
            () -> new NaughtsAndCrossesApiClientException("null response body returned from api"));
  }

  private static <T> Collection<T> toCollection(T[] array) {
    return Optional.ofNullable(array).map(List::of).orElse(Collections.emptyList());
  }
}
