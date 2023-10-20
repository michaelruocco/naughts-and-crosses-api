package uk.co.mruoc.nac.app.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.app.api.ApiGame;
import uk.co.mruoc.nac.app.api.ApiTurn;
import uk.co.mruoc.nac.app.api.converter.ApiGameConverter;
import uk.co.mruoc.nac.app.api.converter.ApiTurnConverter;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Turn;
import uk.co.mruoc.nac.app.domain.usecases.GameFactory;
import uk.co.mruoc.nac.app.domain.usecases.GameNotFoundException;

@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameFactory factory;
    private final Map<UUID, Game> games;
    private final ApiGameConverter gameConverter;
    private final ApiTurnConverter turnConverter;

    public GameController() {
        this(new GameFactory(), new HashMap<>(), new ApiGameConverter(), new ApiTurnConverter());
    }

    @PostMapping
    public ApiGame createGame() {
        Game game = factory.buildGame();
        saveGame(game);
        return gameConverter.toApiGame(game);
    }

    @PutMapping
    @RequestMapping("/{gameId}/turns")
    public ApiGame takeTurn(@PathVariable UUID gameId, @RequestBody ApiTurn apiTurn) {
        Turn turn = turnConverter.toTurn(apiTurn);
        Game game = Optional.ofNullable(games.get(gameId)).orElseThrow(() -> new GameNotFoundException(gameId));
        Game updatedGame = game.take(turn);
        saveGame(updatedGame);
        return gameConverter.toApiGame(updatedGame);
    }

    private void saveGame(Game game) {
        games.put(game.getId(), game);
    }
}
