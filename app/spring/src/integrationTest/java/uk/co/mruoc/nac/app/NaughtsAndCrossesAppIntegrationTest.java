package uk.co.mruoc.nac.app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestTemplate;
import uk.co.mruoc.nac.api.dto.ApiGame;

class NaughtsAndCrossesAppIntegrationTest {

    @RegisterExtension
    public static final NaughtsAndCrossesAppExtension EXTENSION = new NaughtsAndCrossesAppExtension();

    @Test
    void shouldReturnNoGamesInitially() {
        RestTemplate template = new RestTemplate();
        String uri = String.format("%s/v1/games", EXTENSION.getAppBaseUrl());

        ApiGame[] games = template.getForObject(uri, ApiGame[].class);

        assertThat(games).isEmpty();
    }
}
