package uk.co.mruoc.nac.client;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;

@RequiredArgsConstructor
public class NaughtsAndCrossesApiClient {

  private final UriFactory uriFactory;
  private final RestTemplate template;

  public NaughtsAndCrossesApiClient(String baseUrl) {
    this(new UriFactory(baseUrl), new RestTemplate());
  }

  public Collection<ApiGame> getAllGames() {
    ApiGame[] games = template.getForObject(uriFactory.buildGamesUri(), ApiGame[].class);
    return Optional.ofNullable(games).map(List::of).orElse(Collections.emptyList());
  }

  public ApiGame createGame() {
    return template.postForObject(uriFactory.buildGamesUri(), null, ApiGame.class);
  }

  public ApiGame takeTurn(long gameId, ApiTurn turn) {
    return template.postForObject(uriFactory.buildTakeTurnUri(gameId), turn, ApiGame.class);
  }

  public ApiGame getGame(long gameId) {
    return template.getForObject(uriFactory.buildGetGameUri(gameId), ApiGame.class);
  }

  public ApiGame getMinimalGame(long gameId) {
    return template.getForObject(uriFactory.buildGetMinimalGameUri(gameId), ApiGame.class);
  }

  public void deleteAllGames() {
    template.delete(uriFactory.buildGamesUri());
  }

  public void resetIds() {
    template.delete(uriFactory.buildIdsUri());
  }
}
