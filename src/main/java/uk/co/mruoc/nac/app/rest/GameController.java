package uk.co.mruoc.nac.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.app.api.ApiBoard;
import uk.co.mruoc.nac.app.api.ApiGame;
import uk.co.mruoc.nac.app.api.ApiLocation;
import uk.co.mruoc.nac.app.api.ApiPlayer;
import uk.co.mruoc.nac.app.api.ApiStatus;
import uk.co.mruoc.nac.app.api.ApiTurn;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final Map<UUID, ApiGame> games;

    public GameController() {
        this(new HashMap<>());
    }

    @PostMapping
    public ApiGame createGame() {
        ApiGame game = buildGame();
        saveGame(game);
        return game;
    }

    @PutMapping
    @RequestMapping("/{gameId}/turns")
    public ApiGame takeTurn(@PathVariable UUID gameId, @RequestBody ApiTurn turn) {
        ApiGame game = Optional.ofNullable(games.get(gameId)).orElseThrow(() -> new GameNotFoundException(gameId));
        ApiGame updatedGame = game.take(turn);
        saveGame(updatedGame);
        return updatedGame;
    }

    private void saveGame(ApiGame game) {
        games.put(game.getId(), game);
    }

    private static ApiGame buildGame() {
        ApiPlayer player1 = player1();
        ApiPlayer player2 = player2();
        return ApiGame.builder()
                .id(UUID.fromString("5b6bff66-7361-4d8b-9eb1-d7b3500a6bf3"))
                .status(initialStatus(player1))
                .players(List.of(player1, player2))
                .board(initialBoard())
                .build();
    }

    private static ApiPlayer player1() {
        return ApiPlayer.builder()
                .name("Player 1")
                .token('X')
                .build();
    }

    private static ApiPlayer player2() {
        return ApiPlayer.builder()
                .name("Player 2")
                .token('O')
                .build();
    }

    private static ApiStatus initialStatus(ApiPlayer nextPlayer) {
        return ApiStatus.builder()
                .complete(false)
                .nextPlayer(nextPlayer)
                .build();
    }

    private static ApiBoard initialBoard() {
        return ApiBoard.builder()
                .size(3)
                .locations(locations())
                .build();
    }

    private static Collection<ApiLocation> locations() {
        return List.of(
                new ApiLocation(0, 0),
                new ApiLocation(1, 0),
                new ApiLocation(2, 0),
                new ApiLocation(0, 1),
                new ApiLocation(1, 1),
                new ApiLocation(2, 1),
                new ApiLocation(0, 2),
                new ApiLocation(1, 2),
                new ApiLocation(2, 2)
        );
    }
}
