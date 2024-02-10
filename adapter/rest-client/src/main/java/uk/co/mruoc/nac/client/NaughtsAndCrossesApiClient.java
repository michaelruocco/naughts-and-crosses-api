package uk.co.mruoc.nac.client;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUser;

@RequiredArgsConstructor
@Slf4j
public class NaughtsAndCrossesApiClient {

  private final UriFactory uriFactory;
  private final HttpEntityFactory entityFactory;
  private final RestTemplate template;

  public NaughtsAndCrossesApiClient(String baseUrl) {
    this(baseUrl, null);
  }

  public NaughtsAndCrossesApiClient(String baseUrl, String token) {
    this(new UriFactory(baseUrl), new HttpEntityFactory(token), new RestTemplate());
  }

  public Collection<ApiUser> getAllUsers() {
    ApiUser[] users = performGet(uriFactory.buildUsersUri(), ApiUser[].class);
    return toCollection(users);
  }

  public Collection<ApiGame> getAllGames() {
    ApiGame[] games = performGet(uriFactory.buildGamesUri(), ApiGame[].class);
    return toCollection(games);
  }

  public ApiGame createGame(ApiCreateGameRequest request) {
    return performPost(uriFactory.buildGamesUri(), entityFactory.buildRequest(request));
  }

  public ApiGame takeTurn(long gameId, ApiTurn turn) {
    HttpEntity<ApiTurn> request = entityFactory.buildRequest(turn);
    return performPost(uriFactory.buildTakeTurnUri(gameId), request);
  }

  public ApiGame getGame(long gameId) {
    return performGetGame(uriFactory.buildGetGameUri(gameId));
  }

  public ApiGame getMinimalGame(long gameId) {
    return performGetGame(uriFactory.buildGetMinimalGameUri(gameId));
  }

  public void deleteAllGames() {
    performDelete(uriFactory.buildGamesUri());
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

  private ApiGame performPost(String uri, HttpEntity<?> request) {
    ResponseEntity<ApiGame> response =
        template.exchange(uri, HttpMethod.POST, request, ApiGame.class);
    return toBodyIfNotNull(response);
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
