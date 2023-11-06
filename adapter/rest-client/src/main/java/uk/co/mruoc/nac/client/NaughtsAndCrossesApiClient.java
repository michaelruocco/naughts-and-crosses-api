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

    private final String baseUrl;
    private final RestTemplate template;

    public NaughtsAndCrossesApiClient(String baseUrl) {
        this(baseUrl, new RestTemplate());
    }

    public Collection<ApiGame> getAllGames() {
        ApiGame[] games = template.getForObject(buildGamesUri(), ApiGame[].class);
        return Optional.ofNullable(games).map(List::of).orElse(Collections.emptyList());
    }

    public ApiGame createGame() {
        return template.postForObject(buildGamesUri(), null, ApiGame.class);
    }

    public ApiGame takeTurn(long gameId, ApiTurn turn) {
        return template.postForObject(buildTakeTurnUri(gameId), turn, ApiGame.class);
    }

    public void deleteAllGames() {
        template.delete(buildGamesUri());
    }

    public void resetIds() {
        template.delete(buildIdsUri());
    }

    private String buildGamesUri() {
        return String.format("%s/v1/games", baseUrl);
    }

    private String buildTakeTurnUri(long gameId) {
        return String.format("%s/v1/games/%d/turns", baseUrl, gameId);
    }

    private String buildIdsUri() {
        return String.format("%s/v1/ids", baseUrl);
    }
}
