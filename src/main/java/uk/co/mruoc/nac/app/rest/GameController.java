package uk.co.mruoc.nac.app.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.app.api.ApiBoard;
import uk.co.mruoc.nac.app.api.ApiGame;
import uk.co.mruoc.nac.app.api.ApiLocation;
import uk.co.mruoc.nac.app.api.ApiPlayer;
import uk.co.mruoc.nac.app.api.ApiStatus;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/games")
public class GameController {

    @GetMapping
    public ApiGame createGame() {
        ApiPlayer player1 = player1();
        ApiPlayer player2 = player2();
        return ApiGame.builder()
                .id(UUID.randomUUID())
                .status(initialStatus(player1))
                .players(List.of(player1, player2))
                .board(initialBoard())
                .build();
    }

    private static ApiPlayer player1() {
        return ApiPlayer.builder()
                .id(UUID.randomUUID())
                .name("Player 1")
                .token('X')
                .build();
    }

    private static ApiPlayer player2() {
        return ApiPlayer.builder()
                .id(UUID.randomUUID())
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
                new ApiLocation(0, 0, '-'),
                new ApiLocation(1, 0, '-'),
                new ApiLocation(2, 0, '-'),
                new ApiLocation(0, 1, '-'),
                new ApiLocation(1, 1, '-'),
                new ApiLocation(2, 1, '-'),
                new ApiLocation(0, 2, '-'),
                new ApiLocation(1, 2, '-'),
                new ApiLocation(2, 2, '-')
        );
    }
}
