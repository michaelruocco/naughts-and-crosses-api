package uk.co.mruoc.nac.app;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.GameJsonMother;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

class NaughtsAndCrossesAppIntegrationTest {

    @RegisterExtension
    public static final NaughtsAndCrossesAppExtension EXTENSION = new NaughtsAndCrossesAppExtension();

    @Test
    void shouldReturnNoGamesInitially() {
        NaughtsAndCrossesApiClient client = EXTENSION.getAppClient();

        Collection<ApiGame> games = client.getAllGames();

        assertThat(games).isEmpty();
    }

    @Test
    void shouldCreateGame() {
        NaughtsAndCrossesApiClient client = EXTENSION.getAppClient();

        ApiGame game = client.createGame();

        assertThatJson(game).isEqualTo(GameJsonMother.initial());
    }

    @Test
    void shouldReturnGame() {
        NaughtsAndCrossesApiClient client = EXTENSION.getAppClient();
        ApiGame createdGame = client.createGame();

        ApiGame game = client.getGame(createdGame.getId());

        assertThatJson(game).isEqualTo(createdGame);
    }

    @Test
    void shouldReturnMinimalGame() {
        NaughtsAndCrossesApiClient client = EXTENSION.getAppClient();
        ApiGame createdGame = client.createGame();

        ApiGame game = client.getMinimalGame(createdGame.getId());

        assertThatJson(game).whenIgnoringPaths("board", "players").isEqualTo(createdGame);
        assertThat(game.getBoard()).isNull();
        assertThat(game.getPlayers()).isNull();
    }

    @Test
    void gameShouldCompleteWithXWinner() {
        NaughtsAndCrossesApiClient client = EXTENSION.getAppClient();
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
    void gameShouldCompleteWithDraw() {
        NaughtsAndCrossesApiClient client = EXTENSION.getAppClient();
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
}
