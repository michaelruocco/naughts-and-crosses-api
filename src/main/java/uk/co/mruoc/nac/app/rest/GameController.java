package uk.co.mruoc.nac.app.rest;

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
import uk.co.mruoc.nac.app.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Turn;
import uk.co.mruoc.nac.app.domain.usecases.GameService;

@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService service;
    private final ApiConverter converter;

    public GameController() {
        this(new GameService(), new ApiConverter());
    }

    @PostMapping
    public ApiGame createGame() {
        Game game = service.createGame();
        return converter.toApiGame(game);
    }

    @PutMapping("/{gameId}/turns")
    public ApiGame takeTurn(@PathVariable UUID gameId, @RequestBody ApiTurn apiTurn) {
        Turn turn = converter.toTurn(apiTurn);
        Game game = service.takeTurn(gameId, turn);
        return converter.toApiGame(game);
    }
}
